package com.softcodeinfotech.helpapp.ui;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.softcodeinfotech.helpapp.R;
import com.softcodeinfotech.helpapp.util.Constant;
import com.softcodeinfotech.helpapp.util.SharePreferenceUtils;

import java.io.File;

public class MyProfileActivity extends AppCompatActivity {

    ImageButton back;

    String mName, mAge, mGender, mEmail, url;

    TextView name, ageAndGender, email, edit;

    ImageView image;

    static final int RC_TAKE_PHOTO = 1;

    private final int PICK_IMAGE_REQUEST = 2;

    File file;

    Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        mName = SharePreferenceUtils.getInstance().getString(Constant.USER_name);
        mAge = SharePreferenceUtils.getInstance().getString(Constant.User_age);
        mGender = SharePreferenceUtils.getInstance().getString(Constant.USER_gender);
        mEmail = SharePreferenceUtils.getInstance().getString(Constant.USER_email);
        url = SharePreferenceUtils.getInstance().getString(Constant.USER_imageurl);

        setUpWidget();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        name.setText(mName);
        email.setText(mEmail);
        ageAndGender.setText(mAge + "    |    " + mGender);

        //for default placeholder image in glide
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.bgp);
        requestOptions.error(R.drawable.bgp);

        Glide.with(this).setDefaultRequestOptions(requestOptions).load(url).into(image);


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MyProfileActivity.this, EditProfile.class);
                startActivity(i);
            }
        });


    }

    private void setUpWidget() {

        back = findViewById(R.id.imageButton4);
        name = findViewById(R.id.textView63);
        ageAndGender = findViewById(R.id.textView64);
        email = findViewById(R.id.textView66);
        image = findViewById(R.id.imageView6);
        edit = findViewById(R.id.textView33);
    }
}
