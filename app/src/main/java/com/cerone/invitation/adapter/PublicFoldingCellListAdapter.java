package com.cerone.invitation.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.cerone.invitation.helpers.CircleTransform;
import com.cerone.invitation.helpers.FontTypes;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.example.dataobjects.Event;
import com.example.dataobjects.Invitee;
import com.example.utills.StringUtils;
import com.ramotion.foldingcell.FoldingCell;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class PublicFoldingCellListAdapter extends ArrayAdapter<Event> {
    private RecyclerView.LayoutManager mLayoutManager;

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;
    List<Event> events = new ArrayList<Event>();
    Context context;
    FontTypes fontType;


    public PublicFoldingCellListAdapter(Context context, List<Event> allEventsList) {
        super(context, 0, allEventsList);
        this.context = context;
        this.events = allEventsList;
        fontType = new FontTypes(context);
    }

    @Override
    public int getCount() {
        return 5;//events.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get item for selected view
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.public_event_folding_cell, parent, false);
        }
        return cell;
    }


    // simple methods for register cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    public View.OnClickListener getDefaultRequestBtnClickListener() {
        return defaultRequestBtnClickListener;
    }

    public void setDefaultRequestBtnClickListener(View.OnClickListener defaultRequestBtnClickListener) {
        this.defaultRequestBtnClickListener = defaultRequestBtnClickListener;
    }

    public void updateList(List<Event> allEventsList) {
        this.events = allEventsList;
        unfoldedIndexes = new HashSet<>();
        notifyDataSetChanged();
    }

    public View.OnClickListener createOnClickListener(final int position, final ViewGroup parent) {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((ListView) parent).performItemClick(v, position, 0);
            }
        };
    }

    // View lookup cache
    private static class ViewHolder {


    }
}
