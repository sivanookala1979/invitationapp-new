package com.cerone.invitation.fragement;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.cerone.invitation.activities.NewEventActivity;
import com.cerone.invitation.activities.ParticipantsActivity;
import com.cerone.invitation.activities.ShareEventActivity;
import com.cerone.invitation.helpers.ActivityCommunicator;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.example.dataobjects.Event;
import com.example.utills.StringUtils;

import static com.cerone.invitation.R.id.eventName;

/**
 * Created by adarsht on 09/03/17.
 */

public class EventDetailsFragment extends BaseFragment implements View.OnClickListener{
    Event eventDetails;
    LinearLayout participantsLayout;
    Button editEvent, shareEvent, deleteEvent;
    TextView totalCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.event_details_fragment, container, false);
        participantsLayout = (LinearLayout)view.findViewById(R.id.participantsLayout);
        editEvent = (Button)view.findViewById(R.id.editEvent);
        shareEvent = (Button) view.findViewById(R.id.shareEvent);
        deleteEvent = (Button) view.findViewById(R.id.deleteEvent);
        participantsLayout.setOnClickListener(this);
        editEvent.setOnClickListener(this);
        shareEvent.setOnClickListener(this);
        deleteEvent.setOnClickListener(this);
        eventDetails = InvtAppPreferences.getEventDetails();
        loadEventData(view);
        return view;
    }
    private void loadEventData(View view) {
        TextView description = (TextView) view.findViewById(R.id.description);
        TextView eventStartDate = (TextView) view.findViewById(R.id.eventStartDate);
        TextView eventEndDate = (TextView) view.findViewById(R.id.eventEndDate);
        TextView eventStartTime = (TextView) view.findViewById(R.id.eventStartTime);
        TextView eventEndTime = (TextView) view.findViewById(R.id.eventEndTime);
        TextView eventLocation = (TextView) view.findViewById(R.id.eventLocation);
        TextView participantsInfo = (TextView) view.findViewById(R.id.participantsInfo);
        TextView acceptCount = (TextView) view.findViewById(R.id.acceptCount);
        TextView rejectCount = (TextView) view.findViewById(R.id.rejectCount);
        if (!eventDetails.getExtraAddress().isEmpty()) {
            LinearLayout extraAddressLayout = (LinearLayout) view.findViewById(R.id.extraAddressLayout);
            extraAddressLayout.setVisibility(View.VISIBLE);
            TextView extraAddress = (TextView) view.findViewById(R.id.extraAddress);
            extraAddress.setText("" + eventDetails.getExtraAddress());
        }
        totalCount = (TextView) view.findViewById(R.id.totalCount);
        TextView checkedInCount = (TextView) view.findViewById(R.id.checkedinCount);
        acceptCount.setText("" + eventDetails.getAcceptedCount());
        rejectCount.setText("" + eventDetails.getRejectedCount());
        totalCount.setText("" + eventDetails.getInviteesCount());
        checkedInCount.setText("" + eventDetails.getCheckedInCount());
        description.setText(eventDetails.getDescription());
        eventStartDate.setText(StringUtils.formatDateAndTime(eventDetails.getStartDateTime(), 1));
        eventEndDate.setText(StringUtils.formatDateAndTime(eventDetails.getEndDateTime(), 1));
        eventStartTime.setText(StringUtils.formatDateAndTime(eventDetails.getStartDateTime(), 2));
        eventEndTime.setText(StringUtils.formatDateAndTime(eventDetails.getEndDateTime(), 2));
        eventLocation.setText(eventDetails.getAddress());
        participantsInfo.setText("invitees ");
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.participants:
            case R.id.participantsLayout:
                intent = new Intent(getActivity(), ParticipantsActivity.class);
                intent.putExtra("orderId", 0);
                intent.putExtra("title", "Invitees");
                startActivity(intent);
                break;
            case R.id.editEvent:
                intent = new Intent(getActivity(), NewEventActivity.class);
                startActivity(intent);
                break;
            case R.id.shareEvent:
                intent = new Intent(getActivity(), ShareEventActivity.class);
                intent.putExtra("newEvent", false);
                startActivity(intent);
                break;
        }
    }
    public static EventDetailsFragment newInstance() {
        EventDetailsFragment fragment = new EventDetailsFragment();
        Bundle args = new Bundle();
        return fragment;
    }
}
