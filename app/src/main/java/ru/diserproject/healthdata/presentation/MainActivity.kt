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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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

    private var _permissionList = listOf<String>(
        Manifest.permission.BODY_SENSORS,
        Manifest.permission.BODY_SENSORS_BACKGROUND,
        Manifest.permission.WAKE_LOCK,
    )
    private var _problematicPermissions = mutableListOf<String>()
    //UI
    private lateinit var heartTextView: TextView
    private  lateinit var presureTextView: TextView
    //Sensors
    private var heartSensor: Sensor? = null
    private var pressureSensor: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)
        setContentView(R.layout.main_layout)

        heartTextView = findViewById(R.id.heartRateTextView)
        presureTextView = findViewById(R.id.pressureTextView)

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
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        heartSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_HEART_RATE){
            heartTextView.text = "Пульс: ${event.values[0]}"
        }
        if (event?.sensor?.type == Sensor.TYPE_PRESSURE){
            presureTextView.text = "Атм.Дав: ${event.values[0]}"
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this,heartSensor,SensorManager.SENSOR_DELAY_NORMAL,SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(this,pressureSensor,SensorManager.SENSOR_DELAY_NORMAL,SensorManager.SENSOR_DELAY_UI)
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }
}

