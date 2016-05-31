package com.example.michael.dather;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import java.io.IOException;
import java.security.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.HOURS;

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


    public ArrayList<ArrayList<String>> params = new ArrayList<ArrayList<String>>();

    /** CONSTRUCTOR */
    public Sensors(Context context, final int samplingRate, final String userId) throws IOException {
        this.servingContext = context;
        this.sensorManager = (SensorManager) context.getSystemService(servingContext.SENSOR_SERVICE);
        this.SAMPLING_RATE = samplingRate;
        this.userId = userId;

        run();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Long tsLong = System.currentTimeMillis() / 1000;
            String ts = tsLong.toString();

            Calendar mCalendar = new GregorianCalendar();
            TimeZone mTimeZone = mCalendar.getTimeZone();
            int mGMTOffset = mTimeZone.getRawOffset();


            SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
            dateFormatGmt.setTimeZone(TimeZone.getTimeZone("UTC"));

            //Local time zone
            SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");

            //Time in GMT
            try {
                String timeSTamp = "" + dateFormatLocal.parse( dateFormatGmt.format(new Date()) );
                Log.i("TSAMP", timeSTamp);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            TimeZone tz = TimeZone.getDefault();
            Date now = new Date();
            int offsetFromUtc = tz.getOffset(now.getTime()) / 1000 / 60;

            String to = "GMT offset is " + offsetFromUtc + " min";
            Log.i("<><>", to);

            ArrayList<String> list = new ArrayList<>();
            list.add(userId);
            list.add(ts);
            list.add(String.valueOf(lightValue));
            list.add(String.valueOf(stepValue));
            list.add(String.valueOf(getSoundVolume()));
            list.add(String.valueOf(accelerometerValueX));
            list.add(String.valueOf(accelerometerValueY));
            list.add(String.valueOf(accelerometerValueZ));
            list.add(String.valueOf(longitude));
            list.add(String.valueOf(latitude));

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
