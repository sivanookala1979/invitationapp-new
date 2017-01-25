/*
 * (c) Copyright 2001-2016 COMIT AG
 * All Rights Reserved.
 */
package com.cerone.invitation.helpers;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * @author Adarsh.T
 * @version 1.0, 08-Mar-2016
 */
public class InvtTextWatcher implements TextWatcher {

    EditText view;
    TextInputLayout textInputLayout;
    String errorMessage;

    public InvtTextWatcher(EditText view, TextInputLayout textInputLayout, String errorMessage) {
        this.view = view;
        this.textInputLayout = textInputLayout;
        this.errorMessage = errorMessage;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    private boolean validateText() {
        String data = view.getText().toString();
        textInputLayout.setErrorEnabled(true);
        if (data.isEmpty()) {
            textInputLayout.setError(errorMessage);
        } else {
            textInputLayout.setError("");
        }
        return true;
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!errorMessage.isEmpty())
            validateText();
    }
}
