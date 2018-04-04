package com.bridge.passwordholder;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.andexert.library.RippleView;
import com.bridge.passwordholder.Utils.Crypto;
import com.bridge.passwordholder.model.AccountListItem;
import com.bridge.passwordholder.provider.ProviderMetadata;
import com.bridge.passwordholder.uicomponents.RoundedLetterView;

import com.bridge.passwordholder.R;

/**
 * Show the details of the account
 * */
public class DetailsFragment extends Fragment implements RippleView.OnRippleCompleteListener,DeleteConfirmationDialog.OnDialogInteractionListener,AddAccountFragment.OnEditSuccessfulListener {

    private OnDetailFragmentInteractionListener mListener;
    private AccountListItem currentAccountListItem;
    private TextView accountName;
    private TextView accountUsername;
    private TextView accountPassword;
    private TextView accountDetails;
    private TextView date;
    private MainActivity myActivity;
    private Toolbar mToolbar;
    private ImageView editBtn;
    private RoundedLetterView RoundedLetterView;
    private ViewSwitcher eyeViewSwitcher;
    RippleView back_button_ripple,deleteButtonRipple,editButtonRipple;
    static AddAccountFragment.OnEditSuccessfulListener mOnEditSuccessfulListener;

    private ImageButton btn_email_copy,btn_password_copy;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentAccountListItem =(AccountListItem) getArguments().getSerializable("item");
        }
        mOnEditSuccessfulListener=DetailsFragment.this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_details, container, false);
        initUi(v);




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

        initUiComponents(v);

        setUpUi();

        setDetails(currentAccountListItem);

        setUpSwitcher();

    }

    private void setUpSwitcher() {
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

    private void setUpUi() {
        deleteButtonRipple.setOnRippleCompleteListener(this);
        editButtonRipple.setOnRippleCompleteListener(this);
        back_button_ripple.setRippleDuration(150);
        back_button_ripple.setOnRippleCompleteListener(this);

        viewZoomIn(accountName);

        myActivity.setSupportActionBar(mToolbar);
        try {
            myActivity.getSupportActionBar().setTitle("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initUiComponents(View v) {

        btn_email_copy= (ImageButton) v.findViewById(R.id.button_add_email_clipboard);
        btn_password_copy= (ImageButton) v.findViewById(R.id.button_add_pass_clipboard);


        accountName=(TextView) v.findViewById(R.id.account_name);
        accountUsername=(TextView)v.findViewById(R.id.account_username);
        accountPassword=(TextView)v.findViewById(R.id.account_password);
        accountDetails=(TextView)v.findViewById(R.id.account_details);
        date=(TextView)v.findViewById(R.id.date);
        accountPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        RoundedLetterView=(RoundedLetterView) v.findViewById(R.id.detailLetterView);
        eyeViewSwitcher =(ViewSwitcher) v.findViewById(R.id.eye_view_switcher);
        editBtn= (ImageView) v.findViewById(R.id.btn_edit);

        back_button_ripple= (RippleView) v.findViewById(R.id.back_button_ripple);
        deleteButtonRipple=(RippleView) v.findViewById(R.id.delete_button_ripple);
        editButtonRipple=(RippleView) v.findViewById(R.id.edit_button_ripple);
        mToolbar = (Toolbar)v. findViewById(R.id.my_toolbar);
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
    private void viewZoomIn(View v) {

        Animation zoomIn = AnimationUtils.loadAnimation(myActivity, R.anim.zoom_in);
        v.startAnimation(zoomIn);
        v.setVisibility(View.VISIBLE);
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



    private void changeButtonBg(Drawable drawable, String string, boolean enabled) {
        editBtn.setImageDrawable(drawable);
        editBtn.setTag(string);
        accountName.setEnabled(enabled);
        accountUsername.setEnabled(enabled);
        accountPassword.setEnabled(enabled);
        accountDetails.setEnabled(enabled);
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

    private DialogFragment fragment;
    private void ConfirmDeletion(int accountId,View v) {

        fragment = DeleteConfirmationDialog.getDialogInstance(DeleteConfirmationDialog.DialogType.DIALOG_DELETE_CONFIRMATION,
                myActivity.getString(R.string.delete_title), myActivity.getString(R.string.delete_text)+currentAccountListItem.getAccountName()+myActivity.getString(R.string.delete_item),DetailsFragment.this);
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
    public void onDialogDone() {
        myActivity.hideSoftKeyboard();
        deleteItem(currentAccountListItem.getAccountId());
        myActivity.onBackPressed();
    }

    @Override
    public void onDialogCancel() {
        myActivity.hideSoftKeyboard();
    }

    @Override
    public void onEditSuccess(AccountListItem mAccountListItem) {
        setDetails(mAccountListItem);
    }

    @Override
    public void onComplete(RippleView rippleView) {
        switch (rippleView.getId()){
            case R.id.delete_button_ripple:
                ConfirmDeletion(currentAccountListItem.getAccountId(),rippleView);
                break;
            case R.id.edit_button_ripple:
                callEditFragment();
                break;
            case R.id.back_button_ripple:
                myActivity.hideSoftKeyboard();
                myActivity.onBackPressed();
                break;
            default:break;
        }
    }

    private void callEditFragment() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment editFragment=new AddAccountFragment();
        Bundle b=new Bundle();
        b.putString("from","edit");
        b.putSerializable("accountItem", currentAccountListItem);
        editFragment.setArguments(b);
        transaction.replace(R.id.fragment_container,editFragment,"edit").addToBackStack("details");
        transaction.commit();
    }

    public interface OnDetailFragmentInteractionListener {
        void onDetailFragmentInteraction(Uri uri);
    }


}
