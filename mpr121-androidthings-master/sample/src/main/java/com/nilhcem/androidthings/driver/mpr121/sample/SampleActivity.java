package com.nilhcem.androidthings.driver.mpr121.sample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManager;

import java.io.IOException;

public class SampleActivity extends Activity {

    private static final String TAG = SampleActivity.class.getSimpleName();
    private static final String MPR121_I2C = "I2C1";
    //private static final String BUZZER_GPIO = "PWM1";
    private static final String GPIO_RELAY = "BCM17";

    private final Mpr121Helper mpr121 = new Mpr121Helper();

    private final PassiveBuzzerHelper buzzer = new PassiveBuzzerHelper();
    private Gpio relay;
    //Place resources in res/raw directory if you want to use the SoundPoolHelper instead
    //private final SoundPoolHelper soundPool = new SoundPoolHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            //buzzer.init(BUZZER_GPIO);
            relay = PeripheralManager.getInstance().openGpio(GPIO_RELAY);
            relay.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            relay.setActiveType(Gpio.ACTIVE_LOW);
            //soundPool.init(this);
            mpr121.init(MPR121_I2C, new Mpr121Helper.OnTouchListener() {
                @Override
                public void onSensorTouched(int id) {
                    //buzzer.play(id);
                    try {
                        relay.setValue(true);
                    } catch(Exception ex) {

                    }
                    //soundPool.play(id);
                }

                @Override
                public void onSensorReleased(int id) {
                    try {
                        relay.setValue(false);
                    } catch(Exception ex) {

                    }
                }
            });
        } catch (IOException e) {
            Log.e(TAG, "Error initializing project", e);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean result = mpr121.onKeyDown(keyCode);
        if (!result) {
            result = super.onKeyDown(keyCode, event);
        }
        return result;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        boolean result = mpr121.onKeyUp(keyCode);
        if (!result) {
            result = super.onKeyUp(keyCode, event);
        }
        return result;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            relay.close();
//            buzzer.close();
            //soundPool.close();
            mpr121.close();
        } catch (IOException e) {
            Log.e(TAG, "Error closing I/Os");
        }
    }
}
