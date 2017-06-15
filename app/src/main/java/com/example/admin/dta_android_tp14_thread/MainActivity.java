package com.example.admin.dta_android_tp14_thread;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

    private ProgressBar bar;
    private final String PROGRESS_BAR_INCREMENT = "ProgressIncrement";

    private AtomicBoolean isPausing = new AtomicBoolean(false);
    private AtomicBoolean isRunning = new AtomicBoolean(false);

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int p = msg.getData().getInt(PROGRESS_BAR_INCREMENT);
            bar.incrementProgressBy(p);
            Log.d("tp14_thread", "new progress : "+bar.getProgress());
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bar = (ProgressBar) findViewById(R.id.progressBar);
    }


    @Override
    protected void onStart() {
        super.onStart();
        bar.setProgress(0);

        Thread process = new Thread(new Runnable() {

            Bundle messageBundle = new Bundle();
            Message message;

            @Override
            public void run() {
                Log.d("tp14_thread", "thread started");

                isRunning.set(true);

                for(int i=0; i< 100 && isRunning.get(); i++) {
                    try {
                        Log.d("tp14_thread", "thread loop");

                        while (isRunning.get() && isPausing.get()) {
                            Log.d("thread", "sleep");
                            Thread.sleep(1000);
                        }

                        Thread.sleep(40);

                        message = handler.obtainMessage();
                        messageBundle.putInt(PROGRESS_BAR_INCREMENT, 1);
                        message.setData(messageBundle);

                        Log.d("tp14_thread", "thread send message to handler");
                        handler.sendMessage(message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                isRunning.set(false);
                Log.d("tp14_thread", "thread finished");
            }

        });

        isPausing.set(true);
        isRunning.set(false);

        process.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPausing.set(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPausing.set(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        isRunning.set(false);
    }
}
