package com.prismforge.visualizer

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrismForgeApp() {
    val state = remember { VisualizerState() }

    MaterialTheme(colorScheme = darkColorScheme()) {
        Scaffold(
            topBar = {
                SmallTopAppBar(
                    title = { Text("PrismForge") }
                )
            }
        ) { inner ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(inner)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    VisualizerCanvas(state = state)
                }

                UiComponents(
                    state = state,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                )
            }
        }
    }
}
