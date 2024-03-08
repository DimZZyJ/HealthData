/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

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
    private lateinit var heartTextView: TextView
    private var sensor: Sensor? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)
        setContentView(R.layout.main_layout)

        heartTextView = findViewById<TextView>(R.id.heartRateTextView)

        setUpSensor()
    }

    private fun setUpSensor(){
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_HEART_RATE){
            heartTextView.text = event.values[0].toString()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        heartTextView.text = "Расчет"
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(
            this,
            sensor,
            SensorManager.SENSOR_DELAY_NORMAL,
            SensorManager.SENSOR_DELAY_UI)

    }

    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }
}

