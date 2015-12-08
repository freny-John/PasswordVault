package passwordholder.bridge.com.passwordholder;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import passwordholder.bridge.com.passwordholder.Utils.PLog;
import passwordholder.bridge.com.passwordholder.model.AccountListItem;
import passwordholder.bridge.com.passwordholder.model.Message;

public class MainActivity extends AppCompatActivity implements AddAccountFragment.OnFragmentInteractionListener
        ,DetailsFragment.OnDetailFragmentInteractionListener ,DeleteConfirmationDialog.OnDialogInteractionListener{

    Bus bus;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bus = new Bus();
        bus.register(this);
        addAccountListFragment();

    }

    private void addAccountListFragment() {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        AccountListFragment fragment = new AccountListFragment();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    public void addAddAccountFragment() {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        AddAccountFragment fragment = new AddAccountFragment();
        transaction.replace(R.id.fragment_container, fragment).addToBackStack("AccountListFragment");
        transaction.setCustomAnimations(R.anim.splashfadeout, R.anim.bottom_up);
        transaction.commit();
        overridePendingTransition(R.anim.splashfadeout, R.anim.bottom_up);
    }
    public void addAccountDetailsFragment(AccountListItem mAccount) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        DetailsFragment fragment = new DetailsFragment();
        Bundle b=new Bundle();
        b.putSerializable("item",mAccount);
        fragment.setArguments(b);
        transaction.replace(R.id.fragment_container, fragment).addToBackStack("AccountListFragment");
        transaction.setCustomAnimations(R.anim.splashfadeout, R.anim.bottom_up);
        transaction.commit();
        overridePendingTransition(R.anim.splashfadeout, R.anim.bottom_up);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        PLog.e("Backstack count getting:" + getSupportFragmentManager().getBackStackEntryCount());
        if(getSupportFragmentManager().getBackStackEntryCount()!=0){

            getSupportFragmentManager().popBackStackImmediate();
        }
        else {
            //TODO show do you want to finish app
            finish();
        }

    }

    @Override
    public void onFragmentInteraction(String msg) {
        PLog.e("onFragmentInteraction");
        Snackbar.make(findViewById(R.id.fragment_container),msg,Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onDetailFragmentInteraction(Uri uri) {

    }

    @Override
    public void onDialogCancel() {

    }

    @Override
    public void onDialogDone() {
    }


    public interface onMessageListener{
        void onMessageReceived(String message);
    }

    @Subscribe
    public void getMessage(Message msg){
        PLog.e("getMessage");
        bus.post(new Message(getString(R.string.success_message)));


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
    }
}
