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
    private int minSize;

    public void start() {
       // minSize= AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
       // ar = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000,AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT,minSize);
       // ar.startRecording();

        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);

        short bufferSize = 4096;// 2048;


        ar  = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize); //object not created

        short[] tempBuffer = new short[bufferSize];
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

        for (short s : data)
        {
            try {
                Thread.sleep((long) 300.00);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return(Math.abs(s));
        }
       return 0;
    }

}
