package passwordholder.bridge.com.passwordholder;

import android.app.Activity;
import android.content.ContentValues;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.otto.Bus;

import passwordholder.bridge.com.passwordholder.Utils.Crypto;
import passwordholder.bridge.com.passwordholder.Utils.PLog;
import passwordholder.bridge.com.passwordholder.model.AccountListItem;
import passwordholder.bridge.com.passwordholder.model.Reload;
import passwordholder.bridge.com.passwordholder.provider.ProviderMetadata;


public class DetailsFragment extends Fragment implements View.OnClickListener,DeleteConfirmationDialog.OnDialogInteractionListener{

    private OnDetailFragmentInteractionListener mListener;
    AccountListItem currentAccountListItem;
    EditText accountName,accountUsername,accountPassword,accountDetails;
    TextInputLayout accountLayoutName,accountLayoutUsername,accountLayoutPassword,accountLayoutDetails;
    MainActivity myActivity;
    Toolbar mToolbar;
    ImageView deleteBtn,editBtn;
    Bus bus;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentAccountListItem =(AccountListItem) getArguments().getSerializable("item");
        }
        bus=new Bus();
        bus.register(DetailsFragment.this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_details, container, false);
        initUi(v);
        return v;
    }


    private void initUi(View v) {

        accountName=(EditText) v.findViewById(R.id.account_name);
        accountUsername=(EditText)v.findViewById(R.id.account_username);
        accountPassword=(EditText)v.findViewById(R.id.account_password);
        accountDetails=(EditText)v.findViewById(R.id.account_details);

        accountLayoutName=(TextInputLayout) v.findViewById(R.id.account_layout_name);
        accountLayoutUsername=(TextInputLayout)v.findViewById(R.id.account_layout_username);
        accountLayoutPassword=(TextInputLayout)v.findViewById(R.id.account_layout_password);
        deleteBtn= (ImageView) v.findViewById(R.id.btn_delete);
        deleteBtn.setOnClickListener(this);
        editBtn= (ImageView) v.findViewById(R.id.btn_edit);
        editBtn.setOnClickListener(this);

        mToolbar = (Toolbar)v. findViewById(R.id.my_toolbar);
        myActivity.setSupportActionBar(mToolbar);
        myActivity.getSupportActionBar().setTitle("");

        setDetails();


    }

    private void setDetails() {
        if(currentAccountListItem!=null){
            accountName.setText(currentAccountListItem.getAccountName());
            accountUsername.setText(currentAccountListItem.getUsername());
            accountPassword.setText(currentAccountListItem.getPassword());
            accountDetails.setText(currentAccountListItem.getDetails());
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            myActivity=(MainActivity)activity;
            mListener = (OnDetailFragmentInteractionListener) activity;
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_delete:
                ConfirmDeletion(currentAccountListItem.getAccountId(),view);
                break;
            case R.id.btn_edit:
                if(editBtn.getTag().equals(myActivity.getString(R.string.btn_edit))) {
                    editBtn.setImageDrawable(getResources().getDrawable(R.drawable.tick));
                    editBtn.setTag(myActivity.getString(R.string.btn_done));
                    accountName.setEnabled(true);
                    accountUsername.setEnabled(true);
                    accountPassword.setEnabled(true);
                    accountDetails.setEnabled(true);
                }
                else
                {

                    getAccountDetails();

                }
                break;
            default:break;
        }

    }

    private void changeButtonbg(Drawable drawable, String string, boolean enabled) {
        editBtn.setImageDrawable(drawable);
        editBtn.setTag(string);
        accountName.setEnabled(enabled);
        accountUsername.setEnabled(enabled);
        accountPassword.setEnabled(enabled);
        accountDetails.setEnabled(enabled);
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
                    accountLayoutPassword.setError(getActivity().getString(R.string.password_error));

                }
            }else{
                accountUsername.setError(getActivity().getString(R.string.username_error));
            }
        }else{
            accountName.setError(getActivity().getString(R.string.account_error));
        }
    }


    private void addAccountDetailsToDb(String account_name, String account_username, String account_password) {

        ContentValues cv=new ContentValues();
        cv.put(ProviderMetadata.accountTableMetaData.accountName,account_name);
        cv.put(ProviderMetadata.accountTableMetaData.accountUsername,account_username);
        cv.put(ProviderMetadata.accountTableMetaData.accountPassword, Crypto.setPassword(account_password, getActivity()));
        cv.put(ProviderMetadata.accountTableMetaData.accountNotes, accountDetails.getText().toString().trim());
        getActivity().getContentResolver().delete(ProviderMetadata.accountTableMetaData.CONTENT_URI, ProviderMetadata.accountTableMetaData._ID + "=" + currentAccountListItem.getAccountId(), null);
        getActivity().getContentResolver().insert(ProviderMetadata.accountTableMetaData.CONTENT_URI, cv);
        //getActivity().getContentResolver().update(ProviderMetadata.accountTableMetaData.CONTENT_URI, cv, ProviderMetadata.accountTableMetaData.accountId+"="+currentAccountListItem.getAccountId(),null);
        Snackbar snackbar = Snackbar.make(editBtn.getRootView(), "Changes Added Successfully", Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.GREEN);
        snackbar.show();
        changeButtonbg(getResources().getDrawable(R.drawable.edit), myActivity.getString(R.string.btn_edit), false);
    }

    DialogFragment fragment;
    private void ConfirmDeletion(int accountId,View v) {

        fragment = DeleteConfirmationDialog.getDialogInstance(DeleteConfirmationDialog.DialogType.DIALOG_DELETE_CONFIRMATION, myActivity.getString(R.string.dlete_title), myActivity.getString(R.string.delete_text),DetailsFragment.this);
        if(!fragment.isInLayout()) {
            try {
                fragment.show(myActivity.getSupportFragmentManager(), "myDialog");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private int deleteItem(int accountId) {
        String mSelectionClause = ProviderMetadata.accountTableMetaData._ID + " LIKE ?";
        String[] mSelectionArgs = {""+accountId};
        return getActivity().getContentResolver().delete(ProviderMetadata.accountTableMetaData.CONTENT_URI,
                mSelectionClause,mSelectionArgs);
    }

    @Override
    public void onDialogCancel() {

    }

    @Override
    public void onDialogDone() {
        PLog.e("onDone");
        deleteItem(currentAccountListItem.getAccountId());
        myActivity.onBackPressed();
    }

    public interface OnDetailFragmentInteractionListener {
        void onDetailFragmentInteraction(Uri uri);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }




}
