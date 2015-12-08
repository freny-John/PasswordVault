package passwordholder.bridge.com.passwordholder;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import passwordholder.bridge.com.passwordholder.Utils.Crypto;
import passwordholder.bridge.com.passwordholder.provider.ProviderMetadata;


public class AddAccountFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Button btn_add;
    EditText accountName,accountUsername,accountPassword,accountDetails;
    TextInputLayout accountLayoutName,accountLayoutUsername,accountLayoutPassword,accountLayoutDetails;
    MainActivity myActivity;
    Toolbar mToolbar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_add_account, container, false);
        initUi(v);
        btn_add.setOnClickListener(view ->{
            getAccountDetails();
        } );
        return v;
    }

    private void initUi(View v) {

        btn_add= (Button) v.findViewById(R.id.btn_add);
        accountName=(EditText) v.findViewById(R.id.account_name);
        accountUsername=(EditText)v.findViewById(R.id.account_username);
        accountPassword=(EditText)v.findViewById(R.id.account_password);
        accountDetails=(EditText)v.findViewById(R.id.account_details);

        accountLayoutName=(TextInputLayout) v.findViewById(R.id.account_layout_name);
        accountLayoutUsername=(TextInputLayout)v.findViewById(R.id.account_layout_username);
        accountLayoutPassword=(TextInputLayout)v.findViewById(R.id.account_layout_password);

        mToolbar = (Toolbar)v. findViewById(R.id.my_toolbar);
        try {
            myActivity.setSupportActionBar(mToolbar);
            myActivity.getSupportActionBar().setTitle("");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getAccountDetails() {

        String account_name,account_username,account_password;
        account_name=accountName.getText().toString().trim();
        account_username=accountUsername.getText().toString().trim();
        account_password=accountPassword.getText().toString().trim();

        if(!TextUtils.isEmpty(account_name)){
            if(!TextUtils.isEmpty(account_username)){
                if(!TextUtils.isEmpty(account_password)){
                    addAccountDetailsToDb(account_name,account_username,account_password);
                }else{
                    accountLayoutPassword.setError(myActivity.getString(R.string.password_error));

                }
            }else{
                accountUsername.setError(myActivity.getString(R.string.username_error));
            }
        }else{
            accountName.setError(myActivity.getString(R.string.account_error));
        }
    }


    private void addAccountDetailsToDb(String account_name, String account_username, String account_password) {

        ContentValues cv=new ContentValues();
        cv.put(ProviderMetadata.accountTableMetaData.accountName,account_name);
        cv.put(ProviderMetadata.accountTableMetaData.accountUsername,account_username);
        cv.put(ProviderMetadata.accountTableMetaData.accountPassword, Crypto.setPassword(account_password,myActivity));
        cv.put(ProviderMetadata.accountTableMetaData.accountNotes,accountDetails.getText().toString().trim());
        myActivity.getContentResolver().insert(ProviderMetadata.accountTableMetaData.CONTENT_URI,cv);
        if (mListener != null) {
            mListener.onFragmentInteraction(myActivity.getString(R.string.success_message));
            getFragmentManager().popBackStack();    //close fragment after adding account
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
        if(myActivity==null){
            myActivity =(MainActivity)activity;
        }
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnDetailFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
         void onFragmentInteraction(String msg);
    }

}
