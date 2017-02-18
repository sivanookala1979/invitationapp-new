package com.cerone.invitation.fcm;

import android.util.Log;

import com.example.syncher.UserSyncher;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by adarsht on 22/11/16.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    private void sendRegistrationToServer(String token) {
        Log.d("Send token", "Token " + token);
        Log.d("Send token", "callingUpdateJsun ");
        UserSyncher syncher = new UserSyncher();
        syncher.updateGcmCode(token);
        Log.d("Send token", "After call ");
    }
}
