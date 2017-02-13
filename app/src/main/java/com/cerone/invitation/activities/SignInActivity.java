package com.cerone.invitation.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cerone.invitation.R;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.cerone.invitation.helpers.InvtTextWatcher;
import com.cerone.invitation.helpers.ToastHelper;
import com.example.dataobjects.CurrencyTypes;
import com.example.dataobjects.ServerResponse;
import com.example.syncher.BaseSyncher;
import com.example.syncher.CurrencyTypesSyncher;
import com.example.syncher.UserSyncher;

import java.util.List;


public class SignInActivity extends BaseActivity implements OnClickListener {

    EditText mobileNumber, otpNumber;
    Button signInButton, countryCode;
    String mobileNum = "";
    TextInputLayout inputLayoutEmail, otpNumberlayout;
    List<CurrencyTypes> currencyTypes;
    String otpResult;
    ServerResponse serverResponse;
    boolean alreadyLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
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
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.mobileNumberlayout);
        otpNumberlayout = (TextInputLayout) findViewById(R.id.otpNumberlayout);
        mobileNumber = (EditText) findViewById(R.id.editMobileNumber);
        otpNumber = (EditText) findViewById(R.id.editOtpNumber);
        signInButton = (Button) findViewById(R.id.signIn_button);
        countryCode = (Button) findViewById(R.id.country_code);
        signInButton.setOnClickListener(this);
        countryCode.setOnClickListener(this);
        mobileNumber.addTextChangedListener(new InvtTextWatcher(mobileNumber, inputLayoutEmail, "Mobile number should not be empty."));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        loadCountryCodeDetails();
    }

    public void loadCountryCodeDetails() {
        new InvtAppAsyncTask(SignInActivity.this) {
            @Override
            public void process() {
                CurrencyTypesSyncher currencyTypesSyncher = new CurrencyTypesSyncher(SignInActivity.this);
                currencyTypes = currencyTypesSyncher.getAllCurrencyTypes();
            }

            @Override
            public void afterPostExecute() {

                InvtAppPreferences.setCurrencyCountryCodeDetails(currencyTypes);
                getDefaultCountryCode();

            }
        }.execute();
    }

    public void getDefaultCountryCode() {
        if (InvtAppPreferences.getCurrencyCountryCodeDetails().size() > 0) {
            final TelephonyManager tm = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            int index = getCountryIndex(tm.getNetworkCountryIso(), InvtAppPreferences.getCurrencyCountryCodeDetails());
            List<CurrencyTypes> currencyCountryCodeDetails = InvtAppPreferences.getCurrencyCountryCodeDetails();

            InputFilter[] FilterArray = new InputFilter[1];
            FilterArray[0] = new InputFilter.LengthFilter(Integer
                    .parseInt(currencyCountryCodeDetails.get(index).getMobileNumberCount()));
            mobileNumber.setFilters(FilterArray);

            int simState = tm.getSimState();
            switch (simState) {
                case TelephonyManager.SIM_STATE_ABSENT:
                    Toast.makeText(getApplicationContext(), "please check your mobile network.", Toast.LENGTH_LONG).show();
                    break;
                case TelephonyManager.SIM_STATE_READY:
                    countryCode.setText(currencyCountryCodeDetails.get(index).getCountryCode());
                    break;
                case TelephonyManager.SIM_STATE_UNKNOWN:
                    Toast.makeText(getApplicationContext(), "please check your mobile network.", Toast.LENGTH_LONG).show();
                    // do something
                    break;
            }
        }
    }


    @Override
    public void onClick(View v) {
        mobileNum = mobileNumber.getText().toString();
        switch (v.getId()) {
            case R.id.signIn_button:
                if (mobileNum != null && !mobileNum.isEmpty()) {
                    if (signInButton.getText().toString().equalsIgnoreCase("OTP")) {
                        setSendOtp(countryCode.getText().toString() + mobileNum);
                    } else if (signInButton.getText().toString().equalsIgnoreCase("LOGIN")) {
                        String otpNum = otpNumber.getText().toString();
                        if (mobileNum != null && !mobileNum.isEmpty() && otpNum != null && !otpNum.isEmpty()) {
                            new InvtAppAsyncTask(SignInActivity.this) {
                                @Override
                                public void process() {
                                    UserSyncher syncher = new UserSyncher();
                                    serverResponse = syncher.getSignInWithMobileAndOtp(countryCode.getText().toString() + mobileNum, otpNumber.getText().toString());
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
                }

                    break;
                    case R.id.country_code:
                        getCountrycodeDialog(countryCode, mobileNumber, SignInActivity.this);
                        mobileNumber.requestFocus();
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        break;

                }
        }

     void setSendOtp(final String mobileNumber) {
            new InvtAppAsyncTask(SignInActivity.this) {
                @Override
                public void process() {

                    UserSyncher syncher = new UserSyncher();
                    otpResult = syncher.getOtp(mobileNumber);
                    Log.d("OTP Result", otpResult);
                }

                @Override
                public void afterPostExecute() {

                    if (otpResult.equals("Success")) {
                        Toast.makeText(getApplicationContext(), "Please enter otp", Toast.LENGTH_LONG).show();
                        otpNumberlayout.setVisibility(View.VISIBLE);
                        otpNumber.requestFocus();
                        signInButton.setText("Login");
                    }else {
                        ToastHelper.yellowToast(SignInActivity.this, otpResult);
                    }
                }
            }.execute();
        }
    @Override
    public void onBackPressed() {
        doExit();
    }

    }