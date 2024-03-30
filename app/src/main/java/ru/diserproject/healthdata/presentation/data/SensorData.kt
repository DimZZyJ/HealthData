package ru.diserproject.healthdata.presentation.data

import java.time.LocalDateTime

class SensorData(var heartRate:Double,var presure:Double,var ambientTemp:Double,var humidity :Double) {
    public var Time = LocalDateTime.now()
    public var HeartRate : Double = heartRate
    public var Presure : Double = presure
    public var AmbientTemp : Double = ambientTemp
    public var Humidity : Double = humidity
}