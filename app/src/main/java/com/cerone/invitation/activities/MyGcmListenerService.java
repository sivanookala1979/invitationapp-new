package com.cerone.invitation.activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.cerone.invitation.R;
import com.google.android.gms.gcm.GcmListenerService;

import java.util.ArrayList;
import java.util.List;

public class MyGcmListenerService extends GcmListenerService {
    static int notificationCount = 0;
    static int notificationChatCount = 0;
    static int supportNotificationCount = 0;
    private static final String TAG = "MyGcmListenerService";
    final static String GROUP_KEY = "group_key_notification";
    final static String GROUP_CHAT_KEY = "group_chat_key_notification";
    final static String GROUP_SUPPORT_CHAT_KEY = "group_support_chat_key_notification";
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String CHAT_MESSAGE_RECEIVED = "chatMessageReceived";
    int fromuserId = 0;
    private int notificationId = 100;
    private int notificationChatId = 200;
    private int supportNotificationId = 300;
    static List<String> messageList = new ArrayList<String>();
    static List<String> chatList = new ArrayList<String>();
    static List<String> supportChatList = new ArrayList<String>();
    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        String title = data.getString("title");
        String imagePath = data.getString("image_url");
        Log.d(TAG, "bundel " + data);
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);
        Log.d(TAG, "Title: " + title);
        Log.d(TAG, "ImagePath: " + imagePath);
        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        if (data.containsKey("support_message")) {
            Intent pushNotification = new Intent(CHAT_MESSAGE_RECEIVED);
            pushNotification.putExtra("support_message", data.getString("support_message"));
            pushNotification.putExtra("from_user_id", data.getString("from_user_id"));
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
        }

        if (data.getString("from_user_id") != null && data.getString("from_user_id").length() > 0) {
            fromuserId = Integer.parseInt(data.getString("from_user_id"));

        }

        if (title.equalsIgnoreCase("notification")) {
            sendNotification(message);
        } else if (title.equalsIgnoreCase("Promotion")) {
            new GeneratePictureNotification(this, title, message, imagePath).execute();
        } else if (title.equalsIgnoreCase("Chat")) {
            if (!IntraChatActivity.isActive) {
                if (fromuserId > 0)
                    sendChatNotification(message, fromuserId);
            }
        } else if (title.equalsIgnoreCase("Support Chat")) {
            if (!SupportChatActivity.isActive) {
                sendSupportNotification(message, fromuserId);
            }

        }
        // [END_EXCLUDE]
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
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
                .setSmallIcon(R.drawable.home)
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
        Log.d("notification count" ,message);
        NotificationsHelper.notifyListeners(messageCount);
    }

    private void sendNotification(String message) {

        Intent intent = new Intent(this, NotificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);
        int messageCount = 0;
        if (message.contains("#")) {
            messageCount = Integer.parseInt(message.split("#")[0]);
            message = message.substring(message.indexOf("#") + 1);
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.home)
                .setContentTitle("You have " + ++notificationCount + " new notifications")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent).setGroup(GROUP_KEY).setGroupSummary(true);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle("You have " + notificationCount + " new notifications");
        messageList.add(message);
        for (int i = 0; i < messageList.size(); i++) {
            inboxStyle.addLine(messageList.get(i));
        }
        notificationBuilder.setStyle(inboxStyle);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());
        NotificationsHelper.notifyListeners(messageCount);
    }
    private void sendSupportNotification(String message, int userId) {
        Intent intent;
        intent = new Intent(this, SupportChatActivity.class);
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
                .setSmallIcon(R.drawable.home)
                .setContentTitle("You have " + ++supportNotificationCount + " Support chat messages")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent).setGroup(GROUP_SUPPORT_CHAT_KEY).setGroupSummary(true);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle("You have " + supportNotificationCount + " Support chat messages");
        supportChatList.add(message);
        for (int i = 0; i < supportChatList.size(); i++) {
            inboxStyle.addLine(supportChatList.get(i));
        }
        notificationBuilder.setStyle(inboxStyle);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(supportNotificationId /* ID of chat */, notificationBuilder.build());
        NotificationsHelper.notifyListeners(messageCount);
    }
}