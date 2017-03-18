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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.cerone.invitation.helpers.CircleTransform;
import com.example.dataobjects.Event;
import com.example.dataobjects.Invitee;
import com.ramotion.foldingcell.FoldingCell;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.cerone.invitation.R.id.profileImage;
import static com.google.android.gms.analytics.internal.zzy.g;


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
            viewHolder.participantsLayout =(LinearLayout) cell.findViewById(R.id.participantsLayout);
            cell.setTag(viewHolder);
        } else {
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (ViewHolder) cell.getTag();
        }
        Picasso.with(context).load(event.getOwnerInfo().getImage()).transform(new CircleTransform()).into(viewHolder.eventHeaderIcon);
        Picasso.with(context).load(event.getImageUrl()).into(viewHolder.eventImageFooter);
        Picasso.with(context).load(event.getOwnerInfo().getImage()).transform(new CircleTransform()).into(viewHolder.ownerImage);
        viewHolder.eventNameHeader.setText(event.getName());
        viewHolder.eventNameFooter.setText(event.getName());
        viewHolder.ownerName.setText(event.getOwnerInfo().getInviteeName());
        viewHolder.inviteesCount.setText(""+event.getInviteesCount());
        viewHolder.acceptedCount.setText(""+event.getAcceptedCount());
        viewHolder.rejectedCount.setText(""+event.getRejectedCount());
        viewHolder.participantsLayout.removeAllViews();
        int backgroundColour = (!event.isInvitation()) ? context.getResources().getColor(R.color.green_light) : context.getResources().getColor(R.color.orange);
        viewHolder.colourIndicator.setBackgroundColor(backgroundColour);
        viewHolder.eventNameFooter.setBackgroundColor(backgroundColour);
        for(Invitee invitee:event.getInviteesList()){
            View view = LayoutInflater.from(context).inflate(R.layout.profile_image_layout, parent, false);
            ImageView profileImage = (ImageView) view.findViewById(R.id.profileImage);
            Picasso.with(context).load(invitee.getImage()).transform(new CircleTransform()).into(profileImage);
            viewHolder.participantsLayout.addView(view);
        }
        if(event.getInviteesList().size()==0){
            viewHolder.participantsLayout.setVisibility(View.GONE);
        }else {
            viewHolder.participantsLayout.setVisibility(View.VISIBLE);
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
        notifyDataSetChanged();
    }

    // View lookup cache
    private static class ViewHolder {
        ImageView eventHeaderIcon,eventImageFooter,ownerImage;
        TextView eventNameHeader,colourIndicator,eventAddressHeader,eventDateTimeInfo,eventNameFooter,ownerName,eventStartDate,eventTimings,eventAddressFooter,inviteesCount,acceptedCount,rejectedCount;
        LinearLayout participantsLayout;
    }
}
