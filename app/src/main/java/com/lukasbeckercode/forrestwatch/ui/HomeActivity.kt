package com.lukasbeckercode.forrestwatch.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
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
import com.lukasbeckercode.forrestwatch.R
import com.lukasbeckercode.forrestwatch.location.PermissionManager
import com.lukasbeckercode.forrestwatch.models.User

class HomeActivity : AppCompatActivity(), OnMapReadyCallback {
    private var requestCode = 99 //standard value 99 as per SO
    private var map:GoogleMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        var user = User()
        if (intent.hasExtra("user")){
            user = intent.getParcelableExtra("user")!!
        }
        when {

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
        val locationRequest = LocationRequest().setInterval(5000).setFastestInterval(5000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY) //Needed to maintain compatibility with api level 23
        val tvWelcome :TextView = findViewById(R.id.tv_home_title)
        tvWelcome.text = user.firstname

       val  mapView:SupportMapFragment = getSupportFragmentManager().findFragmentById(R.id.home_map_view) as SupportMapFragment
       // var mvBundle:Bundle? = null
        //if (savedInstanceState != null){
          //  mvBundle = savedInstanceState.getBundle("MapViewBundleKey")
        //}
      //  mapView.onCreate(mvBundle)
        mapView.getMapAsync(this)


    }
    private fun setLocationListener() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        // for getting the current location update after every 2 seconds with high accuracy
        val locationRequest = LocationRequest().setInterval(2000).setFastestInterval(2000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
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
                        val pos = LatLng(location.latitude,location.longitude)
                        map?.addMarker(MarkerOptions().position(pos))
                        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(pos,15f))
                    }
                }
            },
            Looper.getMainLooper()
        )
    }

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

    override fun onMapReady(p0: GoogleMap) {
        map = p0
        map!!.addMarker(MarkerOptions().position(LatLng(48.2,16.3)))
        map!!.moveCamera(CameraUpdateFactory.newLatLng(LatLng(48.2,16.3)))
        map!!.moveCamera(CameraUpdateFactory.zoomTo(15f))
    }
}