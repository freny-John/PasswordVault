package passwordholder.bridge.com.passwordholder;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import passwordholder.bridge.com.passwordholder.Utils.AppPreferenceManager;

public class LoginActivity extends AppCompatActivity {
    Button signIn;
    EditText userPassword;
    TextInputLayout layoutPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUi();

    }

    private void initUi() {

        signIn = (Button) findViewById(R.id.btn_signin);
        userPassword= (EditText) findViewById(R.id.last_name);
        layoutPassword= (TextInputLayout) findViewById(R.id.password_layout);
        signIn.setOnClickListener(view -> {
            if (userPassword.getText().toString().trim().equals(AppPreferenceManager.getUserPassword(getApplicationContext()))) {
                gotoMainActivity();
            }else{
                userPassword.setError(getString(R.string.password_incorrect));
            }
        });
    }

    private void gotoMainActivity() {
        Intent startMain = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(startMain);
        finish();
    }
}
