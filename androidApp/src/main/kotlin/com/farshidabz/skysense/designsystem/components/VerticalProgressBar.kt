package com.farshidabz.skysense.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

@Composable
fun VerticalProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    trackColor: Color = Color.Gray.copy(alpha = 0.3f),
    progressColor: Color = Color.White
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(trackColor),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(progress)
                .background(progressColor)
        )
    }
}