package com.example.dashboard1
//distance = speed * duration
//duration = end time - start time
//speed
//car name
//gas used litre
//gas topped up
//curr gas = curr gas - used / curr gas + topped
//start time xxxx
//end time xxxx

class CarModel(
    carName: String?,
    carMod: String?,
    duration: Double?,
    speed: Double?,
    dist: Double?,
    plusGas: Double?,
    usedGas: Double?
) {

    var carName: String = "unnamed car"
    var carMod: String = "default model"
    var duration: Double = 0.0
    var speed: Double = 0.0

    private var durationlocal: Double? = duration
    private var speedlocal: Double? = speed

    var dist: Double = speedlocal?.times(durationlocal!!) ?: 0.0

    var plusGas: Double = 0.0
    var usedGas: Double = 0.0
    var currGas: Double = 0.0 + this.plusGas - this.usedGas

}




