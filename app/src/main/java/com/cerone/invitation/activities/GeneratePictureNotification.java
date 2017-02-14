package com.cerone.invitation.activities;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.cerone.invitation.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by suzuki on 02-06-2016.
 */
public class GeneratePictureNotification extends AsyncTask<String, Void, Bitmap> {

    private Context mContext;
    private String title, message, imageUrl;

    public GeneratePictureNotification(Context context, String title, String message, String imageUrl) {
        super();
        this.mContext = context;
        this.title = title;
        this.message = message;
        this.imageUrl = imageUrl;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        if (this.imageUrl != null && !this.imageUrl.isEmpty()) {
            InputStream in;
            try {
                URL url = new URL(this.imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        Intent intent = new Intent(mContext, HomeScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);
        int messageCount = 0;
        if (message.contains("#")) {
            messageCount = Integer.parseInt(message.split("#")[0]);
            message = message.substring(message.indexOf("#") + 1);
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.home)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true).setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        final NotificationCompat.BigPictureStyle inboxStyle = new NotificationCompat.BigPictureStyle();
        inboxStyle.setSummaryText(message);
        inboxStyle.bigPicture(result);
        notificationBuilder.setStyle(inboxStyle);
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(999 /* ID of notification */, notificationBuilder.build());
        //NotificationsHelper.notifyListeners(messageCount);
    }
}