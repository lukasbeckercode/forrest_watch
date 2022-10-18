package com.lukasbeckercode.forrestwatch.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.lukasbeckercode.forrestwatch.Constants
import com.lukasbeckercode.forrestwatch.FireBaseAuth
import com.lukasbeckercode.forrestwatch.R
import com.lukasbeckercode.forrestwatch.location.PermissionManager
import com.lukasbeckercode.forrestwatch.models.MarkedTree
import com.lukasbeckercode.forrestwatch.models.User
import java.util.*

/**
 * Home Class, user is registered and signed in when seeing this Activity
 * Get the current GPS location of the user
 * displays the location using a google maps fragment
 * allows user to name their current location and save it, so infested trees can be located easily
 */
class HomeActivity : AppCompatActivity(), OnMapReadyCallback {
    private var requestCode = 99 //standard value 99 as per SO
    private var map:GoogleMap? = null
    private lateinit var user:User
    var pos:LatLng? = null

    /**
     * gets called every time the Activity is started
     * used to check if the User object has been destroyed by signing out
     */
    override fun onStart() {
        super.onStart()
        if(user.id == ""){ //prevents showing this view if user presses the back button  after sign-out
            Toast.makeText(this,R.string.no_user_signed_in,Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
         user = User()
        //get the logged in user
        if (intent.hasExtra(Constants.intentKeyUser)){
            user = intent.getParcelableExtra(Constants.intentKeyUser)!!
        }

        when {
            //check permissions needed for location services
            PermissionManager().isFineLocationGranted(this) -> {
                when {
                    PermissionManager().isLocationEnabled(this) -> {
                        setLocationListener()
                    }
                    else -> {
                        PermissionManager().showGPSNotEnabledDialog(this)
                    }
                }
            }
            else -> {
                PermissionManager().askAccessFineLocation(
                    this,requestCode
                )
            }
        }

        //deprecated! Needed to maintain compatibility with API Level 23
        LocationRequest().setInterval(5000).setFastestInterval(5000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY) //Needed to maintain compatibility with api level 23
        val tvWelcome :TextView = findViewById(R.id.tv_home_title)
        tvWelcome.text = user.firstname

       val  mapView:SupportMapFragment = getSupportFragmentManager().findFragmentById(R.id.home_map_view) as SupportMapFragment

        mapView.getMapAsync(this)

        val btnSave:Button = findViewById(R.id.btn_home_save)
        btnSave.setOnClickListener {
            val name:String = findViewById<EditText>(R.id.et_home_name).text.toString()
            val tree = MarkedTree(pos!!.latitude,pos!!.longitude,name,user,this, UUID.randomUUID().toString())
            if(!tree.save()){
                Toast.makeText(this,R.string.error_home_tree_name_empty,Toast.LENGTH_LONG)
                    .show()
            }
        }

        val tvGoToTreeView:TextView = findViewById(R.id.tv_home_gotolistview)
        tvGoToTreeView.setOnClickListener {
            startActivity(Intent(this,TreeView::class.java))
        }

        val tvSignOut:TextView = findViewById(R.id.tv_home_logout)
        tvSignOut.setOnClickListener{
            FireBaseAuth().logOut()
            user.destroy()
            Toast.makeText(this,R.string.sign_out_success,Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,MainActivity::class.java))
        }


    }

    /**
     * Checks Location every 2 seconds
     */
    private fun setLocationListener() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        // for getting the current location update after every 2 seconds with high accuracy
        //Deprecated! Used to maintain compatibility with API Level 23
        val locationRequest = LocationRequest().setInterval(2000).setFastestInterval(2000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        //Check permissions again
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        //get the current location and update it
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    val tvLong:TextView = findViewById(R.id.tv_home_long)
                    val tvLat:TextView = findViewById(R.id.tv_home_lat)
                    super.onLocationResult(locationResult)
                    for (location in locationResult.locations) {
                        tvLong.text = location.latitude.toString()
                        tvLat.text = location.longitude.toString()
                        pos = LatLng(location.latitude,location.longitude)
                        map?.addMarker(MarkerOptions().position(pos!!))
                        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(pos!!,15f))
                    }
                }
            },
            Looper.getMainLooper()
        )
    }

    /**
     * Handler for Permission requests
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults:IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            this.requestCode -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    when {
                        PermissionManager().isLocationEnabled(this) -> {
                            setLocationListener()
                        }
                        else -> {
                            PermissionManager().showGPSNotEnabledDialog(this)
                        }
                    }
                } else {
                    Toast.makeText(this, getString(R.string.error_gps_permissions_not_granted),Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * actually show the map
     */
    override fun onMapReady(p0: GoogleMap) {
        map = p0
        map!!.addMarker(MarkerOptions().position(LatLng(48.2,16.3)))
        map!!.moveCamera(CameraUpdateFactory.newLatLng(LatLng(48.2,16.3)))
        map!!.moveCamera(CameraUpdateFactory.zoomTo(15f))
    }

    /**
     * called if tree was saved successfully
     * @see com.lukasbeckercode.forrestwatch.database.CloudFireStore
     */
    fun saveSuccess() {
        Toast.makeText(this,R.string.home_tree_save_success,
            Toast.LENGTH_SHORT).show()
    }
}