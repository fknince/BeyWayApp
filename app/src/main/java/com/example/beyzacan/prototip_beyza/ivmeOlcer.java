package com.example.beyzacan.prototip_beyza;

/**
 * Created by fince on 13.04.2018.
 */
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import static android.content.Context.SENSOR_SERVICE;

public class ivmeOlcer implements SensorEventListener {
    private WayActivity wayActivity;
    private Sensor mySensor;
    private SensorManager SM;
    @Override
    public void onSensorChanged(SensorEvent event) {
        double x=event.values[0];
        double y=event.values[1];
        double z=event.values[2];
        wayActivity.sensorVerisiGeldi(z);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    public ivmeOlcer(WayActivity wayActivity)
    {
        this.wayActivity=wayActivity;
        SM = (SensorManager)wayActivity.getSystemService(SENSOR_SERVICE);
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SM.registerListener((SensorEventListener) this, mySensor, 999999999);

    }
    public void  sonlandir()
    {
        SM.unregisterListener((SensorEventListener) this);
    }
}
