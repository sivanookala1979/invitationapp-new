package com.cerone.invitation;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.ColorUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cerone.invitation.activities.BaseActivity;
import com.cerone.invitation.adapter.ImageDialogAdapter;
import com.cerone.invitation.helpers.CircleTransform;
import com.cerone.invitation.helpers.InvtAppAsyncTask;
import com.cerone.invitation.helpers.InvtAppPreferences;
import com.cerone.invitation.helpers.ToastHelper;
import com.example.dataobjects.User;
import com.example.syncher.UserSyncher;
import com.example.utills.HTTPUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserProfile extends BaseActivity implements View.OnClickListener {
    TextView editImage;
    EditText name, phone, status;
    ImageView profileImage;
    Button updateProfile;
    RadioButton radioButtonMale, radioButtonFemale;
    User userDetails;
    String customerImageBitmapToString;
    private static final int GALLERY_REQUET_CODE = 11111;
    private static final int CAMERA_REQUEST_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        addToolbarView();
        setFontType(R.id.txt_name, R.id.txt_phone, R.id.txt_status, R.id.txt_gender, R.id.profile_name, R.id.profile_phone, R.id.profile_status, R.id.register, R.id.edit_image);
        editImage = (TextView) findViewById(R.id.edit_image);
        name = (EditText) findViewById(R.id.profile_name);
        phone = (EditText) findViewById(R.id.profile_phone);
        status = (EditText) findViewById(R.id.profile_status);
        profileImage = (ImageView) findViewById(R.id.capture_image);
        radioButtonMale = (RadioButton) findViewById(R.id.radioMale);
        radioButtonFemale = (RadioButton) findViewById(R.id.radioFemale);
        updateProfile = (Button) findViewById(R.id.register);
        editImage.setOnClickListener(this);
        updateProfile.setOnClickListener(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getAllAccountInfoDetails();

    }

    void getAllAccountInfoDetails() {
        new InvtAppAsyncTask(UserProfile.this) {

            @Override
            public void process() {
                UserSyncher syncher = new UserSyncher();
                userDetails = syncher.getUserdetails();
            }

            @Override
            public void afterPostExecute() {
                if (userDetails != null) {
                    User user = new User();
                    name.setText(userDetails.getUserName());
                    phone.setText(userDetails.getPhoneNumber());
                    status.setText(userDetails.getStatus());
                    user.setUserName(userDetails.getUserName());
                    user.setPhoneNumber(userDetails.getPhoneNumber());
                    user.setStatus(userDetails.getStatus());
                    user.setImage(userDetails.getImage());
                    InvtAppPreferences.setProfileDetails(user);
                    if (userDetails.getImage() != null && !userDetails.getImage().isEmpty()) {
                        Picasso.with(getApplicationContext()).load(userDetails.getImage()).transform(new CircleTransform()).into(profileImage);
                        profileImage.setOnClickListener(null);
                    }
                    if (userDetails.getGender() != null && !userDetails.getGender().isEmpty()) {
                        if (userDetails.getGender().equals("Male")) {
                            radioButtonMale.setChecked(true);
                            radioButtonFemale.setChecked(false);
                        } else {
                            radioButtonFemale.setChecked(true);
                            radioButtonMale.setChecked(false);

                        }
                    }
                } else {
                    ToastHelper.redToast(UserProfile.this, "No Details Found..");
                }
            }
        }.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                if(validateInputDetails(name,phone,status)) {
                    new InvtAppAsyncTask(UserProfile.this) {

                        @Override
                        public void process() {
                            User user = new User();
                            user.setUserName(name.getText().toString());
                            user.setPhoneNumber(phone.getText().toString());
                            user.setStatus(status.getText().toString());
                            if (customerImageBitmapToString != null && customerImageBitmapToString.length() > 0)
                                user.setImage(InvtAppPreferences.getAccountImage());
                            UserSyncher syncher = new UserSyncher();
                            userDetails = syncher.updateUserDetails(user);
                        }

                        @Override
                        public void afterPostExecute() {
                            if (userDetails != null) {
                                    InvtAppPreferences.setProfileDetails(userDetails);
                                    InvtAppPreferences.setProfileGiven(true);
                                    setSnackBarValidation("Profile Successfully updated.");
                                    finish();
                                } else {
                                    setSnackBarValidation(userDetails.getErrorMessage());
                                }
                        }
                    }.execute();
                }
                break;
            case R.id.edit_image:
                final Dialog dialog = new Dialog(UserProfile.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.login_image_dialog);
                ListView listView = (ListView) dialog
                        .findViewById(R.id.dialogListView);
                List<String> galleryList = new ArrayList<String>();
                galleryList.add("Take Photo");
                galleryList.add("Choose From Gallery");
                final ImageDialogAdapter dialogAdapter = new ImageDialogAdapter(UserProfile.this, R.layout.gallery_layout, galleryList);
                listView.setAdapter(dialogAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view,
                                            int position, long id) {
                        String strName = dialogAdapter.getItem(position);
                        if (strName.contains("Take Photo")) {
                            Intent takePicture = new Intent(
                                    MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(takePicture, CAMERA_REQUEST_CODE);
                        } else {
                            Intent i = new Intent(
                                    Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(i, GALLERY_REQUET_CODE);
                        }
                        dialog.cancel();
                    }
                });

                dialog.show();
                break;
        }
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());

        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;
        int borderColor = ColorUtils.setAlphaComponent(Color.WHITE, 0xFF);
        int borderRadius = 0;

        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
        Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);

        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);

        Paint paintBg = new Paint();
        paintBg.setColor(borderColor);
        paintBg.setAntiAlias(true);

        canvas.drawCircle(r, r, r, paintBg);
        canvas.drawCircle(r, r, r - borderRadius, paint);
        squaredBitmap.recycle();

        return bitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == CAMERA_REQUEST_CODE || requestCode == GALLERY_REQUET_CODE) && resultCode == Activity.RESULT_OK) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 2);

                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);
                }
            } else {
                if (data != null) {
                    Bitmap bitmap;

                    if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
                        bitmap = (Bitmap) data.getExtras().get("data");

                        Bitmap roundedCornerBitmap = HTTPUtils.getRoundedCornerBitmap(bitmap, 96);
                        profileImage.setImageBitmap(getRoundedCornerBitmap(bitmap));
                        customerImageBitmapToString = HTTPUtils.BitMapToString(bitmap);
                        InvtAppPreferences.setAccountImage(customerImageBitmapToString);
                        Log.d("CustomerImage", customerImageBitmapToString);
                        //postImage();

                    }
                    if (requestCode == GALLERY_REQUET_CODE && resultCode == Activity.RESULT_OK) {
                        bitmap = HTTPUtils.getBitmapFromCameraData(data, UserProfile.this);
                        Bitmap roundedCornerBitmap = HTTPUtils.getRoundedCornerBitmap(bitmap, 96);
                        profileImage.setImageBitmap(getRoundedCornerBitmap(bitmap));
                        customerImageBitmapToString = HTTPUtils.BitMapToString(bitmap);
                        InvtAppPreferences.setAccountImage(customerImageBitmapToString);
                        // postImage();
                        Log.d("CustomerImage", customerImageBitmapToString);
                    } else if (resultCode == Activity.RESULT_CANCELED) {
                        //TODO
                    }
                }
            }

        }

    }

}