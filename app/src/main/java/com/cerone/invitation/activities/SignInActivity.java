package com.cerone.invitation.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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

import static com.cerone.invitation.R.id.headerBack;


public class SignInActivity extends BaseActivity implements OnClickListener {

    EditText mobileNumber;
    Button signIn_button;
    String mobileNumberinString = "";
    TextInputLayout inputLayoutEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        addToolbarView();
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.mobileNumberlayout);
        mobileNumber = (EditText) findViewById(R.id.editMobileNumber);
        signIn_button = (Button) findViewById(R.id.signIn);
        signIn_button.setOnClickListener(this);
        mobileNumber.addTextChangedListener(new InvtTextWatcher(mobileNumber, inputLayoutEmail, "Mobile number should not be empty."));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void onClick(View v) {
        mobileNumberinString = mobileNumber.getText().toString();
        switch (v.getId()) {
            case R.id.signIn :
                if (mobileNumberinString.length() > 0) {
                    if (MobileHelper.hasNetwork(getApplicationContext(), SignInActivity.this, " to login", null)) {
                        new InvtAppAsyncTask(this) {

                            ServerResponse serverResponse;

                            @Override
                            public void process() {
                                UserSyncher userSyncher = new UserSyncher();
                                serverResponse = userSyncher.getAccesToken(mobileNumberinString);
                            }

                            @Override
                            public void afterPostExecute() {
                                if (serverResponse != null) {
                                    if (serverResponse.getToken() != null) {
                                        Toast.makeText(getApplicationContext(), "Successfully logged in.", Toast.LENGTH_LONG).show();
                                        setLoginDetails(serverResponse);
                                        Intent intent = new Intent(getApplicationContext(), HomeScreenActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    } else
                                        ToastHelper.redToast(getApplicationContext(), serverResponse.getStatus());
                                } else {
                                    ToastHelper.redToast(getApplicationContext(), "oops something went wrong");
                                }
                            }
                        }.execute();
                    }
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //menu.findItem(R.id.action_settings).setVisible(false);
        return true;
    }
}