package com.cerone.invitation.helpers;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by peekay on 22/3/17.
 */

public abstract class HappeningTextWatcher implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    public abstract void afterTextChanged(Editable s);
}