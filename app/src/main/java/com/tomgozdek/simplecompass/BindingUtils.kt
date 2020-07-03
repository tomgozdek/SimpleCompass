package com.tomgozdek.simplecompass

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText

@BindingAdapter("latError")
fun TextInputEditText.latError(isValid : Boolean){
    error = if(isValid) null else context.getString(R.string.latitude_range_error)
}

@BindingAdapter("longError")
fun TextInputEditText.longError(isValid : Boolean){
    error = if(isValid) null else context.getString(R.string.longitude_range_error)
}