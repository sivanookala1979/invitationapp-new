package com.cerone.invitation.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.cerone.invitation.R;
import com.cerone.invitation.adapter.NotificationAdapter;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.ToastHelper;
import com.example.dataobjects.Notifications;
import com.example.dataobjects.SaveResult;
import com.example.syncher.NotificationSyncher;

public class NotificationsActivity extends BaseActivity {

    ImageButton refresh;
    TextView clear;
    Notifications notifications = new Notifications();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        addToolbarView();
        clear = (TextView) findViewById(R.id.toolbar_clear);
        refresh = (ImageButton) findViewById(R.id.refresh_notfication);
        clear.setOnClickListener(this);
        refresh.setOnClickListener(this);
        showSavedNotifications();
    }

    private void showSavedNotifications(){
        new InvtAppAsyncTask(NotificationsActivity.this) {

            @Override
            public void process() {
                NotificationSyncher syncher = new NotificationSyncher();
                notifications = syncher.getAllNotificatons();
            }

            @Override
            public void afterPostExecute() {
                if (notifications != null) {
                    NotificationAdapter adapter = new NotificationAdapter(NotificationsActivity.this, notifications.getNotifications());
                    final ListView listView = (ListView) findViewById(R.id.notificationsListView);
                    listView.setAdapter(adapter);
                }else{
                    ToastHelper.yellowToast(NotificationsActivity.this, "Error Occured..");
                }
            }

        }.execute();
    }

    private void clearNotifications(){
        new InvtAppAsyncTask(NotificationsActivity.this) {
            SaveResult saveResult;

            @Override
            public void process() {
                NotificationSyncher syncher = new NotificationSyncher();
                saveResult = syncher.clearNotifications();
            }

            @Override
            public void afterPostExecute() {
                if (saveResult != null&&saveResult.isSuccess()) {
                    showSavedNotifications();
                    ToastHelper.yellowToast(NotificationsActivity.this, saveResult.getStatus());
                }
            }

        }.execute();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v.getId() == R.id.refresh_notfication){
            showSavedNotifications();
        }
        if(v.getId() == R.id.toolbar_clear){
            clearNotifications();
        }
    }
}
