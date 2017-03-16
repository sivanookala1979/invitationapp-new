package com.cerone.invitation.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.cerone.invitation.helpers.CircleTransform;
import com.example.dataobjects.Event;
import com.ramotion.foldingcell.FoldingCell;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class FoldingCellListAdapter extends ArrayAdapter<Event> {
    private RecyclerView.LayoutManager mLayoutManager;

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;
    List<Event> events = new ArrayList<Event>();
    Context context;


    public FoldingCellListAdapter(Context context, List<Event> allEventsList) {
        super(context, 0, allEventsList);
        this.context = context;
        this.events = allEventsList;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Nullable
    @Override
    public Event getItem(int position) {
        return events.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get item for selected view
        Event event = getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.event_folding_child, parent, false);
            // binding view parts to view holder
            viewHolder.participantsListView = (RecyclerView) cell.findViewById(R.id.participantsList);
            viewHolder.eventHeaderIcon = (ImageView)cell.findViewById(R.id.eventIconHeader);
            viewHolder.eventNameHeader = (TextView)cell.findViewById(R.id.eventNameHeader);
            viewHolder.eventAddressHeader = (TextView)cell.findViewById(R.id.eventAddressHeader);
            viewHolder.eventDateTimeInfo = (TextView)cell.findViewById(R.id.eventTimingsHeading);
            viewHolder.colourIndicator = (TextView)cell.findViewById(R.id.colorIndicator);
            //FOOTER VIEW DETAILS
            viewHolder.eventNameFooter = (TextView)cell.findViewById(R.id.eventNameFooter);
            viewHolder.eventImageFooter = (ImageView)cell.findViewById(R.id.eventImageFooter);
            viewHolder.ownerImage = (ImageView)cell.findViewById(R.id.eventOwnerImage);
            viewHolder.ownerName = (TextView)cell.findViewById(R.id.ownerName);
            viewHolder.eventStartDate = (TextView)cell.findViewById(R.id.eventDate);
            viewHolder.eventTimings = (TextView)cell.findViewById(R.id.eventTimings);
            viewHolder.eventAddressFooter = (TextView)cell.findViewById(R.id.eventAddressFooter);
            viewHolder.inviteesCount = (TextView)cell.findViewById(R.id.totalInvitees);
            viewHolder.acceptedCount = (TextView)cell.findViewById(R.id.totalAccepted);
            viewHolder.rejectedCount = (TextView)cell.findViewById(R.id.totalRejected);
            cell.setTag(viewHolder);
        } else {
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (ViewHolder) cell.getTag();
        }
        viewHolder.participantsListView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        viewHolder.participantsListView.setAdapter(new ParticipantsImageAdapter(context));
        Picasso.with(context).load(event.getOwnerInfo().getImage()).transform(new CircleTransform()).into(viewHolder.eventHeaderIcon);
        Picasso.with(context).load(event.getImageUrl()).into(viewHolder.eventImageFooter);
        Picasso.with(context).load(event.getOwnerInfo().getImage()).transform(new CircleTransform()).into(viewHolder.ownerImage);
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
        notifyDataSetChanged();
    }

    // View lookup cache
    private static class ViewHolder {
        RecyclerView participantsListView;
        ImageView eventHeaderIcon,eventImageFooter,ownerImage;
        TextView eventNameHeader,colourIndicator,eventAddressHeader,eventDateTimeInfo,eventNameFooter,ownerName,eventStartDate,eventTimings,eventAddressFooter,inviteesCount,acceptedCount,rejectedCount;
    }
}
