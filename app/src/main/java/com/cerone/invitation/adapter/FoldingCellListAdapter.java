package com.cerone.invitation.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.example.dataobjects.Event;
import com.ramotion.foldingcell.FoldingCell;

import java.util.HashSet;
import java.util.List;

import static com.google.android.gms.analytics.internal.zzy.v;


public class FoldingCellListAdapter extends ArrayAdapter<Event> {
    private RecyclerView.LayoutManager mLayoutManager;

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;
    Context context;


    public FoldingCellListAdapter(Context context, List<Event> objects) {
        super(context, 0, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get item for selected view
        Event item = getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.event_folding_child, parent, false);
            // binding view parts to view holder

            cell.setTag(viewHolder);
        } else {
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (ViewHolder) cell.getTag();
        }
        viewHolder.participantsListView = (RecyclerView) cell.findViewById(R.id.participantsList);
        viewHolder.participantsListView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        viewHolder.participantsListView.setAdapter(new ParticipantsImageAdapter(context));

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

    // View lookup cache
    private static class ViewHolder {
        RecyclerView participantsListView;
//        TextView contentRequestBtn;
//        TextView pledgePrice;
//        TextView fromAddress;
//        TextView toAddress;
//        TextView requestsCount;
//        TextView date;
//        TextView time;
    }
}
