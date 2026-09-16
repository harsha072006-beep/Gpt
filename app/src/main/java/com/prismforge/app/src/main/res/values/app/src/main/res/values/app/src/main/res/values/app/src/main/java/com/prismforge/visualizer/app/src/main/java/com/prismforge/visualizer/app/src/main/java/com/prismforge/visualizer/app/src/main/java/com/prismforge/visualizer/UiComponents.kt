package com.prismforge.visualizer

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun UiComponents(
    state: VisualizerState,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    state.playing = !state.playing
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(if (state.playing) "Stop" else "Play")
            }

            Box(modifier = Modifier.weight(1f)) {
                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(state.preset)
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    state.presets.forEach { preset ->
                        DropdownMenuItem(
                            text = { Text(preset) },
                            onClick = {
                                state.applyPreset(preset)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Object: ${state.selectedObject}")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            VisualObject.values().forEach { obj ->
                FilterChip(
                    selected = state.selectedObject == obj,
                    onClick = {
                        state.selectedObject = obj
                    },
                    label = { Text(obj.name) }
                )
            }
        }

        Text("Glow: ${(state.glow * 100).toInt()}%")
        Slider(
            value = state.glow,
            onValueChange = { state.glow = it }
        )

        Text("Depth: ${(state.depth * 100).toInt()}%")
        Slider(
            value = state.depth,
            onValueChange = { state.depth = it }
        )

        Text("Audio Intensity: ${(state.audioIntensity * 100).toInt()}%")
        Slider(
            value = state.audioIntensity,
            onValueChange = { state.audioIntensity = it }
        )
    }
}
