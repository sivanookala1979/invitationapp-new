package com.cerone.invitation.fragement;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.cerone.invitation.activities.chat.IntraChatActivity;
import com.cerone.invitation.adapter.AcceptedParticipantsAdapater;
import com.cerone.invitation.helpers.ActivityCommunicator;
import com.cerone.invitation.helpers.CircleTransform;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.cerone.invitation.helpers.ToastHelper;
import com.example.dataobjects.Event;
import com.example.dataobjects.Eventstatistics;
import com.example.dataobjects.Invitees;
import com.example.dataobjects.ServerResponse;
import com.example.syncher.EventSyncher;
import com.example.syncher.InvitationSyncher;
import com.example.utills.StringUtils;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.cerone.invitation.R.id.location_address;


public class EventInfoFragment extends BaseFragment implements View.OnClickListener{

    LinearLayout invitationSelection,inviteesLayout, accept, maybe, reject,editEvent,actionsLayout, shareEvent, deleteEvent, chatLayout, layoutParticipants;
    TextView totalInviteesText, acceptCountText, rejectCountText;
    RecyclerView participantsLayout;
    View eventBaseView;
    ImageView editOrShareIdon, locationAddress, chatIcon;
    List<Invitees> allInvitees;
    AcceptedParticipantsAdapater acceptedParticipantsAdapater;

    //INVITATIONS

    private ActivityCommunicator activityCommunicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_info_fragment, container, false);
        eventBaseView = view;
        eventDetails = InvtAppPreferences.getEventDetails();
        participantsLayout = (RecyclerView)view.findViewById(R.id.participantsLayout);
        participantsLayout.setLayoutManager( new LinearLayoutManager(getActivity()));
        acceptedParticipantsAdapater = new AcceptedParticipantsAdapater(getActivity(), allInvitees, eventDetails.getEventId());
        participantsLayout.setAdapter(acceptedParticipantsAdapater);
        ViewGroup.LayoutParams params=participantsLayout.getLayoutParams();
        params.height=ViewGroup.LayoutParams.WRAP_CONTENT;
        participantsLayout.setLayoutParams(params);
        participantsLayout.setNestedScrollingEnabled(false);
        actionsLayout = (LinearLayout)view.findViewById(R.id.actionsLayout);
        shareEvent = (LinearLayout) view.findViewById(R.id.actionOne);
        editEvent = (LinearLayout) view.findViewById(R.id.actionTwo);
        deleteEvent = (LinearLayout) view.findViewById(R.id.actionThree);
        inviteesLayout = (LinearLayout) view.findViewById(R.id.invitees_layout);
        layoutParticipants = (LinearLayout) view.findViewById(R.id.layout_participants);
        chatLayout = (LinearLayout) view.findViewById(R.id.chatLayout);
        editOrShareIdon = (ImageView) view.findViewById(R.id.actionTwoIcon);
        locationAddress = (ImageView) view.findViewById(R.id.location_address);
        chatIcon = (ImageView) view.findViewById(R.id.chatIcon);
        participantsLayout.setOnClickListener(this);
        actionsLayout.setOnClickListener(this);
        editEvent.setOnClickListener(this);
        shareEvent.setOnClickListener(this);
        deleteEvent.setOnClickListener(this);
        inviteesLayout.setOnClickListener(this);
        locationAddress.setOnClickListener(this);
        chatLayout.setOnClickListener(this);
        chatIcon.setOnClickListener(this);
        loadEventData(view);
        getAllInvitees();
        activityCommunicator =(ActivityCommunicator) getActivity();
        String[] startTime = eventDetails.getStartDateTime().split(" ");
        long difference = getTimeDifference(startTime[1], getCurrentTime());
        Log.d("difference", startTime[1]+" "+getCurrentTime()+" "+difference);

        if(difference >= -30){
            layoutParticipants.setVisibility(View.VISIBLE);
        }
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
            actionsLayout.setVisibility(View.GONE);
            editOrShareIdon.setBackgroundResource(R.drawable.group);
            invitationSelection = (LinearLayout) eventBaseView.findViewById(R.id.invitationSelection);
            invitationSelection.setVisibility(View.VISIBLE);
            accept = (LinearLayout) eventBaseView.findViewById(R.id.acceptInvitation);
            maybe = (LinearLayout) eventBaseView.findViewById(R.id.mayBe);
            reject = (LinearLayout) eventBaseView.findViewById(R.id.rejected);
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
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteEvent();
                        Intent intent = new Intent(getActivity(), HomeScreenActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });

                alertDialog.setNegativeButton("No", null);
                alertDialog.setMessage("Do you want to delete?");
                alertDialog.show();
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
            case R.id.invitees_layout :
                    intent = new Intent(getActivity(), ParticipantsActivity.class);
                    intent.putExtra("eventId", eventDetails.getEventId());
                    intent.putExtra("title", "Invitees");
                    startActivity(intent);
                break;
            case R.id.chatLayout:
            case R.id.chatIcon:
                intent = new Intent(getActivity(), IntraChatActivity.class);
                intent.putExtra("UserId", eventDetails.getOwnerId());
                intent.putExtra("UserImage", "");
                intent.putExtra("UserName", eventDetails.getOwnerName());
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

    public void getAllInvitees() {
        new InvtAppAsyncTask(getActivity()) {

            @Override
            public void process() {
                if(eventDetails.getEventId()>0) {
                    InvitationSyncher syncher = new InvitationSyncher();
                    allInvitees = syncher.getAllInviteesList(eventDetails.getEventId());
                }
            }

            @Override
            public void afterPostExecute() {
                if (allInvitees!=null) {
                    Log.d("all invitees", allInvitees.size()+"");
                    acceptedParticipantsAdapater.updateAdapter(allInvitees);
                }else{
                    ToastHelper.blueToast(getActivity(), "no invitees");
                }
            }
        }.execute();
    }

    public static String getCurrentTime() {

        Calendar now = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        return formatter.format(now.getTime());
    }

    public static long getTimeDifference(String time1, String time2) {

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date1 = null;
        try {
            date1 = format.parse(time1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date2 = null;
        try {
            date2 = format.parse(time2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long difference = date2.getTime() - date1.getTime();
        return difference / (60 * 1000) % 60;
    }

    public static EventInfoFragment newInstance() {
        EventInfoFragment fragment = new EventInfoFragment();
        Bundle args = new Bundle();
        return fragment;
    }
}
