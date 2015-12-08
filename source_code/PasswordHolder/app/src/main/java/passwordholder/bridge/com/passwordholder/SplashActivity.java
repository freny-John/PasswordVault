package passwordholder.bridge.com.passwordholder;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import passwordholder.bridge.com.passwordholder.Utils.AppPreferenceManager;

public class SplashActivity extends AppCompatActivity {

    ImageView imageLogo;
    RelativeLayout splashContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        imageLogo =(ImageView) findViewById(R.id.image_logo);
        splashContainer=(RelativeLayout) findViewById(R.id.splash_container);

        new Handler().postDelayed(new Runnable() {

            public void run() {
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

            }

        }, 3000);
    }

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


}
