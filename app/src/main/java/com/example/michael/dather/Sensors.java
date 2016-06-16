package com.example.michael.dather;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;
import com.example.michael.dather.MODEL.Entry;
import com.example.michael.dather.MODEL.MySQLiteHelper;
import com.example.michael.dather.SECURITY.Encrypt;

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

    private String userId = "";
    private float lightValue = 0f;
    private float stepValue = 0f;
    private float accelerometerValueX = 0f;
    private float accelerometerValueY = 0f;
    private float accelerometerValueZ = 0f;
    private double latitude = 0f;
    private double longitude = 0f;
    private Handler handler = new Handler();
    private MySQLiteHelper mySQLiteHelper;
    private Encrypt encryptor;

    public ArrayList<Entry> entries = new ArrayList<Entry>();

    /** CONSTRUCTOR */
    public Sensors(Context context, final int samplingRate, final String userId) throws IOException {
        this.servingContext = context;
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.SAMPLING_RATE = samplingRate;
        this.userId = userId;

        mySQLiteHelper = new MySQLiteHelper(servingContext);
        this.encryptor = new Encrypt();

        run();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Long tsLong = System.currentTimeMillis() / 1000;
            String ts = tsLong.toString();


            String[] entrieVals = {ts, String.valueOf(lightValue) , String.valueOf(stepValue), String.valueOf(getSoundVolume()), String.valueOf(accelerometerValueX), String.valueOf(accelerometerValueY), String.valueOf(accelerometerValueZ), String.valueOf(longitude), String.valueOf(latitude)};
            Entry entry = new Entry(entrieVals, encryptor);

            entries.add(entry);
            mySQLiteHelper.insertEntry(entry);

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
        latitude = location[0];
        longitude = location[1];
    }

    private void logSensorSteps() {
        Sensor steps = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (steps != null) {
            final float[] pressure = {0};

            SensorEventListener listener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    stepValue = event.values[0];
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
                    lightValue = event.values[0];
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
                    accelerometerValueY = event.values[1];
                    accelerometerValueZ = event.values[2];
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
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

        logSensorSteps();
        logSensorLight();
        logSensorAccelerometer();
        logLocation();

        running = true;
        handler.postDelayed(runnable, 0);
    }
}
