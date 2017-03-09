/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package com.cerone.invitation.helpers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.WindowManager;

import com.cerone.invitation.R;
import com.cerone.invitation.exception.InvtAppJSONException;
import com.example.utills.HTTPUtils;


/**
 * @author cerone
 * @version 1.0, Apr 14, 2015
 */
public abstract class InvtAppAsyncTask extends AsyncTask<String, Void, String> {

    Context context;
    boolean showProgress = true;
    String progressMessage = "Loading....";
    public static String FAILURE = "Failure";
    public static String SUCCESS = "Success";
    ProgressDialog progressDialog;

    public InvtAppAsyncTask(Context context) {
        super();
        this.context = context;
    }

    @Override
    protected String doInBackground(String... urls) {
        String result = FAILURE;
        if(MobileHelper.isOnline(context)) {
            try {
                process();
                result = SUCCESS;
            } catch (Exception exception) {
                Log.e("Application", exception.getMessage(), exception);
                ToastHelper.redToast(context, "Internal error occured.");
            }
        }
        return result;
    }

    public abstract void process();

    public abstract void afterPostExecute();

    @Override
    protected void onPostExecute(String result) {
        /*if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }*/
        try {
            if ((this.progressDialog != null) && this.progressDialog.isShowing()) {
                this.progressDialog.dismiss();
            }
        } catch (final IllegalArgumentException e) {
            // Handle or log or ignore
            e.printStackTrace();
        } catch (final Exception e) {
            // Handle or log or ignore
            e.printStackTrace();
        } finally {
            this.progressDialog = null;
        }
        ToastHelper.processPendingToast();
        afterPostExecute();
    }

    @Override
    protected void onPreExecute() {
        Log.d("PresswaaleAsyncTask", "onPreExecuteStart");
        if(progressDialog !=null)
        {
            progressDialog = null;
        }
        if (showProgress) {
            progressDialog = createProgressDialog(context);
            progressDialog.show();
        }
        Log.d("PresswaaleAsyncTask", "onPreExecuteEnd");
        ToastHelper.delayToasting();
    }

    public boolean isShowProgress() {
        return showProgress;
    }

    public InvtAppAsyncTask setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
        return this;
    }

    public String getProgressMessage() {
        return progressMessage;
    }

    public void setProgressMessage(String progressMessage) {
        this.progressMessage = progressMessage;
    }
    public static ProgressDialog createProgressDialog(Context mContext) {
        ProgressDialog dialog = new ProgressDialog(mContext);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {

        }
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.progressdialoglayout);
        return dialog;
    }
}
