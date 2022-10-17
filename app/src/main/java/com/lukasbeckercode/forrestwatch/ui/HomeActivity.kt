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
import com.lukasbeckercode.forrestwatch.R
import com.lukasbeckercode.forrestwatch.location.PermissionManager
import com.lukasbeckercode.forrestwatch.models.User

class HomeActivity : AppCompatActivity() {
    private var LOCATION_PERMISSION_REQUEST_CODE = 99
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
                    this,LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }
        val locationRequest = LocationRequest().setInterval(5000).setFastestInterval(5000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY) //Needed to maintain compatibility with api level 23
        val tvWelcome :TextView = findViewById(R.id.tv_home_title)
        tvWelcome.text = user.firstname


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
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
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
                    }
                    // Things don't end here
                    // You may also update the location on your web app
                }
            },
            Looper.getMainLooper()
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        // A random request code to listen on later
        permissions: Array<out String>,
        grantResults:IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            // Location Permission
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    when {
                        PermissionManager().isLocationEnabled(this) -> {
                            setLocationListener()
                            // Setting things up
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
}