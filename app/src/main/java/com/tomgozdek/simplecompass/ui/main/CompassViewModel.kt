package com.tomgozdek.simplecompass.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.tomgozdek.simplecompass.toDegrees

class CompassViewModel(application: Application) : AndroidViewModel(application)
{
    private val orientationLiveData : LiveData<OrientationDataModel> = OrientationObserver(application)

    val azimuthString = Transformations.map(orientationLiveData){
        it.azimuth.toDegrees().toString()
    }

    fun getCompassLiveData() : LiveData<OrientationDataModel> = orientationLiveData
}