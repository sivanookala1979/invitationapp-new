package com.cerone.invitation.helpers;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by adarsht on 31/03/17.
 */

public class CeroneTextView extends TextView {

    public CeroneTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CeroneTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CeroneTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        String fontType = String.valueOf(getTag());
        if(fontType!=null) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(),fontType);
            if(tf!=null) {
                setTypeface(tf);
            }
        }
    }
}

