package com.tomgozdek.simplecompass.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.tomgozdek.simplecompass.R
import com.tomgozdek.simplecompass.databinding.CompassFragmentBinding

class CompassFragment : Fragment()
{
    private val compassViewModel : CompassViewModel by viewModels()
    private lateinit var binding: CompassFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.compass_fragment, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.compassViewModel = compassViewModel

        return binding.root
    }

}