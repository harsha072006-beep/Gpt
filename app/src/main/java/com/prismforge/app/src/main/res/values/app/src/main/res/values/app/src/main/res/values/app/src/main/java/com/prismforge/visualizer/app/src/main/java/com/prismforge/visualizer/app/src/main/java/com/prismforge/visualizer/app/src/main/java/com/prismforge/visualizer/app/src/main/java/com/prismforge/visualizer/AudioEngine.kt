package com.prismforge.visualizer

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import kotlin.math.PI
import kotlin.math.sin

object AudioEngine {

    private var audioTrack: AudioTrack? = null
    private var audioThread: Thread? = null
    private var running = false

    fun start(state: VisualizerState) {
        if (running) return

        val sampleRate = 44100

        val minBuffer = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        if (minBuffer <= 0) return

        audioTrack = AudioTrack(
            AudioManager.STREAM_MUSIC,
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            minBuffer * 2,
            AudioTrack.MODE_STREAM
        )

        running = true
        audioTrack?.play()

        audioThread = Thread {
            val buffer = ShortArray(minBuffer / 2)
            var phase = 0.0

            while (running) {
                val intensity = state.audioIntensity
                val frequency = when (state.selectedObject) {
                    VisualObject.Cube -> 220.0
                    VisualObject.Sphere -> 330.0
                    VisualObject.Torus -> 440.0
                }

                for (i in buffer.indices) {
                    buffer[i] = (
                        sin(phase) *
                            12000.0 *
                            intensity
                        ).toInt().toShort()

                    phase += 2.0 * PI * frequency / sampleRate

                    if (phase > 2.0 * PI) {
                        phase -= 2.0 * PI
                    }
                }

                audioTrack?.write(
                    buffer,
                    0,
                    buffer.size
                )
            }
        }

        audioThread?.start()
    }

    fun stop() {
        running = false

        audioThread?.interrupt()
        audioThread = null

        audioTrack?.stop()
        audioTrack?.release()
        audioTrack = null
    }
}
