package ru.diserproject.healthdata.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import ru.diserproject.healthdata.R

class ValidationActivity : ComponentActivity() {
    private var permissionGranted = false

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.validation_layout)

        _permissionList.forEach(){
            if (ContextCompat.checkSelfPermission(this,it) != PackageManager.PERMISSION_GRANTED)
                _problematicPermissions.add(it)
        }
    }

    override fun onStart() {
        super.onStart()
        requestPermission()
    }

    override fun onResume() {
        super.onResume()
        if (permissionGranted)
            startActivity(Intent(this,MainActivity::class.java))
    }
    private fun requestPermission(){
        when{
            !_problematicPermissions.any() -> {
                permissionGranted = true
            }
            else -> {
                _problematicPermissions.forEach(){
                    requestPermissionLauncher.launch(it)
                }
            }
        }
    }
}