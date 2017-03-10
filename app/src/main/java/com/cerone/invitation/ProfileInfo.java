package com.cerone.invitation;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cerone.invitation.activities.BaseActivity;

public class ProfileInfo extends BaseActivity {

    TextView profileName, profileMobileNum, profileEmail;
    ImageView profileImage;
    String name, mobileNumber, email, image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);
        addToolbarView();
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        collapsingToolbar.setTitle("Profile Info");
        setFontType(R.id.profileInfo_name,R.id.profileInfo_pokehim,R.id.profileInfo_contactNumber,R.id.profileInfo_emailId);

        profileName = (TextView) findViewById(R.id.profileInfo_name);
        profileMobileNum = (TextView) findViewById(R.id.profileInfo_contactNumber);
        profileEmail = (TextView) findViewById(R.id.profileInfo_emailId);
        profileImage = (ImageView) findViewById(R.id.header);

        name = getIntent().getExtras().getString("UserName");
        mobileNumber = getIntent().getExtras().getString("UserMobile");
        email = getIntent().getExtras().getString("UserEmail");
        image = getIntent().getExtras().getString("UserImage");

        profileName.setText(name);
        profileMobileNum.setText("Contact Number: "+mobileNumber);
        profileEmail.setText("Email ID: "+email);
        profileImage.setBackground(Drawable.createFromPath(image));

    }
}
