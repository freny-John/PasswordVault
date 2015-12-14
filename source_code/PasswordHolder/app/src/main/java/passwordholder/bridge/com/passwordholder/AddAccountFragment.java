package passwordholder.bridge.com.passwordholder;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.squareup.otto.Bus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import passwordholder.bridge.com.passwordholder.Utils.BusProvider;
import passwordholder.bridge.com.passwordholder.Utils.Crypto;
import passwordholder.bridge.com.passwordholder.Utils.PLog;
import passwordholder.bridge.com.passwordholder.model.AccountListItem;
import passwordholder.bridge.com.passwordholder.model.Message;
import passwordholder.bridge.com.passwordholder.provider.ProviderMetadata;


public class AddAccountFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Button btn_add;
    EditText accountName,accountUsername,accountPassword,accountDetails;
    //TextInputLayout accountLayoutName,accountLayoutUsername,accountLayoutPassword,accountLayoutDetails;
    MainActivity myActivity;
    Toolbar mToolbar;
    String from;
    AccountListItem mAccountListItem;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_add_account, container, false);

        Bundle b=getArguments();
        if(b!=null) {
            from = b.getString("from");
            PLog.e("add fragment is calling from "+from);
            if (from.equals("edit")) {
                mAccountListItem = (AccountListItem) b.getSerializable("accountItem");
                PLog.e("add fragment is calling mAccountListItem "+mAccountListItem.toString());
            }
        }
        initUi(v);
        btn_add.setOnClickListener(view -> {
            getAccountDetails();
        });
        return v;
    }

    private void initUi(View v) {

        btn_add= (Button) v.findViewById(R.id.btn_add);
        accountName=(EditText) v.findViewById(R.id.account_name);
        accountUsername=(EditText)v.findViewById(R.id.account_username);
        accountPassword=(EditText)v.findViewById(R.id.account_password);
        accountDetails=(EditText)v.findViewById(R.id.account_details);

        initData();
       /* accountLayoutName=(TextInputLayout) v.findViewById(R.id.account_layout_name);
        accountLayoutUsername=(TextInputLayout)v.findViewById(R.id.account_layout_username);
        accountLayoutPassword=(TextInputLayout)v.findViewById(R.id.account_layout_password);*/

        mToolbar = (Toolbar)v. findViewById(R.id.my_toolbar);
        try {
            myActivity.setSupportActionBar(mToolbar);
            myActivity.getSupportActionBar().setTitle("");
        } catch (Exception e) {
            e.printStackTrace();
        }


        accountPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (accountPassword.getRight() - accountPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        if(accountPassword.getTag().equals("0")){
                            accountPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            accountPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_not_hidden, 0);
                            accountPassword.setTag("1");
                        }
                        else
                        {
                            accountPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            accountPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_hidden, 0);
                            accountPassword.setTag("0");
                        }

                        return false;
                    }
                }
                return false;
            }
        });
    }

    private void initData() {
        if(from.equals("edit")){
            accountName.setText(mAccountListItem.getAccountName());
            accountUsername.setText(mAccountListItem.getUsername());
            accountPassword.setText(mAccountListItem.getPassword());
            accountDetails.setText(mAccountListItem.getDetails());
            btn_add.setText("Edit Account");
        }
        else
        {
            btn_add.setText("add Account");
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
                    // accountLayoutPassword.setError(myActivity.getString(R.string.password_error));

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
        cv.put(ProviderMetadata.accountTableMetaData.accountPassword, Crypto.setPassword(account_password, myActivity));
        cv.put(ProviderMetadata.accountTableMetaData.accountNotes,accountDetails.getText().toString().trim());
        cv.put(ProviderMetadata.accountTableMetaData.timeStamp, getDateTime());
        if(from.equals("add")) {
            myActivity.getContentResolver().insert(ProviderMetadata.accountTableMetaData.CONTENT_URI, cv);
        }
        else
        {
            myActivity.getContentResolver().update(ProviderMetadata.accountTableMetaData.CONTENT_URI, cv, ProviderMetadata.accountTableMetaData._ID + " = " + mAccountListItem.getAccountId(), null);
            mAccountListItem.setAccountName(account_name);
            mAccountListItem.setDate(getDateTime());
            mAccountListItem.setUsername(account_username);
            mAccountListItem.setPassword(account_password);
            mAccountListItem.setDetails(accountDetails.getText().toString().trim());
            DetailsFragment.mOnEditSuccessfulListener.onEditSuccess(mAccountListItem);
        }
        if (mListener != null) {
            mListener.onFragmentInteraction(myActivity.getString(R.string.success_message));
            getFragmentManager().popBackStack();    //close fragment after adding account
        }
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
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
    public interface OnEditSuccessfulListener {
        void onEditSuccess(AccountListItem mAccountListItem);
    }
}
