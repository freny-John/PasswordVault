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



import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import passwordholder.bridge.com.passwordholder.Utils.PLog;
import passwordholder.bridge.com.passwordholder.model.AccountListItem;
import passwordholder.bridge.com.passwordholder.uicomponents.RoundedLetterView;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {

    private static final String TAG = "AccountAdapter";
    private ArrayList<AccountListItem> mAccountList;
    Context mContext;
    private int lastPosition = -1;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView_head,accountDetailsText;
        private final LinearLayout container;
        private final RoundedLetterView mRoundedLetterView;

        public ViewHolder(View v) {
            super(v);
            textView_head = (TextView) v.findViewById(R.id.account_name_txt);
            accountDetailsText = (TextView) v.findViewById(R.id.account_details_txt);
            mRoundedLetterView = (RoundedLetterView) v.findViewById(R.id.rlv_name_view);
            container=(LinearLayout) v.findViewById(R.id.container);

        }

        public TextView getTextView_head() {
            return textView_head;
        }
    }

   static  MainActivity myActivity;
    public AccountAdapter(ArrayList<AccountListItem> accountList,Context mContext) {
        mAccountList=accountList;
        this.mContext = mContext;
        myActivity =(MainActivity)mContext;
    }


    int[] rainbow;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        rainbow= mContext.getResources().getIntArray(R.array.colors);
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.account_list_item, viewGroup, false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        PLog.d(TAG + ":Element " + position + " set.");
        AccountListItem currentListItem=mAccountList.get(position);
        viewHolder.getTextView_head().setText(currentListItem.getAccountName());
        viewHolder.accountDetailsText.setText("Date Modified:"+currentListItem.getDate());
//        viewHolder.mRoundedLetterView.setBackgroundColor(mContext.getColor(R.color.primary));
        viewHolder.mRoundedLetterView.setTitleText(String.valueOf(currentListItem.getAccountName().charAt(0)).toUpperCase());

        viewHolder.container.setOnClickListener(v1 -> {

            myActivity.addAccountDetailsFragment(mAccountList.get(position));
        });
        setAnimation(viewHolder.container, position);
    }

    @Override
    public int getItemCount() {
        return mAccountList.size();
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}