package passwordholder.bridge.com.passwordholder;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity {

    FrameLayout signUpContainer,headerPane;
    ImageView imageLogo;
    TextView headText,textHeadSub;
    CoordinatorLayout snackBarLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        snackBarLocation=(CoordinatorLayout) findViewById(R.id.snackbarlocation);
        signUpContainer =(FrameLayout) findViewById(R.id.fragment_container);
        headerPane =(FrameLayout) findViewById(R.id.header_pane);
        imageLogo = (ImageView) findViewById(R.id.image_logo);
        headText= (TextView) findViewById(R.id.head_text);
        textHeadSub= (TextView) findViewById(R.id.text_head_sub);
        headText.setText(R.string.welcome_back);
        textHeadSub.setText(R.string.login_text);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageLogo.setTransitionName(getString(R.string.activity_image_trans));
            headerPane.setTransitionName(getString(R.string.activity_layout_trans));
        }
        fillWithSignUpFragment();

    }

    public void setTitleTexts(String title,String sub){
        headText.setText(title);
        textHeadSub.setText(sub);
    }

    private void fillWithSignUpFragment() {

        Fragment newFragment = new SignUpFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.mainfadein, R.anim.splashfadeout);
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.commit();
    }
    public void showSnackBar(String message,String actionText,View.OnClickListener actionListener,int color){
        Snackbar snackBar=  Snackbar.make(snackBarLocation, message, Snackbar.LENGTH_LONG);
        if(actionText!=null) {
            snackBar.setAction(actionText, actionListener).show();
        }
        if(color!=-1){
            View snackBarView = snackBar.getView();
            snackBarView.setBackgroundColor(Color.GREEN);

        }
        snackBar.show();
    }


}
