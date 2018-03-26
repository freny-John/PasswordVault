package com.bridge.passwordholder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridge.passwordholder.R;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import com.bridge.passwordholder.Utils.AppPreferenceManager;

/***
 * Splash screen page
 */

public class SplashActivity extends AppCompatActivity {

    private ImageView imageLogo;
    private RelativeLayout splashContainer;
    int splashTime=2000;
    TextView topText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash1);

        imageLogo =(ImageView) findViewById(R.id.image_logo);
        topText= (TextView) findViewById(R.id.topText);
        splashContainer=(RelativeLayout) findViewById(R.id.splash_container);
        viewAnimate(imageLogo, R.anim.zoom_in);
        viewAnimate(topText, R.anim.shaking);


        new Handler().postDelayed(() -> {
            Intent i;
            if(AppPreferenceManager.getFirstTimeUser(getApplicationContext())) {

                i= new Intent(SplashActivity.this,SignUpActivity. class);

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    loadSharedElementTransition(i);

                }
                else {
                    startActivity(i);
                    finish();
                }
            }
            else {
                i = new Intent(SplashActivity.this,LoginActivity. class);
                startActivity(i);
                finish();
            }

        }, splashTime);
    }


    @SuppressLint("NewApi")
    private void loadSharedElementTransition(Intent i) {

        imageLogo.setTransitionName(getString(R.string.activity_image_trans));
        Pair<View, String> pair1 = Pair.create(imageLogo, getString(R.string.activity_image_trans));
        Pair<View, String> pair2 = Pair.create(splashContainer,getString(R.string.activity_layout_trans));
        //  ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(SplashActivity.this,imageLogo,getString(R.string.activity_image_trans));
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(SplashActivity.this,pair1,pair2);
        startActivity(i, options.toBundle());
        overridePendingTransition(0,0);
        finish();
    }
    private void viewAnimate(View v, int anim) {

        Animation zoomIn = AnimationUtils.loadAnimation(this, anim);
        if(anim==R.anim.zoom_in){
            zoomIn.setDuration(1000);
        }
        v.startAnimation(zoomIn);
        v.setVisibility(View.VISIBLE);
    }

}
