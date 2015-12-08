package passwordholder.bridge.com.passwordholder;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import passwordholder.bridge.com.passwordholder.Utils.AppPreferenceManager;

public class SecurityQnFragment extends Fragment {

    TextView title;
    AutoCompleteTextView txtSecurityQn;
    EditText securityAnswer;
    RelativeLayout buttonPane;
    Button btnGo;
    Activity myActivity;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_security_qn, container, false);
        initUi(v);
        return v;
    }
    private void initUi(View v) {

        title=(TextView)v. findViewById(R.id.title);
        buttonPane= (RelativeLayout)v. findViewById(R.id.button_pane);
        txtSecurityQn = (AutoCompleteTextView)v. findViewById(R.id.autocomplete_qn);
        securityAnswer= (EditText)v. findViewById(R.id.answer);
        btnGo=(Button)v. findViewById(R.id.btn_Go);
        setUpValues();
        securityAnswer.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return false;
            }
        });
        btnGo.setOnClickListener(view ->

        {
            if (isValidationSuccess()) {
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.slide_right, R.anim.slide_left);
            }
        });
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            if(myActivity==null){
                myActivity =(Activity)activity;
            }

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnDetailFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        myActivity = null;
    }

    private boolean isValidationSuccess() {
        String sec_qn=txtSecurityQn.getText().toString().trim();
        String sec_ans=securityAnswer.getText().toString().trim();

        if((!TextUtils.isEmpty(sec_qn))&&(!TextUtils.isEmpty(sec_ans)))
        {
            AppPreferenceManager.setValue(getActivity(),AppPreferenceManager.SECURITY_QUESTION,sec_qn);
            AppPreferenceManager.setValue(getActivity(),AppPreferenceManager.SECURITY_ANSWER,sec_ans);
            return true;
        }
        return false;
    }

    private void setUpValues() {

        String[] countries = getResources().getStringArray(R.array.qn_array);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, countries);
        txtSecurityQn.setAdapter(adapter);
        viewFadein(title);
        viewSlideUp(buttonPane);
    }

    private void viewFadein(View v) {

        Animation zoomin = AnimationUtils.loadAnimation(getActivity(), R.anim.fadein);
        zoomin.setDuration(2000);
        v.startAnimation(zoomin);
        v.setVisibility(View.VISIBLE);
    }
    private void viewSlideUp(View v) {

        Animation slidein = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_bottom);
        slidein.setDuration(2000);
        v.startAnimation(slidein);
        v.setVisibility(View.VISIBLE);
    }


}
