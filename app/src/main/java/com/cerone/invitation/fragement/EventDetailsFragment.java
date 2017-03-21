package com.cerone.invitation.fragement;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.cerone.invitation.activities.InvitieesTabActivity;
import com.cerone.invitation.activities.NewEventActivity;
import com.cerone.invitation.activities.ParticipantsActivity;
import com.cerone.invitation.activities.ShareEventActivity;
import com.cerone.invitation.activities.ShowInviteePositions;
import com.cerone.invitation.helpers.ActivityCommunicator;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.cerone.invitation.helpers.ToastHelper;
import com.example.dataobjects.Event;
import com.example.dataobjects.Eventstatistics;
import com.example.syncher.InvitationSyncher;
import com.example.utills.StringUtils;

import static android.support.v7.widget.AppCompatDrawableManager.get;
import static com.cerone.invitation.R.id.acceptCount;
import static com.cerone.invitation.R.id.eventName;
import static com.cerone.invitation.R.id.rejectCount;

/**
 * Created by adarsht on 09/03/17.
 */

public class EventDetailsFragment extends BaseFragment implements View.OnClickListener{

    LinearLayout participantsLayout, location,invitationSelection;
    Button editEvent, shareEvent, deleteEvent,accept, maybe, reject;
    TextView acceptCountText, rejectCountText, totalCount,checkedInCount;
    View eventBaseView;
    //INVITATIONS

    private ActivityCommunicator activityCommunicator;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.event_details_fragment, container, false);
        eventBaseView = view;
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
        activityCommunicator =(ActivityCommunicator) getActivity();
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
        acceptCountText = (TextView) view.findViewById(acceptCount);
        rejectCountText = (TextView) view.findViewById(rejectCount);
        checkedInCount = (TextView) view.findViewById(R.id.checkedinCount);

        if (!eventDetails.getExtraAddress().isEmpty()) {
            LinearLayout extraAddressLayout = (LinearLayout) view.findViewById(R.id.extraAddressLayout);
            extraAddressLayout.setVisibility(View.VISIBLE);
            TextView extraAddress = (TextView) view.findViewById(R.id.extraAddress);
            extraAddress.setText("" + eventDetails.getExtraAddress());
        }
        totalCount = (TextView) view.findViewById(R.id.totalCount);
        TextView checkedInCount = (TextView) view.findViewById(R.id.checkedinCount);
        acceptCountText.setText("" + eventDetails.getAcceptedCount());
        rejectCountText.setText("" + eventDetails.getRejectedCount());
        totalCount.setText("" + eventDetails.getInviteesCount());
        checkedInCount.setText("" + eventDetails.getCheckedInCount());
        description.setText(eventDetails.getDescription());
        eventStartDate.setText(StringUtils.formatDateAndTime(eventDetails.getStartDateTime(), 1));
        eventEndDate.setText(StringUtils.formatDateAndTime(eventDetails.getEndDateTime(), 1));
        eventStartTime.setText(StringUtils.formatDateAndTime(eventDetails.getStartDateTime(), 2));
        eventEndTime.setText(StringUtils.formatDateAndTime(eventDetails.getEndDateTime(), 2));
        eventLocation.setText(eventDetails.getAddress());
        participantsInfo.setText("invitees ");
        if(eventDetails.isInvitation()){
            editEvent.setVisibility(View.INVISIBLE);
            deleteEvent.setVisibility(View.INVISIBLE);
            shareEvent.setText("invitees");
            invitationSelection = (LinearLayout) eventBaseView.findViewById(R.id.invitationSelection);
            invitationSelection.setVisibility(View.VISIBLE);
            accept = (Button) eventBaseView.findViewById(R.id.acceptInvitation);
            maybe = (Button) eventBaseView.findViewById(R.id.mayBe);
            reject = (Button) eventBaseView.findViewById(R.id.rejected);
            location = (LinearLayout) eventBaseView.findViewById(R.id.gpsLocationLayout);
            participantsLayout.setVisibility(View.GONE);
            location.setVisibility(View.VISIBLE);
            if(eventDetails.isAccepted()){
                invitationSelection.setVisibility(View.GONE);
            }else {
                invitationSelection.setVisibility(View.VISIBLE);
            }
            location.setOnClickListener(this);
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
                if(eventDetails.isInvitation()){
                    intent = new Intent(getActivity(), InvitieesTabActivity.class);
                }else {
                    intent = new Intent(getActivity(), ShareEventActivity.class);
                    intent.putExtra("newEvent", false);
                }
                startActivity(intent);
                break;
            case R.id.gpsLocationIcon :
            case R.id.gpsLocationLayout :
                intent = new Intent(getActivity(), ShowInviteePositions.class);
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
            case R.id.inviteesInfo :
                intent = new Intent(getActivity(), InvitieesTabActivity.class);
                startActivity(intent);
                break;
        }
    }
    @Override
    public void acceptOrRejectInvitation(final boolean status, final Event event, final String locationPermission) {
        final String response = "";
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
                    rejectCountText.setText(response.getRejectCount() + "");
                    acceptCountText.setText(response.getAcceptCount() + "");
                    totalCount.setText(response.getAcceptCount() + "");
                    checkedInCount.setText("" + response.getCheckedInCount());
                    invitationSelection.setVisibility(View.GONE);
                }
                ToastHelper.blueToast(getActivity(), response.getMessage());
            }
        }.execute();
    }


    public static EventDetailsFragment newInstance() {
        EventDetailsFragment fragment = new EventDetailsFragment();
        Bundle args = new Bundle();
        return fragment;
    }
}
