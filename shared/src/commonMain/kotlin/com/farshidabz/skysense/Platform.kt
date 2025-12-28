package com.farshidabz.skysense

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform