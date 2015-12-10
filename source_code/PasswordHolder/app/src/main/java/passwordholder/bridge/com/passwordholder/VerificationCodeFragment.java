package passwordholder.bridge.com.passwordholder;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Anu on 12/9/2015.
 */
public class VerificationCodeFragment extends Fragment {

    Activity myActivity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_verification, container, false);
        if(myActivity!=null&&myActivity instanceof LoginActivity){
         ((LoginActivity) myActivity).setTitleTexts(myActivity.getString(R.string.verification_code), myActivity.getString(R.string.entr_code));
        }
        return v;
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
}
