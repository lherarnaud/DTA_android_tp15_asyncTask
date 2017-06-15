package com.example.admin.dta_android_tp15_asyncTask;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

    ProgressBar bar;
    MyProgressAsyncTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        task = new MyProgressAsyncTask();
        bar = (ProgressBar) findViewById(R.id.progressBar);

        task.execute(new ProgressBar[] { bar });
    }

    class MyProgressAsyncTask extends AsyncTask<ProgressBar, Integer, String> {

        ProgressBar bar;

        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), "Démarrage", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(ProgressBar... progressBars) {
            Log.d("tp15_asyncTask", "task doInBackground");

            bar = progressBars[0];

            int progress;
            for(progress=0; progress <= 100; progress++) {
                if(isCancelled())
                    break;
                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {

                }
                publishProgress(progress);
            }

            return "END !";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            bar.setProgress(values[0].intValue());
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }

    };
}
