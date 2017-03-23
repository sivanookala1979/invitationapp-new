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
import com.example.dataobjects.Event;
import com.example.dataobjects.Invitee;
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
            viewHolder.eventHeaderIcon = (ImageView) cell.findViewById(R.id.eventIconHeader);
            viewHolder.eventNameHeader = (TextView) cell.findViewById(R.id.eventNameHeader);
            viewHolder.eventAddressHeader = (TextView) cell.findViewById(R.id.eventAddressHeader);
            viewHolder.eventDateTimeInfo = (TextView) cell.findViewById(R.id.eventTimingsHeading);
            viewHolder.colourIndicator = (TextView) cell.findViewById(R.id.colorIndicator);
            //FOOTER VIEW DETAILS
            viewHolder.eventNameFooter = (TextView) cell.findViewById(R.id.eventNameFooter);
            viewHolder.eventImageFooter = (ImageView) cell.findViewById(R.id.eventImageFooter);
            viewHolder.ownerImage = (ImageView) cell.findViewById(R.id.eventOwnerImage);
            viewHolder.ownerName = (TextView) cell.findViewById(R.id.ownerName);
            viewHolder.eventStartDate = (TextView) cell.findViewById(R.id.eventDate);
            viewHolder.eventTimings = (TextView) cell.findViewById(R.id.eventTimings);
            viewHolder.eventAddressFooter = (TextView) cell.findViewById(R.id.eventAddressFooter);
            viewHolder.inviteesCount = (TextView) cell.findViewById(R.id.totalInvitees);
            viewHolder.acceptedCount = (TextView) cell.findViewById(R.id.totalAccepted);
            viewHolder.rejectedCount = (TextView) cell.findViewById(R.id.totalRejected);
            viewHolder.participantsLayout = (LinearLayout) cell.findViewById(R.id.participantsLayout);
            viewHolder.actionOneLayout = (LinearLayout) cell.findViewById(R.id.actionOne);
            viewHolder.actionTwoLayout = (LinearLayout) cell.findViewById(R.id.actionTwo);
            viewHolder.actionThreeLayout = (LinearLayout) cell.findViewById(R.id.actionThree);
            viewHolder.eventDetailsLayout = (LinearLayout) cell.findViewById(R.id.showEventDetails);
            viewHolder.actionOneIcon = (ImageView) cell.findViewById(R.id.actionOneIcon);
            viewHolder.actionTwoIcon = (ImageView) cell.findViewById(R.id.actionTwoIcon);
            viewHolder.actionThreeIcon = (ImageView) cell.findViewById(R.id.actionThreeIcon);
            viewHolder.showEventIcon = (ImageView) cell.findViewById(R.id.showEventIcon);
            viewHolder.participantsStatisticsLayout = (LinearLayout) cell.findViewById(R.id.participantsStatisticsLayout);
            viewHolder.actionsLayout = (LinearLayout)cell.findViewById(R.id.actionsLayout);
            cell.setTag(viewHolder);
        } else {
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (ViewHolder) cell.getTag();
        }
        viewHolder.participantsStatisticsLayout.setVisibility(View.GONE);
        viewHolder.showEventIcon.setOnClickListener(createOnClickListener(position, parent));
        viewHolder.actionOneLayout.setOnClickListener(createOnClickListener(position, parent));
        viewHolder.actionTwoLayout.setOnClickListener(createOnClickListener(position, parent));
        viewHolder.actionThreeLayout.setOnClickListener(createOnClickListener(position, parent));
        viewHolder.eventDetailsLayout.setOnClickListener(createOnClickListener(position, parent));
        if(event.getImageUrl()!=null && !event.getImageUrl().trim().isEmpty()) {
            Picasso.with(context).load(event.getImageUrl()).into(viewHolder.eventImageFooter);
        }else {
           Picasso.with(context).load("java").placeholder(R.drawable.event_picture).into(viewHolder.eventImageFooter);
        }
        if(event.getOwnerInfo().getImage()!= null && !event.getOwnerInfo().getImage().trim().isEmpty()) {
            Picasso.with(context).load(event.getOwnerInfo().getImage()).placeholder(R.drawable.logo).transform(new CircleTransform()).into(viewHolder.eventHeaderIcon);
            Picasso.with(context).load(event.getOwnerInfo().getImage()).placeholder(R.drawable.logo).transform(new CircleTransform()).into(viewHolder.ownerImage);
        }else {
            Picasso.with(context).load("java").placeholder(R.drawable.logo).transform(new CircleTransform()).into(viewHolder.eventHeaderIcon);
            Picasso.with(context).load("java").placeholder(R.drawable.logo).transform(new CircleTransform()).into(viewHolder.ownerImage);
        }
        if(event.getAddress()!= null && !event.getAddress().isEmpty()){
            String addressInfo = event.getAddress().replace("\n","");
            viewHolder.eventAddressHeader.setText(addressInfo);
            viewHolder.eventAddressFooter.setText(addressInfo);
        }else{
            viewHolder.eventAddressHeader.setText("Location details not provided");
            viewHolder.eventAddressFooter.setText("Location details not provided");
        }
        viewHolder.eventNameHeader.setText(event.getName());
        viewHolder.eventNameFooter.setText(event.getName());
        viewHolder.ownerName.setText(event.getOwnerInfo().getInviteeName());
        viewHolder.inviteesCount.setText("" + event.getInviteesCount());
        viewHolder.acceptedCount.setText("" + event.getAcceptedCount());
        viewHolder.rejectedCount.setText("" + event.getRejectedCount());
        viewHolder.eventDateTimeInfo.setText(event.getStartDateTime()+" "+ event.getEndDateTime());
        viewHolder.eventStartDate.setText("" + event.getStartDateTime());
        viewHolder.eventTimings.setText("" + event.getEndDateTime());
        int backgroundColour = (!event.isInvitation()) ? context.getResources().getColor(R.color.invitation_received_color) :(event.isAccepted())? context.getResources().getColor(R.color.event_accept): context.getResources().getColor(R.color.my_events_color);
        viewHolder.colourIndicator.setBackgroundColor(backgroundColour);
        viewHolder.eventNameFooter.setBackgroundColor(backgroundColour);
        int count = 0;
        viewHolder.participantsLayout.removeAllViews();
        for (Invitee invitee : event.getInviteesList()) {
            View view = LayoutInflater.from(context).inflate(R.layout.profile_image_layout, parent, false);
            ImageView profileImage = (ImageView) view.findViewById(R.id.profileImage);
            if(invitee.getImage()!=null && !invitee.getImage().trim().isEmpty()) {
                Picasso.with(context).load(invitee.getImage()).transform(new CircleTransform()).into(profileImage);
                viewHolder.participantsLayout.addView(view);
                count++;
                if (count > 5) {
                    break;
                }
            }
        }
        if (event.getInviteesList().size() == 0) {
            viewHolder.participantsLayout.setVisibility(View.GONE);
        } else {
            viewHolder.participantsLayout.setVisibility(View.VISIBLE);
            viewHolder.participantsLayout.invalidate();
        }
        updateActionIcons(viewHolder, event);
        return cell;
    }

    private void updateActionIcons(ViewHolder viewHolder, Event event) {
        if (!event.isInvitation()) {
            viewHolder.actionOneIcon.setImageResource(R.drawable.share_icon);
            viewHolder.actionTwoIcon.setImageResource(R.drawable.edit_icon);
            viewHolder.participantsStatisticsLayout.setVisibility(View.VISIBLE);
        } else {
            viewHolder.actionOneIcon.setImageResource(R.drawable.done_icon);
            viewHolder.actionTwoIcon.setImageResource(R.drawable.question_mark_icon);
            if(event.isAccepted()){
                viewHolder.actionsLayout.setVisibility(View.GONE);
            }
        }
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
        ImageView eventHeaderIcon, eventImageFooter, ownerImage;
        TextView eventNameHeader, colourIndicator, eventAddressHeader, eventDateTimeInfo, eventNameFooter, ownerName, eventStartDate, eventTimings, eventAddressFooter, inviteesCount, acceptedCount, rejectedCount;
        LinearLayout participantsLayout, actionOneLayout, actionTwoLayout, actionThreeLayout, eventDetailsLayout, participantsStatisticsLayout,actionsLayout;
        ImageView actionOneIcon, actionTwoIcon, actionThreeIcon, showEventIcon;

    }
}
