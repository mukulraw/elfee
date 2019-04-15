package com.softcodeinfotech.helpapp.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.softcodeinfotech.helpapp.R;
import com.softcodeinfotech.helpapp.util.Constant;
import com.softcodeinfotech.helpapp.util.SharePreferenceUtils;

import java.util.Locale;

public class HelpersProfileActivity extends AppCompatActivity {
    ImageButton back;
    String mName,mAge,mGender,mEmail,url;
    TextView name,ageAndGender,email;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String languageToLoad  = SharePreferenceUtils.getInstance().getString("lang"); // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpers_profile);
        setUpWidget();

        //

        final Intent intent = getIntent();
        mName = intent.getExtras().getString("name");
        mAge = intent.getExtras().getString("age");
        mGender = intent.getExtras().getString("gender");
        mEmail = intent.getExtras().getString("email");
        url = intent.getExtras().getString("image");

        /*intent.putExtra("name", iName);
        intent.putExtra("address",iAddress);
        intent.putExtra("gender",iGender);
        intent.putExtra("email",iEmail);
        intent.putExtra("image",iImage);
        intent.putExtra("age",iAge);
*/

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        name.setText(mName);
        email.setText(mEmail);
        ageAndGender.setText(mAge+"    |    "+mGender);

        //for default placeholder image in glide
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.bgp);
        requestOptions.error(R.drawable.bgp);

        Glide.with(this).setDefaultRequestOptions(requestOptions).load(url).into(image);
    }

    private void setUpWidget() {

        back= findViewById(R.id.imageButton4);
        name = findViewById(R.id.textView63);
        ageAndGender = findViewById(R.id.textView64);
        email = findViewById(R.id.textView66);
        image = findViewById(R.id.imageView6);
    }
}
