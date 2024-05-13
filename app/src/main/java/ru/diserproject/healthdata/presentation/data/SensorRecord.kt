package ru.diserproject.healthdata.presentation.data

import java.time.LocalDateTime

class SensorRecord(var sensorData : MutableMap<String,MutableList<Float>>){
    public var Time = LocalDateTime.now()
    public var Record : MutableMap<String,MutableList<Float>> = sensorData
}