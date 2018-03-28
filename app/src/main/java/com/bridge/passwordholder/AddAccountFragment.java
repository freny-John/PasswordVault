package com.bridge.passwordholder;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.andexert.library.RippleView;
import com.bridge.passwordholder.Utils.Crypto;
import com.bridge.passwordholder.model.AccountListItem;
import com.bridge.passwordholder.provider.ProviderMetadata;


public class AddAccountFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private RippleView btn_add;
    private ImageButton btn_email_copy,btn_password_copy;
    private EditText accountName;
    private EditText accountUsername;
    private EditText accountPassword;
    private EditText accountDetails;
    TextInputLayout accountLayoutName,accountLayoutUsername,accountLayoutPassword;
    private MainActivity myActivity;
    private Toolbar mToolbar;
    private String from;
    private AccountListItem mAccountListItem;
    TextView toolbarTitle;
    ImageView back;
    String account_name,account_username,account_password;
    RippleView back_button_ripple;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_add_account, container, false);

        Bundle b=getArguments();
        if(b!=null) {
            from = b.getString("from");
            if (from.equals("edit")) {
                mAccountListItem = (AccountListItem) b.getSerializable("accountItem");
            }
        }
        initUi(v);
       // btn_add.setOnRippleCompleteListener(rippleView -> submitForm());

        btn_add.setOnRippleCompleteListener(rippleView -> {

      submitForm();

            Toast.makeText(getActivity(),"Submit",Toast.LENGTH_LONG).show();
        });


        btn_email_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager cManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData cData = ClipData.newPlainText("text", accountUsername.getText().toString());
                cManager.setPrimaryClip(cData);

                Toast.makeText(getActivity(),"Copied to clipboard",Toast.LENGTH_SHORT).show();
            }
        });


        btn_password_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager cManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData cData = ClipData.newPlainText("text", accountPassword.getText().toString());
                cManager.setPrimaryClip(cData);
                Toast.makeText(getActivity(),"Copied to clipboard",Toast.LENGTH_SHORT).show();
            }
        });


        return v;
    }

    private void initUi(View v) {

        btn_add= (RippleView) v.findViewById(R.id.btn_add);
        btn_email_copy= (ImageButton) v.findViewById(R.id.button_add_email_clipboard);
        btn_password_copy= (ImageButton) v.findViewById(R.id.button_add_pass_clipboard);

        accountName=(EditText) v.findViewById(R.id.account_name);
        accountUsername=(EditText)v.findViewById(R.id.account_username);
        accountPassword=(EditText)v.findViewById(R.id.account_password);
        accountDetails=(EditText)v.findViewById(R.id.account_details);
        toolbarTitle=(TextView) v.findViewById(R.id.toolbar_title);
        accountLayoutName=(TextInputLayout) v.findViewById(R.id.account_layout_name);
        accountLayoutUsername=(TextInputLayout) v.findViewById(R.id.account_layout_username);
        accountLayoutPassword=(TextInputLayout) v.findViewById(R.id.account_layout_password);
        Typeface tf = tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/Optima.ttf");
        accountLayoutPassword.setTypeface(tf);
        back_button_ripple= (RippleView) v.findViewById(R.id.back_button_ripple);
        back=(ImageView) v.findViewById(R.id.back);

        initData();
        back_button_ripple.setRippleDuration(150);
        back_button_ripple.setOnRippleCompleteListener(rippleView -> {
            myActivity.hideSoftKeyboard();
            myActivity.onBackPressed();
        });

        mToolbar = (Toolbar)v. findViewById(R.id.my_toolbar_add);
        try {
            myActivity.setSupportActionBar(mToolbar);
            myActivity.getSupportActionBar().setTitle("");
        } catch (Exception e) {
            e.printStackTrace();
        }
        accountName.addTextChangedListener(new MyTextWatcher(accountName));
        accountUsername.addTextChangedListener(new MyTextWatcher(accountUsername));
        accountPassword.addTextChangedListener(new MyTextWatcher(accountPassword));

    }

    private void initData() {
        if(from.equals("edit")){
            accountName.setText(mAccountListItem.getAccountName());
            accountUsername.setText(mAccountListItem.getUsername());
            accountPassword.setText(mAccountListItem.getPassword());
            accountDetails.setText(mAccountListItem.getDetails());
            toolbarTitle.setText("Edit Account");
        }
        else
        {
            toolbarTitle.setText("Add Account");
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
        { //from edit
            myActivity.getContentResolver().update(ProviderMetadata.accountTableMetaData.CONTENT_URI, cv, ProviderMetadata.accountTableMetaData._ID + " = " + mAccountListItem.getAccountId(), null);
            mAccountListItem.setAccountName(account_name);
            mAccountListItem.setDate(getDateTime());
            mAccountListItem.setUsername(account_username);
            mAccountListItem.setPassword(account_password);
            mAccountListItem.setDetails(accountDetails.getText().toString().trim());
            DetailsFragment.mOnEditSuccessfulListener.onEditSuccess(mAccountListItem);
        }
        showMessage();
    }

    private void showMessage() {
        if (mListener != null) {
            myActivity.hideSoftKeyboard();
            if (from.equals("edit")){
                mListener.onFragmentInteraction(myActivity.getString(R.string.success_message_edit));
            }
            else
            {
                mListener.onFragmentInteraction(myActivity.getString(R.string.success_message));

            }
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
    ////////////////validation
    /**
     * Validating form
     */
    private void submitForm() {

        if (!validateAccountType()) {
            return;
        }
        if (!validateUserName()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }
        addAccountDetailsToDb(account_name,account_username,account_password);
    }

    private boolean validateUserName() {

        account_username=accountUsername.getText().toString().trim();
        if (TextUtils.isEmpty(account_username)) {
            accountLayoutUsername.setError(myActivity.getString(R.string.username_error));
            requestFocus(accountUsername);
            return false;
        } else {
            accountLayoutUsername.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateAccountType() {
        account_name=accountName.getText().toString().trim();
        if (TextUtils.isEmpty(account_name)) {
            accountLayoutName.setError(myActivity.getString(R.string.account_error));
            requestFocus(accountName);
            return false;
        } else {
            accountLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
       account_password=accountPassword.getText().toString().trim();
        if (TextUtils.isEmpty(account_password)) {
            accountLayoutPassword.setError(getString(R.string.password_error));
           accountPassword.requestFocus();
            return false;
        } else {
            accountLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            myActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.account_username:
                    validateUserName();
                    break;
                case R.id.account_name:
                    validateAccountType();
                    break;
                case R.id.account_password:
                    validatePassword();
                    break;
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_login, menu);
    }
}
