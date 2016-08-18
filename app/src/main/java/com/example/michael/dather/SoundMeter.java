package com.example.michael.dather;

/**
 * Created by michael on 03/05/16.
 */
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

public class SoundMeter {
    private AudioRecord ar = null;

    public void start() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);

        short bufferSize = 4096;

        ar  = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
        ar.startRecording();
    }

    public void stop() {
        if (ar != null) {
            ar.stop();
        }
    }

    public int getAmplitude() {
        short bufferSize = 4096;// 2048;
        short data [] = new short[bufferSize];

        ar.startRecording();
        ar.read(data, 0, bufferSize);
        ar.stop();

        if(data.length > 0) {
            return(Math.abs(data[0]));
        }
        else {
            return 0;
        }
    }
}
