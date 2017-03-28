package com.cerone.invitation;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.cerone.invitation.activities.BaseActivity;
import com.squareup.picasso.Picasso;

public class ProfileInfo extends BaseActivity {

    TextView profileName, profileMobileNum, profileEmail;
    ImageView profileImage;
    String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile_info);
        addToolbarView();
        setFontType(R.id.profileInfo_name, R.id.profileInfo_contactNumber,R.id.profileInfo_emailId, R.id.txt_phoneNumber, R.id.txt_email);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                TextView title = (TextView) findViewById(R.id.toolbar_title);
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    title.setVisibility(View.VISIBLE);
                    toolbar.setBackground(getResources().getDrawable(R.drawable.my_theme));
                    isShow = true;
                } else if(isShow) {
                    title.setVisibility(View.INVISIBLE);
                    toolbar.getBackground().setAlpha(0);
                    isShow = false;
                }
            }
        });

        profileName = (TextView) findViewById(R.id.profileInfo_name);
        profileMobileNum = (TextView) findViewById(R.id.profileInfo_contactNumber);
        profileEmail = (TextView) findViewById(R.id.profileInfo_emailId);
        profileImage = (ImageView) findViewById(R.id.header);

        image = getIntent().getExtras().getString("UserImage");

        profileName.setText(getIntent().getExtras().getString("UserName"));
        profileMobileNum.setText(getIntent().getExtras().getString("UserMobile"));
        profileEmail.setText(getIntent().getExtras().getString("UserEmail"));
        if(image!= null && image.length()>0) {
            Picasso.with(getApplicationContext()).load(image).into(profileImage);
        }

    }
}
