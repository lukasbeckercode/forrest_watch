package com.lukasbeckercode.forrestwatch.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.lukasbeckercode.forrestwatch.Constants
import com.lukasbeckercode.forrestwatch.R
import com.lukasbeckercode.forrestwatch.models.Coordinates

class TreeMapView : AppCompatActivity(),OnMapReadyCallback {
    private lateinit var coordinates:Coordinates
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tree_map_view)
        if(intent.hasExtra(Constants.intentKeyCoordinates)){
            coordinates = intent.getParcelableExtra(Constants.intentKeyCoordinates)!!
        }
        val  mapView: SupportMapFragment = getSupportFragmentManager().findFragmentById(R.id.home_map_view) as SupportMapFragment

        mapView.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap) {
        p0.addMarker(MarkerOptions().position(LatLng(coordinates.lat,coordinates.long)))
        p0.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(coordinates.lat,coordinates.long),30f))
    }
}