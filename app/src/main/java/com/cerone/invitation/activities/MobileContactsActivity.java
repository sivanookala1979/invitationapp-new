package com.cerone.invitation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.cerone.invitation.adapter.ContactsAdapter;
import com.cerone.invitation.helpers.HappeningTextWatcher;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.cerone.invitation.helpers.MobileHelper;
import com.example.dataobjects.Event;
import com.example.dataobjects.User;
import com.example.syncher.EventSyncher;
import com.example.utills.StringUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by adarsh on 12/26/16.
 */

public class MobileContactsActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    EditText searchTextField;
    ListView contactsListView;
    TextView inviteFriends;
    List<User> allContacts;
    List<User> allContactsList;
    List<User> filterList = new ArrayList<User>();
    List<User> selectedList = new ArrayList<User>();
    ContactsAdapter contactsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_activity_layout);
        addToolbarView();
        setFontType(R.id.share, R.id.event_date, R.id.event_time, R.id.event_address, R.id.search);
       // final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        searchTextField = (EditText) findViewById(R.id.search);
        contactsListView = (ListView) findViewById(R.id.contactsListView);
        inviteFriends = (TextView) findViewById(R.id.share);
        inviteFriends.setOnClickListener(this);
        contactsListView.setOnItemClickListener(this);
        contactsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        searchTextField.addTextChangedListener(new HappeningTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String filterString = searchTextField.getText().toString();
                if (filterString.length() > 2) {
                    filterList = getFilterList(filterString);
                }
                if (filterString.length() == 0) {
                    filterList.clear();
                    filterList.addAll(allContactsList);
                }
                contactsAdapter.updateAdapter(filterList);
            }
        });
        allContactsList = InvtAppPreferences.getAllContacts();
        Log.d("list size", allContactsList.size()+"");
        if(allContactsList.size() == 0){
            loadAllMobileContacts();
        }else{
            filterList = allContactsList;
        }
        contactsAdapter = new ContactsAdapter(MobileContactsActivity.this, R.layout.contact_layout, allContactsList);
        contactsListView.setAdapter(contactsAdapter);
        try {
            showEventData();
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                swipeView.setRefreshing(true);
//                allContactsList.clear();
//                loadAllMobileContacts();
//                Log.d("Swipe", "Refreshing Number");
//                ( new Handler()).postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        swipeView.setRefreshing(false);
//                    }
//                }, 1000);
//            }
//        });
    }

    private void showEventData() throws ParseException {
        Event eventDetails = InvtAppPreferences.getEventDetails();
        TextView eventDate = (TextView) findViewById(R.id.event_date);
        TextView eventTime = (TextView) findViewById(R.id.event_time);
        TextView eventLocation = (TextView) findViewById(R.id.event_address);
        if(eventDetails!=null) {
            try {
                eventDate.setText(StringUtils.getEventDateFormat(eventDetails.getStartDateTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            eventTime.setText(StringUtils.getEventTimeFormat(eventDetails.getStartDateTime(), eventDetails.getEndDateTime()));
            if(eventDetails.getAddress()!=null&&!eventDetails.getAddress().isEmpty())
            eventLocation.setText(eventDetails.getAddress());
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.share:
                    if (selectedList.size() >= 1) {
                        Intent intent = new Intent();
                        setResult(100, intent);
                        InvtAppPreferences.setContacts(selectedList);
                        Log.d("selected contacts", selectedList.size()+"");
                        finish();
                    } else {
                        setSnackBarValidation("Please select contacts.");
                    }
                break;
        }
    }

    private void loadAllMobileContacts() {
            new InvtAppAsyncTask(MobileContactsActivity.this) {

                @Override
                public void process() {
                    allContacts = MobileHelper.getAllPhoneContacts(MobileContactsActivity.this);
                    EventSyncher syncher = new EventSyncher();
                    allContactsList = syncher.getcontactsList(allContacts);
                }

                @Override
                public void afterPostExecute() {
                    if(allContactsList.size()>0) {
                        filterList.addAll(allContactsList);
                        contactsAdapter.updateAdapter(filterList);
                        InvtAppPreferences.setAllContacts(allContactsList);
                    }
                }
            }.execute();
        }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        User user = filterList.get(i);
        Log.d("name", user.getUserName());
        if(view.getId()==R.id.contact_check) {
            if (!selectedList.contains(user)) {
                user.setSelected(true);
                selectedList.add(user);
            } else {
                selectedList.remove(user);
                user.setSelected(false);
            }
            contactsAdapter.updateAdapter(filterList);
        }
    }

    private List<User> getFilterList(String filterString) {
        List<User> list = new ArrayList<User>();
        for (User contact : allContactsList) {
            if (contact.getUserName().toLowerCase().contains(filterString.toLowerCase()) || contact.getPhoneNumber().contains(filterString)) {
                list.add(contact);
                if (contact.isSelected()) {
                    selectedList.add(contact);
                }
            }
        }
        return list;
    }
}
