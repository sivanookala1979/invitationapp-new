/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package com.cerone.invitation.helpers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.cerone.invitation.exception.InvtAppJSONException;


/**
 * @author cerone
 * @version 1.0, Apr 14, 2015
 */
public abstract class InvtAppAsyncTask extends AsyncTask<String, Void, String> {

    Activity baseActivity;
    boolean showProgress = true;
    String progressMessage = "Loading....";
    public static String FAILURE = "Failure";
    public static String SUCCESS = "Success";
    ProgressDialog progressDialog;

    public InvtAppAsyncTask(Activity baseActivity) {
        super();
        this.baseActivity = baseActivity;
        // baseActivity.executingTask(this);
    }

    @Override
    protected String doInBackground(String... urls) {
        String result = FAILURE;
        try {
            process();
            result = SUCCESS;
        } catch (InvtAppJSONException jsonException) {
            Log.e("Application", jsonException.getCustomMessage(), jsonException);
            ToastHelper.redToast(baseActivity, "Application Data Exchange Failed.");
        } catch (Exception exception) {
            Log.e("Application", exception.getMessage(), exception);
            ToastHelper.redToast(baseActivity, "oops something went wrong.");
        }
        return result;
    }

    public abstract void process();

    public abstract void afterPostExecute();

    @Override
    protected void onPostExecute(String result) {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        } catch (Exception exception) {
            InvtAppLog.e("FAILED to close the dailog :", exception.getMessage());
        }
        ToastHelper.processPendingToast();
        afterPostExecute();
    }

    @Override
    protected void onPreExecute() {
        InvtAppLog.d("VNAsyncTask", "onPreExecuteStart");
        if (showProgress) {
            progressDialog = ProgressDialog.show(baseActivity, "", progressMessage);
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
        }
        InvtAppLog.d("VNAsyncTask", "onPreExecuteEnd");
        ToastHelper.delayToasting();
    }

    public boolean isShowProgress() {
        return showProgress;
    }

    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
    }

    public String getProgressMessage() {
        return progressMessage;
    }

    public void setProgressMessage(String progressMessage) {
        this.progressMessage = progressMessage;
    }
}
