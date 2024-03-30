package ru.diserproject.healthdata.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import ru.diserproject.healthdata.R

class MainActivity : ComponentActivity(),SensorEventListener {
    private lateinit var sensorManager: SensorManager

    private  val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        isGranted: Boolean ->
        if (isGranted)
            Log.i("Permission","Granted")
        else
            Log.i("Permission","Denied")
    }
    //Permissions
    private var _permissionList = listOf<String>(
        Manifest.permission.BODY_SENSORS,
        Manifest.permission.BODY_SENSORS_BACKGROUND,
        Manifest.permission.WAKE_LOCK,
    )
    private var _problematicPermissions = mutableListOf<String>()
    //UI
    private lateinit var heartTextView: TextView
    private  lateinit var presureTextView: TextView
    private lateinit var temperatureTextView: TextView
    private lateinit var humidityTextView: TextView
    //Sensors
    private  var availableSensors = mutableListOf<Sensor>()
    private var heartSensor: Sensor? = null
    private var pressureSensor: Sensor? = null
    private var temperatureSensor: Sensor? = null
    private var humiditySensor:Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)
        setContentView(R.layout.main_layout)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        heartTextView = findViewById(R.id.heartRateTextView)
        presureTextView = findViewById(R.id.pressureTextView)
        temperatureTextView = findViewById(R.id.tempepatureTextView)
        humidityTextView = findViewById(R.id.humidityTextView)

        _permissionList.forEach(){
            if (ContextCompat.checkSelfPermission(this,it) != PackageManager.PERMISSION_GRANTED)
                _problematicPermissions.add(it)
        }
    }

    override fun onStart() {
        super.onStart()
        requestPermission()
        setUpSensor()
    }
    override fun onResume() {
        super.onResume()
        registerAvailableSensors()
    }
    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }
    private fun requestPermission(){
        when{
            !_problematicPermissions.any() -> {
                // Разрешения получены
            }
            else -> {
                _problematicPermissions.forEach(){
                    requestPermissionLauncher.launch(it)
                }
            }
        }
    }
    private fun setUpSensor(){
        heartSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)

        if (heartSensor != null) availableSensors.add(heartSensor!!)
        if (pressureSensor != null) availableSensors.add(pressureSensor!!)
        if (temperatureSensor != null) availableSensors.add(temperatureSensor!!)
        if (humiditySensor != null) availableSensors.add(humiditySensor!!)

        //var sensors = sensorManager.getSensorList(Sensor.TYPE_ALL)
    }
    private fun registerAvailableSensors(){
        availableSensors.forEach{ sensorManager.registerListener(this,it,SensorManager.SENSOR_DELAY_NORMAL,SensorManager.SENSOR_DELAY_UI)}
    }
    override fun onSensorChanged(event: SensorEvent?) {
        when (event?.sensor?.type){
            Sensor.TYPE_HEART_RATE -> heartTextView.text = "Пульс: ${event.values[0]}"
            Sensor.TYPE_PRESSURE -> presureTextView.text = "Атм.Дав: ${event.values[0]}"
            Sensor.TYPE_AMBIENT_TEMPERATURE -> temperatureTextView.text = "Температура: ${event.values[0]}"
            Sensor.TYPE_RELATIVE_HUMIDITY -> humidityTextView.text = "Влажность: ${event.values[0]}"
        }
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }


}

