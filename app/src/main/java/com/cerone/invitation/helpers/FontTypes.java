package com.cerone.invitation.helpers;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by peekay on 10/2/17.
 */

public class FontTypes {

    public static Typeface tfBold, tfRegular;


    public FontTypes(Context context) {
        tfRegular = Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Light.otf");
        tfBold = Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Medium.otf");
    }

    public void setTypeface(TextView view, Typeface fotntType) {
        view.setTypeface(fotntType);
    }

    public void setTypeface(View... view) {
        for (View textview : view) {
            if (textview != null)
                ((TextView) textview).setTypeface(tfRegular);
        }
    }

    public void setBoldTypeface(View... view) {
        for (View textview : view) {
            if (textview != null)
                ((TextView) textview).setTypeface(tfBold);
        }
    }

    public void setTypeface(EditText... view) {
        for (EditText editText : view) {
            editText.setTypeface(tfRegular);
        }
    }

    public void setRegularFontToTextViews(TextView... view) {
        for (TextView textView : view) {
            textView.setTypeface(tfRegular);
        }
    }

    public void setBoldFontToTextViews(TextView... view) {
        for (TextView textView : view) {
            textView.setTypeface(tfRegular);
        }
    }


    public static void setEditTextRegularFont(EditText... view) {
        for (EditText button : view) {
            button.setTypeface(tfRegular);
        }
    }
}
