package com.tomgozdek.simplecompass.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.tomgozdek.simplecompass.R
import com.tomgozdek.simplecompass.databinding.CompassFragmentBinding

class CompassFragment : Fragment()
{
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View
    {
        val binding = DataBindingUtil.inflate<CompassFragmentBinding>(inflater, R.layout.compass_fragment, container, false)
        return binding.root
    }

}