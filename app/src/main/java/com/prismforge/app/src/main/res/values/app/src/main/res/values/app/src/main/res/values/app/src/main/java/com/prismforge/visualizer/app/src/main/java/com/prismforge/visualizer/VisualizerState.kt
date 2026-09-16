package com.prismforge.visualizer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

enum class VisualObject { Cube, Sphere, Torus }

class VisualizerState {
    var selectedObject by mutableStateOf(VisualObject.Cube)
    var glow by mutableStateOf(0.5f)
    var depth by mutableStateOf(0.5f)
    var audioIntensity by mutableStateOf(0.6f)
    var playing by mutableStateOf(false)
    var preset by mutableStateOf("Neon Orbit")
    var rotation by mutableStateOf(0.0)

    val presets = listOf(
        "Neon Orbit",
        "Pulse Tunnel",
        "Crystal Wave"
    )

    fun applyPreset(name: String) {
        preset = name
        when (name) {
            "Neon Orbit" -> {
                selectedObject = VisualObject.Cube
                glow = 0.8f
                depth = 0.6f
                audioIntensity = 0.7f
            }
            "Pulse Tunnel" -> {
                selectedObject = VisualObject.Torus
                glow = 0.6f
                depth = 0.9f
                audioIntensity = 0.9f
            }
            "Crystal Wave" -> {
                selectedObject = VisualObject.Sphere
                glow = 0.3f
                depth = 0.4f
                audioIntensity = 0.5f
            }
        }
    }
}
