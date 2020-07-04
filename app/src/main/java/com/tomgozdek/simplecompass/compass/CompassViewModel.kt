package com.tomgozdek.simplecompass.compass

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tomgozdek.simplecompass.toDegrees
import java.lang.NumberFormatException

class CompassViewModel(application: Application) : AndroidViewModel(application)
{
    private val latitudeInput = MutableLiveData<Double>()
    private val longitudeInput = MutableLiveData<Double>()
    init {
        latitudeInput.value = 0.0
        longitudeInput.value = 0.0
    }
    private val orientationLiveData : LiveData<OrientationDataModel> = OrientationObserver(application)
    private val locationLiveData = LocationObserver(application)

    val azimuth = Transformations.map(orientationLiveData){
        it.azimuth.toDegrees()
    }

    val isLatitudeValid = Transformations.map(latitudeInput){
        -90 <= it  && it <= 90
    }

    @Suppress("UNUSED_PARAMETER")
    fun onLatitudeChanged(sequence: CharSequence, start : Int, before : Int, count : Int){
        try {
            latitudeInput.value = sequence.toString().toDouble()
        }
        catch (exception : NumberFormatException){}
    }

    val isLongitudeValid = Transformations.map(longitudeInput){
        -180 <= it  && it <= 180
    }

    @Suppress("UNUSED_PARAMETER")
    fun onLongitudeChanged(sequence: CharSequence, start : Int, before : Int, count : Int){
        try {
            longitudeInput.value = sequence.toString().toDouble()
        }
        catch (exception : NumberFormatException){}
    }

    private val showDestinationEvent = MutableLiveData<Boolean>()
    val showDestination : LiveData<Boolean>
        get() = showDestinationEvent

    val destinationBearing = Transformations.map(locationLiveData){location ->
        location?.bearingTo(
            Location("NAME").apply {
                latitude = latitudeInput.value!!
                longitude= longitudeInput.value!!
            }
        )?.toInt()
    }

    val missingDestinationCoordinates : LiveData<Boolean?> = Transformations.map(showDestinationEvent){showDestination ->
        if(showDestination && (latitudeInput.value == 0.0 || longitudeInput.value == 0.0)) true else null
    }


    fun showDestinationRequested() {
        showDestinationEvent.value = true
        locationLiveData.getLastLocation()
    }
}