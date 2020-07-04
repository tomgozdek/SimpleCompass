package com.tomgozdek.simplecompass.compass

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CompasViewModelFactory(
    private val orientationObserver: OrientationObserver,
    private val locationObserver: LocationObserver
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CompassViewModel(orientationObserver, locationObserver) as T
    }
}