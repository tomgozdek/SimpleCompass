package com.tomgozdek.simplecompass.compass

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.tomgozdek.simplecompass.R
import com.tomgozdek.simplecompass.databinding.CompassFragmentBinding

class CompassFragment : Fragment()
{
    private val compassViewModel : CompassViewModel by viewModels()
    private lateinit var binding: CompassFragmentBinding

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {

            } else {
                showFeatureDeniedMessage()
            }
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.compass_fragment, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.compassViewModel = compassViewModel

        binding.showDestinationBtn.setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // You can use the API that requires the permission.
                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    showRequestPermissionRationale()
                }
            else -> {
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
        }

        return binding.root
    }

    private fun showRequestPermissionRationale() {
        showDialog(
            R.string.location_permission_request_title,
            R.string.location_permission_request_prompt
        ).setPositiveButton(R.string.request) { _, _->
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }.show()
    }

    private fun showFeatureDeniedMessage() {
        showDialog(
            R.string.feature_denied_title,
            R.string.feature_denied_msg
        ).show()
    }

    private fun showDialog(@StringRes titleId : Int, @StringRes msgId : Int) =
        AlertDialog.Builder(requireContext())
            .setTitle(titleId)
            .setMessage(msgId)
            .setNeutralButton(R.string.ok) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }

}