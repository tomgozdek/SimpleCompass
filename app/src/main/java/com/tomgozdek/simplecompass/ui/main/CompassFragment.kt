package com.tomgozdek.simplecompass.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.tomgozdek.simplecompass.R
import com.tomgozdek.simplecompass.databinding.CompassFragmentBinding
import com.tomgozdek.simplecompass.toDegrees

class CompassFragment : Fragment()
{
    private val compassViewModel : CompassViewModel by viewModels()
    private lateinit var binding: CompassFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.compass_fragment, container, false)

        compassViewModel.getCompassLiveData().observe(viewLifecycleOwner,Observer { compassData ->
            Log.d("Fragment", "On compass data - ${compassData.azimuth.toDegrees()}")
        })

        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

}