package com.example.goo.profinal.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.example.goo.profinal.R;

import java.util.Timer;
import java.util.TimerTask;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

/**
 * Created by Goo on 2018-02-26.
 */

public class Loading_progress extends AppCompatActivity {

    ProgressBar progress;

    Handler handler;
    Runnable runnable;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_progress);

      //  progress = findViewById(R.id.progress);
     //   progress.setVisibility(View.VISIBLE);

        ACProgressFlower dialog = new ACProgressFlower.Builder(Loading_progress.this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Loading...")
                .fadeColor(Color.DKGRAY).build();

        dialog.show();
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

              //  progress.setVisibility(View.GONE);
                timer.cancel();

                Intent intent = new Intent(Loading_progress.this, RealHome.class);
                startActivity(intent);
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        }, 1000, 1000);
    }
}