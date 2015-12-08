package passwordholder.bridge.com.passwordholder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import passwordholder.bridge.com.passwordholder.Utils.AppPreferenceManager;

public class LoginActivity extends AppCompatActivity {

    TextView headText,textHeadSub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUi();
        if( AppPreferenceManager.getIntegerValue(this, AppPreferenceManager.FAILED_ATTEMPTS)<=AppPreferenceManager.MAX_ATTEMPTS) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, new PinFragment()).commit();
        }
        else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, new SecurityQnFragment()).commit();
        }
    }

    private void initUi() {

        headText=(TextView) findViewById(R.id.head_text);
        textHeadSub=(TextView) findViewById(R.id.text_head_sub);
        headText.setText(R.string.welcome_back);
        textHeadSub.setText(R.string.login_text);
    }

    public void gotoMainActivity() {
        Intent startMain = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(startMain);
        finish();
    }
}
