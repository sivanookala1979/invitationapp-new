package com.cerone.invitation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cerone.invitation.R;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtTextWatcher;
import com.cerone.invitation.helpers.MobileHelper;
import com.cerone.invitation.helpers.ToastHelper;
import com.example.dataobjects.*;
import com.example.syncher.UserSyncher;


public class RegistrationActivity extends BaseActivity implements OnClickListener {

    EditText userName, userMobileNumber;
    Button register;
    ImageButton headerBack;
    TextInputLayout mobileNumberInput, userNameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_layout);
        userName = (EditText) findViewById(R.id.editUserName);
        mobileNumberInput = (TextInputLayout) findViewById(R.id.mobileNumberInput);
        userNameInput = (TextInputLayout) findViewById(R.id.userNameInput);
        userMobileNumber = (EditText) findViewById(R.id.editMobileNumber);
        headerBack = (ImageButton) findViewById(R.id.headerBack);
        headerBack.setOnClickListener(this);
        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(this);
        userMobileNumber.addTextChangedListener(new InvtTextWatcher(userMobileNumber, mobileNumberInput, "Mobile number should not be empty."));
        userName.addTextChangedListener(new InvtTextWatcher(userName, userNameInput, "User name should not be empty."));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register :
                String name,
                mobileNumber;
                name = userName.getText().toString();
                mobileNumber = userMobileNumber.getText().toString();
                if (name.length() > 0 && mobileNumber.length() >= 10) {
                    final User newUser = new User();
                    newUser.setUserName(name);
                    newUser.setPhoneNumber(mobileNumber);
                    if (MobileHelper.hasNetwork(getApplicationContext(), RegistrationActivity.this, " to register", null)) {
                        new InvtAppAsyncTask(this) {

                            ServerResponse serverResponse;

                            @Override
                            public void process() {
                                UserSyncher userSyncher = new UserSyncher();
                                serverResponse = userSyncher.createUser(newUser);
                            }

                            @Override
                            public void afterPostExecute() {
                                if (serverResponse.getId() != null) {
                                    setLoginDetails(serverResponse);
                                    Toast.makeText(getApplicationContext(), "Successfully Registered.", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), HomeScreenActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                } else
                                    ToastHelper.redToast(getApplicationContext(), serverResponse.getStatus());
                            }
                        }.execute();
                    }
                } else
                    ToastHelper.redToast(getApplicationContext(), "Please enter valid data. ");
                break;
            case R.id.headerBack :
                finish();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // menu.findItem(R.id.action_settings).setVisible(false);
        return true;
    }
}