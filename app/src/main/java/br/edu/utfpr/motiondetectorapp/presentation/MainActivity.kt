package br.edu.utfpr.motiondetectorapp.presentation

import android.Manifest
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.VibratorManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import br.edu.utfpr.motiondetectorapp.presentation.ui.MotionScreen
import br.edu.utfpr.motiondetectorapp.presentation.ui.theme.WearMotionTheme
import kotlin.math.sqrt
import kotlin.random.Random

class MainActivity : ComponentActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null
    private val handler = Handler(Looper.getMainLooper())

    private var magnitude by mutableStateOf(0.0)
    private var statusText by mutableStateOf("INICIALIZANDO")
    private var useMock by mutableStateOf(true)
    private var lastTriggerTime = 0L

    private val thresholdStrong = 8.0
    private val thresholdLight = 1.0
    private val debounceMs = 1000L

    private val mockRunnable = object : Runnable {
        override fun run() {
            val x = Random.nextDouble(-1.0, 1.0)
            val y = Random.nextDouble(-1.0, 1.0)
            val z = Random.nextDouble(-1.0, 1.0)
            val spike = if (Random.nextDouble() > 0.9) Random.nextDouble(5.0, 10.0) else 0.0
            processAcceleration(x + spike, y, z)
            handler.postDelayed(this, 350)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)

        setContent {
            WearMotionTheme {
                MotionScreen(
                    magnitude = magnitude,
                    status = statusText,
                    onToggleMock = { toggleMock() },
                    mockEnabled = useMock
                )
            }
        }

        startMock()
    }

    override fun onResume() {
        super.onResume()
        if (!useMock) registerSensor()
    }

    override fun onPause() {
        super.onPause()
        unregisterAll()
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    override fun onSensorChanged(event: SensorEvent) {
        val (x, y, z) = event.values
        processAcceleration(x.toDouble(), y.toDouble(), z.toDouble())
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    @RequiresPermission(Manifest.permission.VIBRATE)
    private fun processAcceleration(x: Double, y: Double, z: Double) {
        val mag = sqrt(x * x + y * y + z * z)
        magnitude = mag

        val now = System.currentTimeMillis()
        if (now - lastTriggerTime < debounceMs) return

        when {
            mag > thresholdStrong -> {
                statusText = "MOVIMENTO FORTE"
                vibrate(400)
            }
            mag > thresholdLight -> {
                statusText = "MOVIMENTO"
                vibrate(150)
            }
            else -> statusText = "PARADO"
        }
        lastTriggerTime = now
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    private fun vibrate(ms: Long) {
        val vibrator = getSystemService(VibratorManager::class.java).defaultVibrator
        vibrator.vibrate(VibrationEffect.createOneShot(ms, VibrationEffect.DEFAULT_AMPLITUDE))
    }

    private fun toggleMock() {
        if (useMock) {
            stopMock(); registerSensor()
        } else {
            unregisterSensor(); startMock()
        }
        useMock = !useMock
    }

    private fun startMock() {
        handler.post(mockRunnable)
        statusText = "Simulação"
    }

    private fun stopMock() {
        handler.removeCallbacks(mockRunnable)
    }

    private fun registerSensor() {
        sensor?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
            statusText = "Sensor real"
        } ?: run {
            statusText = "Sensor indisponível"
        }
    }

    private fun unregisterSensor() {
        sensorManager.unregisterListener(this)
    }

    private fun unregisterAll() {
        stopMock()
        unregisterSensor()
    }
}
