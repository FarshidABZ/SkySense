package com.farshidabz.skysense.database

import kotlin.test.Test
import kotlin.test.assertEquals

class CityDataSourceImplTest {

    @Test
    fun getCities_defaultList_isInExpectedOrder() {
        val dataSource: CityDataSource = CityDataSourceImpl()

        val cities = dataSource.getCities()

        assertEquals(listOf("Amsterdam", "Tokyo", "Moscow", "Dubai"), cities)
    }
}
