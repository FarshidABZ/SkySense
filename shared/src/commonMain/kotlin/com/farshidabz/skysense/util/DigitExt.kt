package com.farshidabz.skysense.util

fun Int?.orZero(): Int {
    if(this == null) {
        return 0
    } else {
        return this
    }
}

fun Float?.orZero(): Float {
    if(this == null) {
        return 0f
    } else {
        return this
    }
}