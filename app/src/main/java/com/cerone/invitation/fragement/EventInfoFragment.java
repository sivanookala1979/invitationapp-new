package com.cerone.invitation.fragement;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.cerone.invitation.activities.CreateNewEventActivity;
import com.cerone.invitation.activities.HomeScreenActivity;
import com.cerone.invitation.activities.InvitieesTabActivity;
import com.cerone.invitation.activities.LocationDetailsActivity;
import com.cerone.invitation.activities.ParticipantsActivity;
import com.cerone.invitation.activities.ShareEventActivity;
import com.cerone.invitation.helpers.ActivityCommunicator;
import com.cerone.invitation.helpers.CircleTransform;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.cerone.invitation.helpers.ToastHelper;
import com.example.dataobjects.Event;
import com.example.dataobjects.Eventstatistics;
import com.example.dataobjects.ServerResponse;
import com.example.syncher.EventSyncher;
import com.example.syncher.InvitationSyncher;
import com.example.utills.StringUtils;
import com.squareup.picasso.Picasso;

import java.text.ParseException;

import static com.cerone.invitation.R.id.location_address;


public class EventInfoFragment extends BaseFragment implements View.OnClickListener{

    LinearLayout participantsLayout, invitationSelection, accept, maybe, reject,editEvent, shareEvent, deleteEvent;
    TextView totalInviteesText, acceptCountText, rejectCountText;
    View eventBaseView;
    ImageView editOrShareIdon, locationAddress;
    //INVITATIONS

    private ActivityCommunicator activityCommunicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_info_fragment, container, false);
        eventBaseView = view;
        participantsLayout = (LinearLayout)view.findViewById(R.id.participantsLayout);
        shareEvent = (LinearLayout) view.findViewById(R.id.actionOne);
        editEvent = (LinearLayout) view.findViewById(R.id.actionTwo);
        deleteEvent = (LinearLayout) view.findViewById(R.id.actionThree);
        editOrShareIdon = (ImageView) view.findViewById(R.id.actionTwoIcon);
        locationAddress = (ImageView) view.findViewById(location_address);
        participantsLayout.setOnClickListener(this);
        editEvent.setOnClickListener(this);
        shareEvent.setOnClickListener(this);
        deleteEvent.setOnClickListener(this);
        locationAddress.setOnClickListener(this);
        eventDetails = InvtAppPreferences.getEventDetails();
        loadEventData(view);
        activityCommunicator =(ActivityCommunicator) getActivity();

        return view;
    }

    private void loadEventData(View view) {
        TextView ownerName = (TextView) view.findViewById(R.id.ownerName);
        TextView eventDate = (TextView) view.findViewById(R.id.eventDate);
        TextView eventTime = (TextView) view.findViewById(R.id.eventTimings);
        TextView eventLocation = (TextView) view.findViewById(R.id.event_address);
        ImageView eventImage = (ImageView) view.findViewById(R.id.eventImage);
        ImageView eventOwnerImage = (ImageView) view.findViewById(R.id.eventOwnerImage);
        totalInviteesText = (TextView) view.findViewById(R.id.totalInvitees);
        acceptCountText = (TextView) view.findViewById(R.id.totalAccepted);
        rejectCountText = (TextView) view.findViewById(R.id.totalRejected);
        acceptCountText.setText("" + eventDetails.getAcceptedCount());
        rejectCountText.setText("" + eventDetails.getRejectedCount());
        totalInviteesText.setText("" + eventDetails.getInviteesCount());
        if(eventDetails.getImageUrl()!=null&&!eventDetails.getImageUrl().isEmpty()) {
            Picasso.with(getActivity()).load(eventDetails.getImageUrl()).placeholder(R.drawable.event_picture).into(eventImage);
        }
        if(eventDetails!=null&&eventDetails.getOwnerInfo()!=null&&eventDetails.getOwnerInfo().getImage()!=null) {
            Picasso.with(getActivity()).load(eventDetails.getOwnerInfo().getImage()).placeholder(R.drawable.logo).transform(new CircleTransform()).into(eventOwnerImage);
            ownerName.setText(eventDetails.getOwnerInfo().getInviteeName());
        }
        try {
            eventDate.setText(StringUtils.getEventDateFormat(eventDetails.getStartDateTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            eventTime.setText(StringUtils.getEventTimeFormat(eventDetails.getStartDateTime(), eventDetails.getEndDateTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(eventDetails.getAddress()!=null&&!eventDetails.getAddress().isEmpty()) {
            eventLocation.setText(eventDetails.getAddress());
        }
        if(eventDetails.isInvitation()){
            editEvent.setVisibility(View.GONE);
            deleteEvent.setVisibility(View.GONE);
            editOrShareIdon.setBackgroundResource(R.drawable.group);
            invitationSelection = (LinearLayout) eventBaseView.findViewById(R.id.invitationSelection);
            invitationSelection.setVisibility(View.VISIBLE);
            accept = (LinearLayout) eventBaseView.findViewById(R.id.acceptInvitation);
            maybe = (LinearLayout) eventBaseView.findViewById(R.id.mayBe);
            reject = (LinearLayout) eventBaseView.findViewById(R.id.rejected);
            participantsLayout.setVisibility(View.GONE);
            if(eventDetails.isAccepted()){
                invitationSelection.setVisibility(View.GONE);
            }else {
                invitationSelection.setVisibility(View.VISIBLE);
            }
            accept.setOnClickListener(this);
            maybe.setOnClickListener(this);
            reject.setOnClickListener(this);
        }
        if(eventDetails.isExpired()){
            view.findViewById(R.id.expiredMessage).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {

            case R.id.actionOne:
                if(eventDetails.isInvitation()){
                    intent = new Intent(getActivity(), ParticipantsActivity.class);
                    intent.putExtra("eventId", eventDetails.getEventId());
                    intent.putExtra("title", "Invitees");
                }else {
                    intent = new Intent(getActivity(), ShareEventActivity.class);
                    intent.putExtra("newEvent", false);
                }
                startActivity(intent);
                break;
            case R.id.actionTwo:
                intent = new Intent(getActivity(), CreateNewEventActivity.class);
                startActivity(intent);
                break;
            case R.id.actionThree:
                deleteEvent();
                intent = new Intent(getActivity(), HomeScreenActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case location_address :
                intent = new Intent(getActivity(), LocationDetailsActivity.class);
                startActivity(intent);
                break;
            case R.id.acceptInvitation :
                showLocationPermissionDialog();
                break;
            case R.id.mayBe :
                invitationSelection.setVisibility(View.GONE);
                break;
            case R.id.rejected :
                acceptOrRejectInvitation(false, eventDetails, locationPermission);
                break;
            case R.id.actionTwoIcon :
                intent = new Intent(getActivity(), InvitieesTabActivity.class);
                startActivity(intent);
                break;
        }
    }
    @Override
    public void acceptOrRejectInvitation(final boolean status, final Event event, final String locationPermission) {
        new InvtAppAsyncTask(getActivity()) {

            Eventstatistics response;

            @Override
            public void process() {
                InvitationSyncher invitationSyncher = new InvitationSyncher();
                response = invitationSyncher.getInvitationStatus(event.getEventId(), status, locationPermission);
            }

            @Override
            public void afterPostExecute() {
                eventBaseView.findViewById(R.id.invitationSelection).setVisibility(View.VISIBLE);
                if (response.isValid()) {
                    //TODO NEED TO ACTIVATE SERVICE
                    if (status) {
                        //activateService();
                        activityCommunicator.enableChatView(true);
                    }
                    totalInviteesText.setText(response.getAcceptCount() + "");
                    rejectCountText.setText(response.getRejectCount() + "");
                    acceptCountText.setText(response.getAcceptCount() + "");
                    invitationSelection.setVisibility(View.GONE);
                }
                ToastHelper.blueToast(getActivity(), response.getMessage());
            }
        }.execute();
    }

    public void deleteEvent() {
        new InvtAppAsyncTask(getActivity()) {

            ServerResponse response;

            @Override
            public void process() {
                if(eventDetails.getEventId()>0) {
                    EventSyncher syncher = new EventSyncher();
                    response = syncher.deleteEvent(eventDetails.getEventId());
                }
            }

            @Override
            public void afterPostExecute() {
                if (response!=null) {
                    ToastHelper.blueToast(getActivity(), response.getStatus());
                }
            }
        }.execute();
    }

    public static EventInfoFragment newInstance() {
        EventInfoFragment fragment = new EventInfoFragment();
        Bundle args = new Bundle();
        return fragment;
    }
}
