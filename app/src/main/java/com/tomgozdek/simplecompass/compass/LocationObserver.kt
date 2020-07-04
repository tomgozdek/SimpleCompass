package com.tomgozdek.simplecompass.compass

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import com.google.android.gms.location.LocationServices

class  LocationObserver(context : Context) : LiveData<Location>(){

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener {location ->
            location?.let {
                value = location
            }
        }
    }
}
