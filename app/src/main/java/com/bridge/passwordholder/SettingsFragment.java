package com.bridge.passwordholder;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.andexert.library.RippleView;
import com.bridge.passwordholder.Utils.PLog;

import com.bridge.passwordholder.R;
public class SettingsFragment extends Fragment {

    private ImageView back;
    private MainActivity myActivity;
    RippleView back_button_ripple,resetPinButtonRipple;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_settings, container, false);
        back_button_ripple=(RippleView) v.findViewById(R.id.back_button_ripple);
        resetPinButtonRipple=(RippleView) v.findViewById(R.id.reset_pin_button_ripple);
        back_button_ripple.setOnRippleCompleteListener(rippleView -> myActivity.onBackPressed());
        resetPinButtonRipple.setOnRippleCompleteListener(rippleView -> {
            if (myActivity != null ) {
                myActivity.addResetPinFragment();
            }
        });
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            myActivity=(MainActivity) activity;
        } catch (ClassCastException e) {
            PLog.e(e.getMessage());
        }
    }


}
