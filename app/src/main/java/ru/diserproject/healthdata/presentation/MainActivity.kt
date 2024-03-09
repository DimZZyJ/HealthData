package ru.diserproject.healthdata.presentation

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import ru.diserproject.healthdata.R

class MainActivity : ComponentActivity(),SensorEventListener {
    private lateinit var sensorManager: SensorManager
    //UI
    private lateinit var heartTextView: TextView
    private  lateinit var presureTextView: TextView
    //Sensors
    private var heartSensor: Sensor? = null
    private var presureSensor: Sensor? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)
        setContentView(R.layout.main_layout)

        heartTextView = findViewById<TextView>(R.id.heartRateTextView)
        presureTextView = findViewById<TextView>(R.id.presureTextView)

        setUpSensor()
    }

    private fun setUpSensor(){
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        heartSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)
        presureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_HEART_RATE){
            heartTextView.text = "Пульс: ${event.values[0].toString()}"
        }
        if (event?.sensor?.type == Sensor.TYPE_PRESSURE){
            presureTextView.text = "Атм.Дав: ${event.values[0].toString()}"
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        heartTextView.text = "Расчет"
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this,heartSensor,SensorManager.SENSOR_DELAY_NORMAL,SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(this,presureSensor,SensorManager.SENSOR_DELAY_NORMAL,SensorManager.SENSOR_DELAY_UI)
    }

    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }
}

