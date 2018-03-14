package com.bridge.passwordholder;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.bridge.passwordholder.Utils.PLog;
import com.bridge.passwordholder.model.AccountListItem;

import com.bridge.passwordholder.R;

public class MainActivity extends AppCompatActivity implements com.bridge.passwordholder.AddAccountFragment.OnFragmentInteractionListener
        , com.bridge.passwordholder.DetailsFragment.OnDetailFragmentInteractionListener , com.bridge.passwordholder.DeleteConfirmationDialog.OnDialogInteractionListener{

    private CoordinatorLayout snackBarLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        snackBarLocation=(CoordinatorLayout) findViewById(R.id.snack_bar_location);
        addAccountListFragment();

    }

    private void addAccountListFragment() {
        hideSoftKeyboard();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        AccountListFragment fragment = new AccountListFragment();
        transaction.replace(R.id.fragment_container, fragment, "accountList");
        transaction.commit();
    }

    private void addSettingsFragment() {
        hideSoftKeyboard();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        SettingsFragment fragment = new SettingsFragment();
        transaction.replace(R.id.fragment_container, fragment,"settings").addToBackStack("accountList");
        transaction.commit();
    }
    public void addResetPinFragment() {
        hideSoftKeyboard();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        PinFragment fragment = new PinFragment();
        transaction.replace(R.id.fragment_container, fragment, "pin").addToBackStack("settings");
        transaction.commit();
    }


    public void addAccountDetailsFragment(AccountListItem mAccount) {
        hideSoftKeyboard();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        DetailsFragment fragment = new DetailsFragment();
        Bundle b=new Bundle();
        b.putSerializable("item",mAccount);
        fragment.setArguments(b);
        transaction.setCustomAnimations(R.anim.slide_right, R.anim.slide_left);
        transaction.replace(R.id.fragment_container, fragment,"details").addToBackStack("accountList");
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            addSettingsFragment();
            return false;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        PLog.e("Back stack count getting:" + getSupportFragmentManager().getBackStackEntryCount());
        if(getSupportFragmentManager().getBackStackEntryCount()!=0){

            getSupportFragmentManager().popBackStackImmediate();
        }
        else {
            confirmFinish();
        }

    }
    private DialogFragment fragment;
    private void confirmFinish() {
        fragment = DeleteConfirmationDialog.getDialogInstance(DeleteConfirmationDialog.DialogType.DIALOG_DELETE_CONFIRMATION, getString(R.string.finish_app), getString(R.string.finish_text), MainActivity.this);
        if(!fragment.isInLayout()) {
            try {
                fragment.show(getSupportFragmentManager(), "myDialog");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFragmentInteraction(String msg) {
        showSnackBar(msg, null, null, 1);
    }

    @Override
    public void onDetailFragmentInteraction(Uri uri) {

    }

    @Override
    public void onDialogDone() {
        finish();
    }

    @Override
    public void onDialogCancel() {

    }

    public void showSnackBar(String message,String actionText,View.OnClickListener actionListener,int color){
        Snackbar snackBar=  Snackbar.make(snackBarLocation, message, Snackbar.LENGTH_LONG);
        if(actionText!=null) {
            snackBar.setAction(actionText, actionListener);
        }
        if(color!=-1){
            View snackBarView = snackBar.getView();
            TextView tv = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.GREEN);
        }
        snackBar.show();
    }
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
