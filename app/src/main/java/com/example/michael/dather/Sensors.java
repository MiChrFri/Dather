package com.example.michael.dather;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by michael on 03/05/16.
 */
public class Sensors  {

    /** PROPERTIES */
    Context servingContext;
    SensorManager sensorManager;
    final int SAMPLING_RATE;

    /** CONSTRUCTOR */
    public Sensors(Context context, final int samplingRate) throws IOException {
        servingContext = context;
        sensorManager = (SensorManager) context.getSystemService(servingContext.SENSOR_SERVICE);
        SAMPLING_RATE = samplingRate;

        getSensorList();



        //LogSoundVolume();
        LogLocation();
        //LogSensorTemperature();
        //LogSensorLight();
        //LogSensorAccelerometer();
    }

    /** SENSORS */
    private List<Sensor> getSensorList() {
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        for(Sensor sensor: sensorList){
            Log.i("AVAILABLE SENSORS:", sensor.getName());
        }

        return sensorList;
    }

    private void LogSoundVolume() {
        final SoundMeter soundmeter = new SoundMeter();
        soundmeter.start();

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.i("MAX: ", "" + soundmeter.getAmplitude());
            }
        }, 0, SAMPLING_RATE);
    }

    private void LogLocation() {
        final LocationRequester locationService = new LocationRequester(servingContext);


        locationService.getLocation();



//        new Timer().scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                locationService.getLocation();
//            }
//        }, 0, SAMPLING_RATE);
    }


    private void LogSensorSteps() {
        Sensor steps = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (steps != null) {
            final float[] pressure = {0};

            SensorEventListener listener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    Log.i("STEPS : ",  "" + event.values[0]);
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {}
            };
            sensorManager.registerListener(listener, steps, SensorManager.SENSOR_DELAY_UI);
        }
    }

    private void LogSensorTemperature() {
        Sensor temperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        if (temperature != null) {
            final float[] pressure = {0};

            SensorEventListener listener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {

                    float ambient_temperature = event.values[0];
                    Log.i("Ambient Temperature:\n ",  String.valueOf(ambient_temperature));
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {}
            };
            sensorManager.registerListener(listener, temperature, SensorManager.SENSOR_DELAY_UI);
        }
    }

    private void LogSensorLight() {
        Sensor light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if(light != null) {
            final float[] lightValue = {0};

            SensorEventListener listener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    lightValue[0] = event.values[0];
                    Log.i("LIGHT: ", "" + lightValue[0]);
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {}
            };
            sensorManager.registerListener(listener, light, SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void LogSensorAccelerometer() {
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if(accelerometer != null) {
            final float[] x = {0};
            final float[] y = {0};
            final float[] z = {0};

            SensorEventListener listener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    x[0] = event.values[0];
                    y[0] = event.values[1];
                    z[0] = event.values[2];
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                }
            };

            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    Log.i("ACC", "x:" + x[0] + " y:" + y[0] + " z:" + z[0]);
                }
            }, 0, SAMPLING_RATE);

            sensorManager.registerListener(listener, accelerometer, SAMPLING_RATE);
        }
    }

}
