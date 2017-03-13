package com.cerone.invitation;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cerone.invitation.activities.BaseActivity;
import com.squareup.picasso.Picasso;

public class ProfileInfo extends BaseActivity {

    TextView profileMobileNum, profileEmail;
    ImageView profileImage;
    String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);
        addToolbarView();

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        collapsingToolbar.setTitle(getIntent().getExtras().getString("UserName"));
        final Typeface tf = Typeface.createFromAsset(this.getAssets(), "fonts/AvenirNextLTPro-Regular.otf");
        collapsingToolbar.setCollapsedTitleTypeface(tf);
        collapsingToolbar.setExpandedTitleTypeface(tf);
        setFontType(R.id.profileInfo_contactNumber,R.id.profileInfo_emailId);

        profileMobileNum = (TextView) findViewById(R.id.profileInfo_contactNumber);
        profileEmail = (TextView) findViewById(R.id.profileInfo_emailId);
        profileImage = (ImageView) findViewById(R.id.header);

        image = getIntent().getExtras().getString("UserImage");

        profileMobileNum.setText("Contact Number: "+getIntent().getExtras().getString("UserMobile"));
        profileEmail.setText("Email ID: "+getIntent().getExtras().getString("UserEmail"));
        if(image!= null & image.length()>0) {
            Picasso.with(getApplicationContext()).load(image).into(profileImage);
        }

    }
}
