package com.tomgozdek.simplecompass.compass

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tomgozdek.simplecompass.toDegrees
import java.lang.NumberFormatException

class CompassViewModel(application: Application) : AndroidViewModel(application)
{
    private val latitude = MutableLiveData<Double>()
    private val longitude = MutableLiveData<Double>()
    init {
        latitude.value = 0.0
        longitude.value = 0.0
    }
    private val orientationLiveData : LiveData<OrientationDataModel> = OrientationObserver(application)

    val azimuth = Transformations.map(orientationLiveData){
        it.azimuth.toDegrees()
    }

    val isLatitudeValid = Transformations.map(latitude){
        -90 <= it  && it <= 90
    }

    @Suppress("UNUSED_PARAMETER")
    fun onLatitudeChanged(sequence: CharSequence, start : Int, before : Int, count : Int){
        try {
            latitude.value = sequence.toString().toDouble()
        }
        catch (exception : NumberFormatException){}
    }

    val isLongitudeValid = Transformations.map(longitude){
        -180 <= it  && it <= 180
    }

    @Suppress("UNUSED_PARAMETER")
    fun onLongitudeChanged(sequence: CharSequence, start : Int, before : Int, count : Int){
        try {
            longitude.value = sequence.toString().toDouble()
        }
        catch (exception : NumberFormatException){}
    }
}