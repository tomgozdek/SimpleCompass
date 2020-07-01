package com.tomgozdek.simplecompass.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class CompassViewModel(application: Application) : AndroidViewModel(application)
{
    private val orientationLiveData : LiveData<OrientationDataModel> = OrientationObserver(application)

    fun getCompassLiveData() : LiveData<OrientationDataModel> = orientationLiveData
}