package com.tomgozdek.simplecompass

fun Float.toDegrees() : Int{
    return Math.toDegrees(this.toDouble()).toInt()
}