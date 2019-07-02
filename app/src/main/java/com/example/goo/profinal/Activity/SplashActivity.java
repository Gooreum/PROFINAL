package com.example.goo.profinal.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.view.WindowManager;

import com.daimajia.androidanimations.library.Techniques;
import com.example.goo.profinal.R;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

/**
 * Created by Goo on 2018-03-06.
 */

public class SplashActivity extends AwesomeSplash {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }

    @Override
    public void initSplash(ConfigSplash configSplash) {

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Background animation
        configSplash.setBackgroundColor(R.color.bg_splash);
        configSplash.setAnimCircularRevealDuration(3000);
        //configSplash.setAnimCircularRevealDuration(500);
        configSplash.setRevealFlagX(Flags.REVEAL_LEFT);
        configSplash.setRevealFlagX(Flags.REVEAL_BOTTOM);

        //Logo
        configSplash.setLogoSplash(R.drawable.question);
        configSplash.setAnimLogoSplashDuration(300);
        // configSplash.setAnimLogoSplashDuration(500);
        configSplash.setAnimLogoSplashTechnique(Techniques.Bounce);

        //Title
        configSplash.setTitleSplash("Let Pro");
        configSplash.setTitleTextColor(R.color.colorAccent);
        configSplash.setTitleTextSize(30f);
         configSplash.setAnimTitleDuration(3000);
        //configSplash.setAnimTitleDuration(500);
        configSplash.setAnimTitleTechnique(Techniques.FlipInX);

    }

    @Override
    public void animationsFinished() {

        startActivity(new Intent(SplashActivity.this, Login.class));
    }
}
