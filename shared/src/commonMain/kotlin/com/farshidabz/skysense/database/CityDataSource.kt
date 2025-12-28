package com.farshidabz.skysense.database

interface CityDataSource {
    fun getCities(): List<String>
}