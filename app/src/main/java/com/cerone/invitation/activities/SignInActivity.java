package com.cerone.invitation.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cerone.invitation.R;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.cerone.invitation.helpers.ToastHelper;
import com.example.dataobjects.CurrencyTypes;
import com.example.dataobjects.ServerResponse;
import com.example.syncher.BaseSyncher;
import com.example.syncher.CurrencyTypesSyncher;
import com.example.syncher.UserSyncher;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.List;

public class SignInActivity extends BaseActivity implements OnClickListener {

    TextView sendAgain;
    EditText name, mobileNumber, otpNumber;
    Button buttonCountryCode, buttonOtp, buttonVerify;
    LinearLayout layoutOtp;
    List<CurrencyTypes> currencyTypes;
    String otpResult;
    UserSyncher userSyncher = new UserSyncher();
    ServerResponse serverResponse;
    boolean alreadyLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        setFontType(R.id.edit_name,R.id.button_countryCode,R.id.edit_mobileNumber,R.id.button_generateOtp,R.id.txt_otp,R.id.edit_otpNumber,
                R.id.txt_sendAgain,R.id.button_verify);
        InvtAppPreferences.setPref(getApplicationContext());
        alreadyLoggedIn = InvtAppPreferences.isLoggedIn();
        InvtAppPreferences.setServiceRefresh(false);
        if (alreadyLoggedIn) {
            String token = InvtAppPreferences.getAccessToken();
            if(token!=null && !token.isEmpty() && token.length() >10) {
                BaseSyncher.setAccessToken(InvtAppPreferences.getAccessToken());
                System.out.println("Existing access Token " + BaseSyncher.getAccessToken());
                Intent intent = new Intent(getApplicationContext(), HomeScreenActivity.class);
                startActivity(intent);
                finish();
            }else {
                InvtAppPreferences.reset();
            }
        }
        name = (EditText) findViewById(R.id.edit_name);
        mobileNumber = (EditText) findViewById(R.id.edit_mobileNumber);
        otpNumber = (EditText) findViewById(R.id.edit_otpNumber);
        sendAgain = (TextView) findViewById(R.id.txt_sendAgain);
        buttonCountryCode = (Button) findViewById(R.id.button_countryCode);
        buttonOtp = (Button) findViewById(R.id.button_generateOtp);
        buttonVerify = (Button) findViewById(R.id.button_verify);
        layoutOtp = (LinearLayout) findViewById(R.id.layout_otp);
        buttonCountryCode.setOnClickListener(this);
        buttonOtp.setOnClickListener(this);
        buttonVerify.setOnClickListener(this);
        sendAgain.setOnClickListener(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        loadCountryCodeDetails();
        checkUserPermissions();
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
                    buttonCountryCode.setText(currencyCountryCodeDetails.get(index).getCountryCode());
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
        switch (v.getId()) {
            case R.id.button_generateOtp:
                if(validateInputDetails(mobileNumber)){
                    setSendOtp(buttonCountryCode.getText().toString() + mobileNumber.getText().toString());
                }
                break;
            case R.id.button_verify:
                if (validateInputDetails(name,mobileNumber,otpNumber)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    new InvtAppAsyncTask(SignInActivity.this) {
                                @Override
                                public void process() {
                                    serverResponse = userSyncher.getSignInWithMobileAndOtp(buttonCountryCode.getText().toString()+mobileNumber.getText().toString(), otpNumber.getText().toString(), name.getText().toString());
                                    if(serverResponse!=null && serverResponse.getToken()!=null){
                                        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                                        Log.i("TAG", "FCM Registration Token: " + refreshedToken);
                                        BaseSyncher.setAccessToken(serverResponse.getToken());
                                        userSyncher.updateGcmCode(refreshedToken);
                                    }
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
                        }else{
                            if(otpNumber.getText().toString().isEmpty()){
                                otpNumber.requestFocus();
                            }
                            Toast.makeText(getApplicationContext(), "Fields should't be empty", Toast.LENGTH_LONG).show();
                        }
                break;
            case R.id.button_countryCode:
                getCountrycodeDialog(buttonCountryCode, mobileNumber, SignInActivity.this);
                mobileNumber.requestFocus();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                break;
            case R.id.txt_sendAgain:
                setSendOtp(buttonCountryCode.getText().toString() + mobileNumber.getText().toString());
                break;

        }
    }

    void setSendOtp(final String mobileNumber) {
        new InvtAppAsyncTask(SignInActivity.this) {
            @Override
            public void process() {
                otpResult = userSyncher.getOtp(mobileNumber);
                Log.d("OTP Result", otpResult);
            }

            @Override
            public void afterPostExecute() {
                if (otpResult!=null) {
                    if (otpResult.equals("Success")) {
                        Toast.makeText(getApplicationContext(), "Please enter otp", Toast.LENGTH_LONG).show();
                        layoutOtp.setVisibility(View.VISIBLE);
                        otpNumber.requestFocus();
                    } else {
                        ToastHelper.yellowToast(SignInActivity.this, otpResult);
                    }
                }
            }
        }.execute();
    }

    @Override
    public void onBackPressed() {
        doExit();
    }

}