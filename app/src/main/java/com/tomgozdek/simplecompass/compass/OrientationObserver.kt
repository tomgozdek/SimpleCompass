package com.tomgozdek.simplecompass.compass

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.LiveData

class OrientationObserver(context: Context) : LiveData<OrientationDataModel>(), SensorEventListener {

    companion object {
        const val FILTER_FACTOR = 0.20F
    }

    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val accelerometer: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val magnetometer: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    private var accelerometerData = FloatArray(3)
    private var magnetometerData = FloatArray(3)

    private var rotationMatrix = FloatArray(9)

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(sensorEvent: SensorEvent?) {

        when (sensorEvent?.sensor?.type) {
            Sensor.TYPE_MAGNETIC_FIELD -> magnetometerData = filter(sensorEvent.values!!, magnetometerData)
            Sensor.TYPE_ACCELEROMETER -> accelerometerData = filter(sensorEvent.values!!, accelerometerData)
            else -> return
        }

        value = calculateCompassData()

    }

    /**
     * Reduces noise present in sensor readings
     */
    private fun filter(values: FloatArray, output: FloatArray): FloatArray {
        for((index, value) in values.withIndex()){
            output[index] = output[index] + FILTER_FACTOR * (value - output[index])
        }

        return output
    }

    private fun calculateCompassData() : OrientationDataModel {
        SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerData, magnetometerData)
        val orientationData = FloatArray(3)
        SensorManager.getOrientation(rotationMatrix, orientationData)

        return OrientationDataModel(
            orientationData[0],
            orientationData[1],
            orientationData[2]
        )
    }

    override fun onActive() {
        super.onActive()
        listOf(accelerometer, magnetometer).forEach {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onInactive() {
        super.onInactive()
        sensorManager.unregisterListener(this)
    }
}