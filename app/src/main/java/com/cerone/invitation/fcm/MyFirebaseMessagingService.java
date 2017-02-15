package com.cerone.invitation.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.cerone.invitation.R;
import com.cerone.invitation.activities.GeneratePictureNotification;
import com.cerone.invitation.activities.HomeScreenActivity;
import com.cerone.invitation.activities.NotificationsHelper;
import com.cerone.invitation.activities.SupportChatActivity;
import com.cerone.invitation.activities.chat.IntraChatActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.cerone.invitation.helpers.InvtAppPreferences.getPref;
import static com.google.android.gms.analytics.internal.zzy.e;

/**
 * Created by adarsht on 22/11/16.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    private static int count = 0;

    static int notificationCount = 0;
    static int notificationChatCount = 0;
    static int supportNotificationCount = 0;
    //private static final String TAG = "MyGcmListenerService";
    final static String GROUP_KEY = "group_key_notification";
    final static String GROUP_CHAT_KEY = "group_chat_key_notification";
    final static String GROUP_SUPPORT_CHAT_KEY = "group_support_chat_key_notification";
    int fromuserId = 0;
    private int notificationId = 100;
    private int notificationChatId = 200;
    private int supportNotificationId = 300;
    static List<String> messageList = new ArrayList<String>();
    static List<String> chatList = new ArrayList<String>();
    static List<String> supportChatList = new ArrayList<String>();


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            try {
                JSONObject data = new JSONObject(remoteMessage.getData());
                if(data!= null) {
                    String message = data.getString("message");
                    if (data.has("support_message")) {
                        Intent pushNotification = new Intent(QuickstartPreferences.CHAT_MESSAGE_RECEIVED);
                        pushNotification.putExtra("support_message", data.getString("support_message"));
                        pushNotification.putExtra("from_user_id", data.getString("from_user_id"));
                        pushNotification.putExtra("message", data.getString("message"));
                        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                    }

                    if (data.getString("from_user_id") != null && data.getString("from_user_id").length() > 0) {
                        fromuserId = Integer.parseInt(data.getString("from_user_id"));

                    }
                    String title = data.getString("title");
                    if (title.equalsIgnoreCase("Chat")) {
                        if (!IntraChatActivity.isActive) {
                            if (fromuserId > 0)
                                sendChatNotification(message, fromuserId);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }
        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//            sendNotification(remoteMessage.getNotification().getBody(),0);
//        }
    }

    private void sendChatNotification(String message, int userId) {
        Intent intent;
        intent = new Intent(this, IntraChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("UserId", userId);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);
        int messageCount = 0;
        if (message.contains("#")) {
            messageCount = Integer.parseInt(message.split("#")[0]);
            message = message.substring(message.indexOf("#") + 1);
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("You have " + ++notificationChatCount + " chat messages")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent).setGroup(GROUP_CHAT_KEY).setGroupSummary(true);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle("You have " + notificationChatCount + " chat messages");
        chatList.add(message);
        for (int i = 0; i < chatList.size(); i++) {
            inboxStyle.addLine(chatList.get(i));
        }
        notificationBuilder.setStyle(inboxStyle);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationChatId /* ID of chat */, notificationBuilder.build());
        Log.d("notification count", message);
        NotificationsHelper.notifyListeners(messageCount);
    }

    private void sendNotification(String messageBody, int orderId) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent = new Intent(this, HomeScreenActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, count, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Invitation")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(count, notificationBuilder.build());
        count++;
    }
}
