package com.farshidabz.skysense

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.farshidabz.skysense.designsystem.snackbar.SnackbarManager
import com.farshidabz.skysense.navigation.NavigationRoot

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@Preview
fun App() {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        SnackbarManager.events.collect { event ->
            val message = event.message ?: event.messageResId?.let { context.getString(it) } ?: ""
            val action = event.actionLabelResId?.let { context.getString(it) }
            if (message.isNotBlank()) {
                try {
                    snackbarHostState.showSnackbar(message = message, actionLabel = action)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        NavigationRoot(modifier = Modifier.fillMaxSize())
    }
}