package passwordholder.bridge.com.passwordholder;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import passwordholder.bridge.com.passwordholder.Utils.BusProvider;
import passwordholder.bridge.com.passwordholder.Utils.PLog;
import passwordholder.bridge.com.passwordholder.model.AccountListItem;
import passwordholder.bridge.com.passwordholder.model.Message;

public class MainActivity extends AppCompatActivity implements AddAccountFragment.OnFragmentInteractionListener
        ,DetailsFragment.OnDetailFragmentInteractionListener ,DeleteConfirmationDialog.OnDialogInteractionListener{

    CoordinatorLayout snackBarLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        snackBarLocation=(CoordinatorLayout) findViewById(R.id.snackbarlocation);
        addAccountListFragment();

    }

    private void addAccountListFragment() {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        AccountListFragment fragment = new AccountListFragment();
        transaction.replace(R.id.fragment_container, fragment,"accountList");
        transaction.commit();
    }

    private void addSettingsFragment() {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        SettingsFragment fragment = new SettingsFragment();
        transaction.replace(R.id.fragment_container, fragment,"settings").addToBackStack("accountList");
        transaction.commit();
    }
    public void addResetPinFragment() {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        PinFragment fragment = new PinFragment();
        transaction.replace(R.id.fragment_container, fragment, "pin").addToBackStack("settings");
        transaction.commit();
    }

   /* public void addAddAccountFragment() {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        AddAccountFragment fragment = new AddAccountFragment();
        transaction.replace(R.id.fragment_container, fragment,"accountList").addToBackStack("AddAccount");
        transaction.setCustomAnimations(R.anim.splashfadeout, R.anim.bottom_up);
        transaction.commit();
        overridePendingTransition(R.anim.splashfadeout, R.anim.bottom_up);
    }*/
    public void addAccountDetailsFragment(AccountListItem mAccount) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        DetailsFragment fragment = new DetailsFragment();
        Bundle b=new Bundle();
        b.putSerializable("item",mAccount);
        fragment.setArguments(b);
        transaction.setCustomAnimations(R.anim.slide_right, R.anim.slide_left);
        transaction.replace(R.id.fragment_container, fragment,"details").addToBackStack("accountList");
        transaction.commit();
        //overridePendingTransition(R.anim.splashfadeout, R.anim.bottom_up);
        // overridePendingTransition(R.anim.splashfadeout, R.anim.bottom_up);
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
        PLog.e("Backstack count getting:" + getSupportFragmentManager().getBackStackEntryCount());
        if(getSupportFragmentManager().getBackStackEntryCount()!=0){

            getSupportFragmentManager().popBackStackImmediate();
        }
        else {
            confirmFinish();
        }

    }
    DialogFragment fragment;
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
        showSnackBar(msg, null, null, -1);
        Message msg1 = new Message(msg);
        BusProvider.postOnMain(msg1);
    }

    @Override
    public void onDetailFragmentInteraction(Uri uri) {

    }

    @Override
    public void onDialogCancel() {

    }

    @Override
    public void onDialogDone() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void showSnackBar(String message,String actionText,View.OnClickListener actionListener,int color){
        Snackbar snackBar=  Snackbar.make(snackBarLocation, message, Snackbar.LENGTH_LONG);
        if(actionText!=null) {
                    snackBar.setAction(actionText, actionListener).show();
        }
        if(color!=-1){
            View snackBarView = snackBar.getView();
            snackBarView.setBackgroundColor(Color.GREEN);

        }
        snackBar.show();
    }
}
