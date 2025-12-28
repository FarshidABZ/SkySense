package com.farshidabz.skysense.designsystem.theme

import androidx.compose.ui.graphics.Color
import kotlin.random.Random

object CardPaletteTheme {
    val CardPalette = listOf(
        Color(0xFF6CD0A6),
        Color(0xFF2B537D),
        Color(0xFF5187BD),
        Color(0xFFEBB06D),
        Color(0xFF9B81AC),
        Color(0xFFE68585)
    )

    fun getColorForCity(cityName: String): Color {
        val random = Random(cityName.hashCode())
        val index = random.nextInt(CardPalette.size)
        return CardPalette[index]
    }
}