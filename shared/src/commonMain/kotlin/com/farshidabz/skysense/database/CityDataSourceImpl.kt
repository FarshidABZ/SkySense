package com.farshidabz.skysense.database

class CityDataSourceImpl : CityDataSource {
    override fun getCities() = listOf("Amsterdam", "Tokyo", "Moscow", "Dubai")
}