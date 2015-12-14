package passwordholder.bridge.com.passwordholder;

import android.app.Activity;
import android.support.v4.app.FragmentTransaction;
import android.content.ContentValues;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.squareup.otto.Bus;

import passwordholder.bridge.com.passwordholder.Utils.Crypto;
import passwordholder.bridge.com.passwordholder.model.AccountListItem;
import passwordholder.bridge.com.passwordholder.provider.ProviderMetadata;
import passwordholder.bridge.com.passwordholder.uicomponents.RoundedLetterView;


public class DetailsFragment extends Fragment implements View.OnClickListener,DeleteConfirmationDialog.OnDialogInteractionListener,AddAccountFragment.OnEditSuccessfulListener {

    private OnDetailFragmentInteractionListener mListener;
    AccountListItem currentAccountListItem;
    TextView accountName,accountUsername,accountPassword,accountDetails,date;
    TextInputLayout accountLayoutPassword;
    MainActivity myActivity;
    Toolbar mToolbar;
    ImageView deleteBtn,editBtn,back;
    Bus bus;
    RoundedLetterView RoundedLetterView;
    ViewSwitcher eyeViewSwitcher;
static AddAccountFragment.OnEditSuccessfulListener mOnEditSuccessfulListener;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentAccountListItem =(AccountListItem) getArguments().getSerializable("item");
        }
        bus=new Bus();
        bus.register(DetailsFragment.this);
        mOnEditSuccessfulListener=DetailsFragment.this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_details, container, false);
        initUi(v);
        return v;
    }


    private void initUi(View v) {

        accountName=(TextView) v.findViewById(R.id.account_name);
        accountUsername=(TextView)v.findViewById(R.id.account_username);
        accountPassword=(TextView)v.findViewById(R.id.account_password);
        accountDetails=(TextView)v.findViewById(R.id.account_details);
        date=(TextView)v.findViewById(R.id.date);
        accountPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        RoundedLetterView=(RoundedLetterView) v.findViewById(R.id.detailLetterView);
        eyeViewSwitcher =(ViewSwitcher) v.findViewById(R.id.eye_view_switcher);
        deleteBtn= (ImageView) v.findViewById(R.id.btn_delete);
        back= (ImageView) v.findViewById(R.id.back);
        deleteBtn.setOnClickListener(this);
        editBtn= (ImageView) v.findViewById(R.id.btn_edit);
        editBtn.setOnClickListener(this);

        mToolbar = (Toolbar)v. findViewById(R.id.my_toolbar);
        myActivity.setSupportActionBar(mToolbar);
        myActivity.getSupportActionBar().setTitle("");

        setDetails(currentAccountListItem);
        back.setOnClickListener(view -> {
            myActivity.onBackPressed();
        });

        eyeViewSwitcher.setOnClickListener(view -> {
            if(eyeViewSwitcher.getTag().equals("0"))
            {
                accountPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                eyeViewSwitcher.setTag("1");
            }
            else
            {
                accountPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                eyeViewSwitcher.setTag("0");

            }
            eyeViewSwitcher.showNext();
        });

    }

    private void setDetails(AccountListItem currentAccountListItem) {
        if(currentAccountListItem!=null){
            accountName.setText(currentAccountListItem.getAccountName());
            accountUsername.setText(currentAccountListItem.getUsername());
            accountPassword.setText(currentAccountListItem.getPassword());
            accountDetails.setText(currentAccountListItem.getDetails());
            date.setText(myActivity.getString(R.string.last_modified)+currentAccountListItem.getDate());
            RoundedLetterView.setTitleText(String.valueOf(currentAccountListItem.getAccountName().charAt(0)).toUpperCase());
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
                   /* editBtn.setImageDrawable(getResources().getDrawable(R.drawable.tick));
                    editBtn.setTag(myActivity.getString(R.string.btn_done));
                    accountName.setEnabled(true);
                    accountUsername.setEnabled(true);
                    accountPassword.setEnabled(true);
                    accountDetails.setEnabled(true);*/
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();

                   // FragmentTransaction transaction=getChildFragmentManager().beginTransaction();
                    Fragment editFragment=new AddAccountFragment();
                    Bundle b=new Bundle();
                    b.putString("from","edit");
                    b.putSerializable("accountItem", currentAccountListItem);
                    editFragment.setArguments(b);
                    transaction.replace(R.id.fragment_container,editFragment,"edit").addToBackStack("details");
                    transaction.commit();


                }
                else
                {

                    getAccountDetails();

                }
                break;
            default:break;
        }

    }

    private void changeButtonBg(Drawable drawable, String string, boolean enabled) {
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
        cv.put(ProviderMetadata.accountTableMetaData.accountPassword, Crypto.setPassword(account_password, myActivity));
        cv.put(ProviderMetadata.accountTableMetaData.accountNotes, accountDetails.getText().toString().trim());
        myActivity.getContentResolver().delete(ProviderMetadata.accountTableMetaData.CONTENT_URI, ProviderMetadata.accountTableMetaData._ID + "=" + currentAccountListItem.getAccountId(), null);
        myActivity.getContentResolver().insert(ProviderMetadata.accountTableMetaData.CONTENT_URI, cv);
        myActivity.showSnackBar(myActivity.getString(R.string.changes_added_successfully),null,null,Color.GREEN);
        changeButtonBg(getResources().getDrawable(R.drawable.edit), myActivity.getString(R.string.btn_edit), false);
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
        return myActivity.getContentResolver().delete(ProviderMetadata.accountTableMetaData.CONTENT_URI,
                mSelectionClause,mSelectionArgs);
    }

    @Override
    public void onDialogCancel() {

    }

    @Override
    public void onDialogDone() {
        deleteItem(currentAccountListItem.getAccountId());
        myActivity.onBackPressed();
    }

    @Override
    public void onEditSuccess(AccountListItem mAccountListItem) {
        setDetails(mAccountListItem);
    }

    public interface OnDetailFragmentInteractionListener {
        void onDetailFragmentInteraction(Uri uri);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }


}
