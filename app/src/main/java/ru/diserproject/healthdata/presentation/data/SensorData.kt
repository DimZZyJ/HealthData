package ru.diserproject.healthdata.presentation.data

import java.time.LocalDateTime

class SensorData() {
    public var HeartRate : Float? = null
    public var Presure : Float? = null
    public var AmbientTemp : Float? = null
    public var Humidity : Float? = null

    public fun IsDataFull() : Boolean {
        if (HeartRate != null && Presure != null && AmbientTemp != null && Humidity != null )
            return true
        else
            return false
    }
}

class SensorRecord(var sensorData : SensorData){
    public var Time = LocalDateTime.now()
    public var Record : SensorData = sensorData
}