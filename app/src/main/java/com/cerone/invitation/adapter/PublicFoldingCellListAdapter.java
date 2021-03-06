package com.cerone.invitation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.cerone.invitation.helpers.FontTypes;
import com.example.dataobjects.PublicEvent;
import com.example.utills.StringUtils;
import com.ramotion.foldingcell.FoldingCell;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.cerone.invitation.R.id.locationAddress;
import static com.cerone.invitation.R.id.showEventIcon;
import static com.example.utills.StringUtils.getFormatedDateFromServerFormatedDate;

public class PublicFoldingCellListAdapter extends ArrayAdapter<PublicEvent> {
    private RecyclerView.LayoutManager mLayoutManager;
    Context context;
    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;
    List<PublicEvent> events = new ArrayList<PublicEvent>();
    FontTypes fontType;

    public PublicFoldingCellListAdapter(Context context, List<PublicEvent> allEventsList) {
        super(context, 0, allEventsList);
        this.context = context;
        this.events = allEventsList;
        fontType = new FontTypes(context);
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public PublicEvent getItem(int position) {
        return events.get(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get item for selected view
        // if cell is exists - reuse it, if not - create the new one from resource

        PublicEvent publicEvent = getItem(position);

        FoldingCell cell = (FoldingCell) convertView;
        final ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.public_event_folding_cell, parent, false);
            // binding view parts to view holder
            viewHolder.eventIconHeader = (ImageView) cell.findViewById(R.id.eventIconHeader);
            viewHolder.headerEventName = (TextView) cell.findViewById(R.id.headerEventName);
            viewHolder.eventAddressHeader = (TextView) cell.findViewById(R.id.eventAddressHeader);
            viewHolder.eventTimingsHeading = (TextView) cell.findViewById(R.id.eventTimingsHeading);
            viewHolder.entryFeeHeader = (TextView) cell.findViewById(R.id.entryFeeHeader);
            viewHolder.headerFavourite = (ImageView) cell.findViewById(R.id.header_favourite);
            viewHolder.headerCart = (ImageView) cell.findViewById(R.id.header_cart);
            viewHolder.headerFriendsAttending = (ImageView) cell.findViewById(R.id.header_friendsAttending);
            viewHolder.headerClose = (ImageView) cell.findViewById(R.id.header_close);
            viewHolder.layoutFavourite = (RelativeLayout) cell.findViewById(R.id.layout_favourite);
            viewHolder.layoutCart = (RelativeLayout) cell.findViewById(R.id.layout_cart);
            viewHolder.layoutFriendsAttending = (RelativeLayout) cell.findViewById(R.id.layout_friendsAttending);
            viewHolder.layoutClose = (RelativeLayout) cell.findViewById(R.id.layout_close);


            //footer view parts to view holder
            viewHolder.footerEventName = (TextView) cell.findViewById(R.id.footer_event_name);
            viewHolder.footerEventDate = (TextView) cell.findViewById(R.id.footer_event_date);
            viewHolder.footerEventTime = (TextView) cell.findViewById(R.id.footer_event_time);
            viewHolder.footerEventAddress = (TextView) cell.findViewById(R.id.footer_event_address);
            viewHolder.footerFavourite = (ImageView) cell.findViewById(R.id.footer_favourite);
            viewHolder.footerCart = (ImageView) cell.findViewById(R.id.footer_cart);
            viewHolder.footerFriendsAttending = (ImageView) cell.findViewById(R.id.footer_friendsAttending);
            viewHolder.footerClose = (ImageView) cell.findViewById(R.id.footer_close);
            viewHolder.showEventIcon = (ImageView) cell.findViewById(showEventIcon);
            viewHolder.locationAddress = (ImageView) cell.findViewById(locationAddress);
            viewHolder.showEventDetails = (LinearLayout) cell.findViewById(R.id.showEventDetails);

            cell.setTag(viewHolder);

        } else {
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (PublicFoldingCellListAdapter.ViewHolder) cell.getTag();
        }
        if(publicEvent.getImage()!= null && !publicEvent.getImage().trim().isEmpty()) {
            Picasso.with(context).load(publicEvent.getImage()).placeholder(R.drawable.event_picture).into(viewHolder.eventIconHeader);
        }else {
            Picasso.with(context).load("java").placeholder(R.drawable.event_picture).into(viewHolder.eventIconHeader);
        }
            viewHolder.headerEventName.setText(publicEvent.getEventName());
            viewHolder.eventAddressHeader.setText(publicEvent.getAddress());
        viewHolder.entryFeeHeader.setText(publicEvent.getEntryFee().equals("0.0")?"Free":publicEvent.getEntryFee());
        try {
            viewHolder.eventTimingsHeading.setText(StringUtils.getEventDateFormat(getFormatedDateFromServerFormatedDate(publicEvent.getStartRime())) +" "+ StringUtils.getEventTimeFormat(getFormatedDateFromServerFormatedDate(publicEvent.getStartRime()), getFormatedDateFromServerFormatedDate(publicEvent.getEndTime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(publicEvent.isFavourite()){
            viewHolder.headerFavourite.setColorFilter(context.getResources().getColor(R.color.happening_orange));
            viewHolder.footerFavourite.setColorFilter(context.getResources().getColor(R.color.happening_orange));
        }else{
            viewHolder.headerFavourite.setColorFilter(context.getResources().getColor(R.color.darkgray));
            viewHolder.footerFavourite.setColorFilter(context.getResources().getColor(R.color.darkgray));
        }
        if(publicEvent.isCart()){
            viewHolder.headerCart.setColorFilter(context.getResources().getColor(R.color.happening_orange));
            viewHolder.footerCart.setColorFilter(context.getResources().getColor(R.color.happening_orange));
        }
        if(publicEvent.isFriendsAttending()){
            viewHolder.headerFriendsAttending.setColorFilter(context.getResources().getColor(R.color.happening_orange));
            viewHolder.footerFriendsAttending.setColorFilter(context.getResources().getColor(R.color.happening_orange));
        }
        viewHolder.footerEventName.setText(publicEvent.getEventName());
        viewHolder.footerEventAddress.setText(publicEvent.getAddress());
        try {
            viewHolder.footerEventDate.setText(StringUtils.getEventDateFormat(getFormatedDateFromServerFormatedDate(publicEvent.getStartRime())));
            viewHolder.footerEventTime.setText(StringUtils.getEventTimeFormat(getFormatedDateFromServerFormatedDate(publicEvent.getStartRime()), getFormatedDateFromServerFormatedDate(publicEvent.getEndTime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(publicEvent.isFavourite()){
            viewHolder.footerFavourite.setColorFilter(context.getResources().getColor(R.color.happening_orange));
        }

        viewHolder.headerFavourite.setOnClickListener(createOnClickListener(position, parent));
        viewHolder.footerFavourite.setOnClickListener(createOnClickListener(position, parent));
        viewHolder.headerCart.setOnClickListener(createOnClickListener(position, parent));
        viewHolder.footerCart.setOnClickListener(createOnClickListener(position, parent));
        viewHolder.headerFriendsAttending.setOnClickListener(createOnClickListener(position, parent));
        viewHolder.footerFriendsAttending.setOnClickListener(createOnClickListener(position, parent));
        viewHolder.headerClose.setOnClickListener(createOnClickListener(position, parent));
        viewHolder.footerClose.setOnClickListener(createOnClickListener(position, parent));
        viewHolder.showEventIcon.setOnClickListener(createOnClickListener(position, parent));
        viewHolder.showEventDetails.setOnClickListener(createOnClickListener(position, parent));
        viewHolder.locationAddress.setOnClickListener(createOnClickListener(position, parent));
        viewHolder.layoutFavourite.setOnClickListener(createOnClickListener(position, parent));
        viewHolder.layoutCart.setOnClickListener(createOnClickListener(position, parent));
        viewHolder.layoutFriendsAttending.setOnClickListener(createOnClickListener(position, parent));
        viewHolder.layoutClose.setOnClickListener(createOnClickListener(position, parent));

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

    public void updateList(List<PublicEvent> allEventsList) {
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
        ImageView eventIconHeader, headerFavourite, headerCart, headerFriendsAttending, headerClose, footerFavourite, footerCart, footerFriendsAttending, footerClose, showEventIcon, locationAddress;
        TextView headerEventName, eventAddressHeader, eventTimingsHeading, entryFeeHeader, footerEventName, footerEventDate, footerEventTime, footerEventAddress;
        RelativeLayout layoutFavourite, layoutCart, layoutFriendsAttending, layoutClose;
        LinearLayout showEventDetails;
    }
}
