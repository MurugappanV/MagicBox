package com.nilhcem.smarthome.androidthings.ui.main

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.google.android.things.contrib.driver.apa102.Apa102
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManager
import com.nilhcem.smarthome.androidthings.data.lights.Lights
import javax.inject.Inject

class MainBoardComponents @Inject constructor() : LifecycleObserver {

    companion object {
        const val GPIO_RELAY = "BCM18"
    }

    private lateinit var relay: Gpio
    private lateinit var ledstrip: Apa102

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        relay = PeripheralManager.getInstance().openGpio(GPIO_RELAY)
        relay.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
        relay.setActiveType(Gpio.ACTIVE_LOW)

        ledstrip = RainbowHat.openLedStrip()
        ledstrip.brightness = 1
    }

    fun setFanOn(on: Boolean) {

        val color = if (on) 1 else 0
        ledstrip.write(IntArray(RainbowHat.LEDSTRIP_LENGTH, { color }))
    }

    fun setLights(lights: Lights) {
        relay.value = !lights.isOn
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        ledstrip.close()
        relay.close()
    }
}
