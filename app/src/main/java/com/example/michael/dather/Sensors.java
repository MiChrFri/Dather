package com.example.michael.dather;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 03/05/16.
 */
public class Sensors implements Runnable {
    /** PROPERTIES */
    Context servingContext;
    SensorManager sensorManager;
    int SAMPLING_RATE;
    public boolean running = false;

    SoundMeter soundmeter;

    final float[] lightValue = {0};
    final float[] stepValue = {0};
    private float accelerometerValueX = 0f;
    final float[] accelerometerValueY = {0};
    final float[] accelerometerValueZ = {0};
    final double[] latitude = {0};
    final double[] longitude = {0};
    private Handler handler = new Handler();


    public ArrayList<ArrayList<String>> params = new ArrayList<ArrayList<String>>();

    /** CONSTRUCTOR */
    public Sensors(Context context, final int samplingRate) throws IOException {
        servingContext = context;
        sensorManager = (SensorManager) context.getSystemService(servingContext.SENSOR_SERVICE);
        SAMPLING_RATE = samplingRate;

        run();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Long tsLong = System.currentTimeMillis() / 1000;
            String ts = tsLong.toString();

            ArrayList<String> list = new ArrayList<>();
            list.add(ts);
            list.add(String.valueOf(lightValue[0]));
            list.add(String.valueOf(stepValue[0]));
            list.add(String.valueOf(getSoundVolume()));
            list.add(String.valueOf(accelerometerValueX));
            list.add(String.valueOf(accelerometerValueY[0]));
            list.add(String.valueOf(accelerometerValueZ[0]));
            list.add(String.valueOf(longitude[0]));
            list.add(String.valueOf(latitude[0]));

            params.add(list);
            Log.i("SENSORING ...", String.valueOf(list));

            if(running) {
                handler.postDelayed(this, SAMPLING_RATE);
            }
            else {
                if(soundmeter != null) {
                    soundmeter.stop();
                }
            }
        }
    };

    /** SENSORS */
    private List<Sensor> getSensorList() {
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        for(Sensor sensor: sensorList){
            Log.i("AVAILABLE SENSORS:", sensor.getName());
        }

        return sensorList;
    }

    private double getSoundVolume() {
        soundmeter = new SoundMeter();
        soundmeter.start();

        soundmeter.getAmplitude();

        return soundmeter.getAmplitude();
    }

    private void logLocation() {
        final LocationRequester locationService = new LocationRequester(servingContext);
        double[] location = locationService.getLocation();
        latitude[0] = location[0];
        longitude[0] = location[1];
    }

    private void logSensorSteps() {
        Sensor steps = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (steps != null) {
            final float[] pressure = {0};

            SensorEventListener listener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    stepValue[0] = event.values[0];
                }
                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {}
            };
            sensorManager.registerListener(listener, steps, SensorManager.SENSOR_DELAY_UI);
        }
    }

    private void logSensorLight() {
        Sensor light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if(light != null) {
            SensorEventListener listener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    lightValue[0] = event.values[0];
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {}
            };
            sensorManager.registerListener(listener, light, SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void logSensorAccelerometer() {
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if(accelerometer != null) {
            SensorEventListener listener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    accelerometerValueX = event.values[0];
                    accelerometerValueY[0] = event.values[1];
                    accelerometerValueZ[0] = event.values[2];
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                }
            };

            sensorManager.registerListener(listener, accelerometer, SAMPLING_RATE);
        }
    }

    @Override
    public void run() {
        logSensorSteps();
        logSensorLight();
        logSensorAccelerometer();
        logLocation();

        running = true;
        handler.postDelayed(runnable, 0);
    }
}
