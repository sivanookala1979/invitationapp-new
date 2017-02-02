package com.cerone.invitation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.cerone.invitation.R;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.example.syncher.BaseSyncher;


public class LoginActivity extends BaseActivity implements OnClickListener {

    Button loginButton, registerButton;
    boolean alreadyLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        InvtAppPreferences.setPref(getApplicationContext());
        alreadyLoggedIn = InvtAppPreferences.isLoggedIn();
        InvtAppPreferences.setServiceRefresh(false);
        if (alreadyLoggedIn) {
            BaseSyncher.setAccessToken(InvtAppPreferences.getAccessToken());
            System.out.println("Existing access Token " + BaseSyncher.getAccessToken());
            Intent intent = new Intent(getApplicationContext(), HomeScreenActivity.class);
            startActivity(intent);
            finish();
        }
        loginButton = (Button) findViewById(R.id.loginButton);
        registerButton = (Button) findViewById(R.id.registerButton);
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.loginButton :
                intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
                break;
            case R.id.registerButton :
                intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(intent);
                break;
            default :
                break;
        }
    }

    @Override
    public void onBackPressed() {
        doExit();
    }
}
