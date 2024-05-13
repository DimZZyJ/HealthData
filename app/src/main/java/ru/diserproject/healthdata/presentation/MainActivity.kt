package ru.diserproject.healthdata.presentation

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import ru.diserproject.healthdata.R
import ru.diserproject.healthdata.presentation.data.SensorRecord

class MainActivity : ComponentActivity(),SensorEventListener {
    private lateinit var sensorManager: SensorManager
    //UI
    private lateinit var statusTextView: TextView
    private lateinit var heartTextView: TextView
    private  lateinit var pressureTextView: TextView
    private lateinit var temperatureTextView: TextView
    private lateinit var humidityTextView: TextView
    //Sensors
    private  var availableSensors = mutableListOf<Sensor>()
    //Data
    private var sensorDataDictionary: MutableMap<String,MutableList<Float>> = mutableMapOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)
        setContentView(R.layout.main_layout)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        statusTextView = findViewById(R.id.statusTextView)
        heartTextView = findViewById(R.id.heartRateTextView)
        pressureTextView = findViewById(R.id.pressureTextView)
        temperatureTextView = findViewById(R.id.tempepatureTextView)
        humidityTextView = findViewById(R.id.humidityTextView)
    }

    override fun onStart() {
        super.onStart()
        availableSensors = sensorManager.getSensorList(Sensor.TYPE_ALL)

        availableSensors.forEach{
            sensor -> sensorDataDictionary[sensor.stringType] = mutableListOf()
        }

        availableSensors.forEach{
            sensorManager.registerListener(this,it,SensorManager.SENSOR_DELAY_NORMAL,SensorManager.SENSOR_DELAY_UI)
        }
    }
    override fun onResume() {
        super.onResume()
    }
    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        statusTextView.text = "Идет запись..."
        displayData(event)
        if (event != null) {
            sensorDataDictionary[event.sensor.stringType] = event.values.toMutableList()
        }
        if (isDictionatyFull()){
            var sensorRecord = SensorRecord(sensorDataDictionary)
            sensorManager.unregisterListener(this)
            statusTextView.text = "Запись завершена"
        }
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
    private fun displayData(event: SensorEvent?) {
        when (event?.sensor?.type) {
            Sensor.TYPE_HEART_RATE -> heartTextView.text = "Пульс: ${event.values[0]}"
            Sensor.TYPE_PRESSURE -> pressureTextView.text = "Атм.Дав: ${event.values[0]}"
            Sensor.TYPE_AMBIENT_TEMPERATURE -> temperatureTextView.text = "Температура: ${event.values[0]}"
            Sensor.TYPE_RELATIVE_HUMIDITY -> humidityTextView.text = "Влажность: ${event.values[0]}"
        }
    }
    private fun isDictionatyFull() : Boolean{
        var isFull = true
        sensorDataDictionary.values.forEach { valuesList ->
            if (valuesList.isEmpty())
                isFull = false
        }
        return isFull
    }
}