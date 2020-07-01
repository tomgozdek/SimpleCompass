package com.tomgozdek.simplecompass

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.tomgozdek.simplecompass.databinding.MainActivityBinding
import com.tomgozdek.simplecompass.ui.main.CompassFragment

class MainActivity : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<MainActivityBinding>(this,  R.layout.main_activity)
    }
}