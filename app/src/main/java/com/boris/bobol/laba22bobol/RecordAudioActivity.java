package com.boris.bobol.laba22bobol;

import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class RecordAudioActivity extends AppCompatActivity {
    MediaRecorder mediaRecorder;
    boolean rState;
    Button button12;
    TextView textView8;
    Timer timer;
    boolean state;
    public final static int OKEY = 2;
    public static String FILE_NAME;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_audio);
        FILE_NAME = getCacheDir().getPath() + "/tmp.tmp";
        rState = false;
        button12 = findViewById(R.id.button12);
        textView8 = findViewById(R.id.textView8);
        timer = null;
        File file = new File(FILE_NAME);
        if(file.exists())
            file.delete();
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(FILE_NAME);
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, getString(R.string.errorRecord), Toast.LENGTH_LONG).show();
            mediaRecorder = null;
        }
    }

    public void onButton12Click(View view) {
        if(mediaRecorder == null){
            Toast.makeText(this, getString(R.string.errorRecord), Toast.LENGTH_LONG).show();
            return;
        }
        if(!rState){
            mediaRecorder.start();
            button12.setText(R.string.stop);
            rState = true;
            timer = new Timer();
            timer.execute();
        }else{
            timer.cancel(true);
            exit();
        }

    }

    private void exit(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mediaRecorder.stop();
                button12.setText(R.string.record);
            }
        });
        rState = false;
        setResult(OKEY);
        finish();
    }

    private class Timer extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... unused) {
            for(int i = 59; i > 0; i--){
                SystemClock.sleep(1000);
                final int ii = i;
                runOnUiThread(new Runnable() {
                    @Override public void run() { textView8.setText(String.valueOf(ii));}
                });
                if (isCancelled()) break;
            }
            return(null);
        }

        @Override protected void onProgressUpdate(Void... unused) {}

        @Override
        protected void onPostExecute(Void unused) {
            exit();
        }
    }
}
