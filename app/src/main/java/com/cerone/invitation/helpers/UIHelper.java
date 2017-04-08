package com.cerone.invitation.helpers;

/**
 * Created by adarsht on 10/11/16.
 */

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.cerone.invitation.adapter.AcceptedParticipantsAdapater;


public class UIHelper {
    public static void updateListViewSize(ListView mListView, int margin) {
        ListAdapter mListAdapter = mListView.getAdapter();
        if (mListAdapter == null) {
            // when adapter is null
            return;
        }
        int height = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < mListAdapter.getCount(); i++) {
            View listItem = mListAdapter.getView(i, null, mListView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            height += listItem.getMeasuredHeight() + margin;
        }
        ViewGroup.LayoutParams params = mListView.getLayoutParams();
        params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
        mListView.setLayoutParams(params);
        mListView.requestLayout();
        Log.i("height of listItem:", String.valueOf(height));
    }
    public static void updateRecycleViewSize(RecyclerView recyclerView,View view) {
        AcceptedParticipantsAdapater adapter = (AcceptedParticipantsAdapater)recyclerView.getAdapter();
        if (adapter == null) {
            // when adapter is null
            return;
        }
        int height = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(recyclerView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < adapter.getItemCount(); i++) {
            if(view!=null) {
                View listItem = view;
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }else {
                Log.i("Adarsh", "View null");
            }
        }
        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
        params.height = height +  (adapter.getItemCount() - 1);
        recyclerView.setLayoutParams(params);
        recyclerView.requestLayout();
        Log.i("height of listItem:", String.valueOf(height));
    }

    public static void updateListViewSize(ListView mListView) {
        updateListViewSize(mListView, 30);
    }
}
