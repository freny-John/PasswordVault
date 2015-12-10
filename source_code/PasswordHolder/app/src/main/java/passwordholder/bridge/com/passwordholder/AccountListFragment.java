package passwordholder.bridge.com.passwordholder;
/*
* Copyright (C) 2014 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import passwordholder.bridge.com.passwordholder.Utils.Crypto;
import passwordholder.bridge.com.passwordholder.Utils.PLog;
import passwordholder.bridge.com.passwordholder.model.AccountListItem;
import passwordholder.bridge.com.passwordholder.model.Message;
import passwordholder.bridge.com.passwordholder.provider.ProviderMetadata;

/**
 * Created by Anu on 11/26/2015.
 */

public class AccountListFragment extends Fragment  implements LoaderManager.LoaderCallbacks<Cursor>,View.OnClickListener,OnResumeFragmentInterface{

    private static final String TAG = "AccountListFragment";
    protected RecyclerView mRecyclerView;
    protected FloatingActionButton mAdd;
    protected RelativeLayout mListContainer;
    protected AccountAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected ArrayList<AccountListItem> mAccountList;
    Loader<Cursor> accountLoader;
    MainActivity myActivity;
    Bus bus;
    Toolbar mToolbar;
    SearchView searchView;
    TextView noItems;
    boolean firstTime=true;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initDataset();
        bus = new Bus();
        bus.register(this);
        mAccountList=new ArrayList<>() ;
        accountLoader=getLoaderManager().restartLoader(1, null, this);
        setHasOptionsMenu(true);
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        myActivity.getMenuInflater().inflate(R.menu.menu_account_list, menu);
        SearchManager searchManager = (SearchManager)myActivity. getSystemService(Context.SEARCH_SERVICE);
        searchView= (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(myActivity.getComponentName()));
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                doSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText) || (!firstTime)) {
                    firstTime = false;
                    doSearch(newText);
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }


    private void doSearch(String query) {
        Cursor c;
        if(!TextUtils.isEmpty(query)) {
            String[] selectionArgs={"%"+query+"%"};
            c = myActivity.getContentResolver().query(ProviderMetadata.accountTableMetaData.CONTENT_URI, null, ProviderMetadata.accountTableMetaData.accountName + " LIKE ?", selectionArgs, null);

        }
        else
        {
            c = myActivity.getContentResolver().query(ProviderMetadata.accountTableMetaData.CONTENT_URI, null, null, null, null);
        }
        PLog.e("cursor length getting:" + c.getCount());
        loadDataToList(c);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_account_list, container, false);
        rootView.setTag(TAG);
        mListContainer = (RelativeLayout) rootView.findViewById(R.id.account_list_container);
        mAdd = (FloatingActionButton) rootView.findViewById(R.id.myFAB);
        mAdd.setOnClickListener(this);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(myActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        noItems=(TextView) rootView.findViewById(R.id.noItems);
        mToolbar = (Toolbar)rootView. findViewById(R.id.my_toolbar);

        myActivity.setSupportActionBar(mToolbar);
        myActivity.getSupportActionBar().setTitle(" ");
        myActivity.getSupportActionBar().setDisplayShowHomeEnabled(true); // show or hide the default home button
        myActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myActivity.getSupportActionBar().setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        myActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        myActivity.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }



    public void showDomainUnAuthorisedView() {

        Fragment newFragment = new AddAccountFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_right, R.anim.slide_left);
        //transaction.setCustomAnimations(R.anim.bottom_up, R.anim.splashfadeout);
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    /***Loader/
     *
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = ProviderMetadata.accountTableMetaData.CONTENT_URI;
        return new CursorLoader(myActivity, uri, null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor mCursor) {
        loadDataToList(mCursor);
    }

    private void loadDataToList(Cursor mCursor) {
        if(mCursor.getCount()!=0){
            noItems.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            PLog.e("fav cursor length:" + mCursor.getCount());
            mAccountList.clear();
            mCursor.moveToFirst();
            while(!mCursor.isAfterLast()) {
                AccountListItem mAccountListItem=new AccountListItem();
                mAccountListItem.setAccountId(mCursor.getInt(mCursor.getColumnIndex(ProviderMetadata.accountTableMetaData._ID)));
                mAccountListItem.setAccountName(mCursor.getString(mCursor.getColumnIndex(ProviderMetadata.accountTableMetaData.accountName)));
                mAccountListItem.setUsername(mCursor.getString(mCursor.getColumnIndex(ProviderMetadata.accountTableMetaData.accountUsername)));
                mAccountListItem.setPassword(Crypto.getPassword(mCursor.getString(mCursor.getColumnIndex(ProviderMetadata.accountTableMetaData.accountPassword)), myActivity));
                mAccountListItem.setDetails(mCursor.getString(mCursor.getColumnIndex(ProviderMetadata.accountTableMetaData.accountNotes)));
                mAccountList.add(mAccountListItem); //add the item
                mCursor.moveToNext();
            }
            mAdapter = new AccountAdapter(mAccountList,myActivity);
            mRecyclerView.setAdapter(mAdapter);

        }
        else{
            noItems.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Subscribe
    public void showSnackbar(Message mMessage) {
        Snackbar.make(mListContainer.getRootView(), mMessage.getMessage(),Snackbar.LENGTH_SHORT).show();

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.myFAB:
                showDomainUnAuthorisedView();
                break;
            default:break;
        }
    }

    @Override
    public void onAttach(Context context) {
        if(myActivity==null){
            myActivity =(MainActivity)context;
            firstTime=true;
        }
        super.onAttach(context);
    }

    @Override
    public void onAttach(Activity activity) {
        if(myActivity==null){
            myActivity =(MainActivity)activity;
        }
        super.onAttach(activity);
    }


    @Override
    public void onDestroy() {
        bus.unregister(this);
        super.onDestroy();
    }

    @Override
    public void onResumeFragment() {
        PLog.e("onresume fragment");
    }
}
