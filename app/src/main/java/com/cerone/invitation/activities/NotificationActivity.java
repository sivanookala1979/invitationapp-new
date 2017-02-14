package com.cerone.invitation.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.cerone.invitation.R;
import com.cerone.invitation.adapter.NotificationAdapter;
import com.example.dataobjects.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends BaseActivity implements View.OnClickListener {

    List<Notification> pushNotifications = new ArrayList<Notification>();
    NotificationAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_layout);
        addToolbarView();

//        AdzShopPreferences.setNotificationCount(0);
//        MyGcmListenerService.messageList.clear();
//        MyGcmListenerService.notificationCount = 0;
//         listView = (ListView) findViewById(R.id.notification_listview);
//        loadNotifications();
//    }
//    void loadNotifications()
//    {
//        new InvtAppAsyncTask(NotificationActivity.this) {
//            @Override
//            public void process() {
//                NotificationSyncher syncher =new NotificationSyncher();
//                 pushNotifications = syncher.getPushNotifications();
//                SaveResult notified = syncher.notified();
//            }
//
//            @Override
//            public void afterPostExecute() {
//                if(pushNotifications.size() > 0){
//                      adapter =new NotificationAdapter(getApplicationContext(), R.layout.notification_item, pushNotifications);
//                    listView.setAdapter(adapter);
//                }
//            }
//        }.execute();
//    }
//
//    @Override
//    public void onClick(View v) {
//        super.onClick(v);
//    }
    }
}
