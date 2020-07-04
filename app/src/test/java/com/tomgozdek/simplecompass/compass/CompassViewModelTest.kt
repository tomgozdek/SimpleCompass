package com.tomgozdek.simplecompass.compass

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tomgozdek.simplecompass.getOrAwaitValue
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
class CompassViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var compassViewModel : CompassViewModel

    private lateinit var orientationObserverMock : OrientationObserver
    private lateinit var locationObserverMock : LocationObserver

    @Before
    fun setup() {
        orientationObserverMock = Mockito.mock(OrientationObserver::class.java)
        locationObserverMock = Mockito.mock(LocationObserver::class.java)
        compassViewModel = CompassViewModel(orientationObserverMock, locationObserverMock)
    }

    @Test
    fun showDestinationRequested_setsShowDestinationEvent() {

        compassViewModel.showDestinationRequested()

        assertThat(compassViewModel.showDestination.getOrAwaitValue(), `is`(true))
    }
}