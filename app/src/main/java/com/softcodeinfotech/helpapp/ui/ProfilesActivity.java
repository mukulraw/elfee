package com.softcodeinfotech.helpapp.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.softcodeinfotech.helpapp.R;
import com.softcodeinfotech.helpapp.ServiceInterface;
import com.softcodeinfotech.helpapp.response.ProfileResponse;
import com.softcodeinfotech.helpapp.util.Constant;
import com.softcodeinfotech.helpapp.util.SharePreferenceUtils;

import java.util.Locale;

import belka.us.androidtoggleswitch.widgets.ToggleSwitch;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfilesActivity extends AppCompatActivity {

    Button submit;
    ImageButton back;
    EditText aadhar, age, email, address, pin;
    String mAadhar, mAge, mMobile, mAddress, mPin, mState, mEmail, mName;
    ToggleSwitch toggleSwitchGender;
    int genderPositionToggle;
    String mGender;
    TextView alreadyMember;
    ProgressBar pBar;
    Spinner spinner;

    Retrofit retrofit;
    ServiceInterface serviceInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String languageToLoad  = SharePreferenceUtils.getInstance().getString("lang"); // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);

        setUpWidget();
        getData();
        pBar.setVisibility(View.GONE);
        mMobile = SharePreferenceUtils.getInstance().getString("USER_mobile");
        mName = SharePreferenceUtils.getInstance().getString("USER_name");


        Gson gson = new GsonBuilder().create();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serviceInterface = retrofit.create(ServiceInterface.class);


        //category Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getApplicationContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.selectState));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {

                ((TextView) arg0.getChildAt(0)).setTextColor(Color.WHITE);
                // ((TextView) arg0.getChildAt(0)).setTextSize(5);


                mState = String.valueOf(arg0.getItemAtPosition(position));
                Toast.makeText(ProfilesActivity.this, "" + mState, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pBar.setVisibility(View.VISIBLE);
                getData();
                saveDataReq();
                // Toast.makeText(ProfilesActivity.this, "" + mAadhar + "" + mAge + "" + mMobile + "" + mAddress, Toast.LENGTH_LONG).show();
                // Toast.makeText(ProfilesActivity.this, "" + mGender, Toast.LENGTH_SHORT).show();
                // Toast.makeText(ProfilesActivity.this, "" + mPin + "" + mState, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveDataReq() {
        Call<ProfileResponse> call = serviceInterface.profileUpdate(convertPlainString(mEmail),
                convertPlainString(mName), convertPlainString(mAge), convertPlainString(mGender), convertPlainString(mMobile),
                convertPlainString(mAadhar), convertPlainString(mAddress), convertPlainString(mState), convertPlainString(mPin));
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.body().getStatus() == 1) {
                   /* SharePreferenceUtils.getInstance().saveString("USER_name", response.body().getInformation().getName());
                    SharePreferenceUtils.getInstance().saveString("USER_age", response.body().getInformation().getAge());
                    SharePreferenceUtils.getInstance().saveString("USER_gender", response.body().getInformation().getGender());
                    SharePreferenceUtils.getInstance().saveString("USER_mobile", response.body().getInformation().getMobile());
                    SharePreferenceUtils.getInstance().saveString("USER_address", response.body().getInformation().getAddress());
                    SharePreferenceUtils.getInstance().saveString("USER_state", response.body().getInformation().getState());
                    SharePreferenceUtils.getInstance().saveString("USER_pin", response.body().getInformation().getPin());*/


                    SharePreferenceUtils.getInstance().saveString(Constant.USER_id, String.valueOf(response.body().getInformation().getUId()));
                    SharePreferenceUtils.getInstance().saveString(Constant.USER_email, response.body().getInformation().getEmail());
                    SharePreferenceUtils.getInstance().saveString(Constant.USER_name, response.body().getInformation().getName());
                    SharePreferenceUtils.getInstance().saveString(Constant.User_age, response.body().getInformation().getAge());
                    SharePreferenceUtils.getInstance().saveString(Constant.USER_gender, response.body().getInformation().getGender());
                    SharePreferenceUtils.getInstance().saveString(Constant.USER_mobile, response.body().getInformation().getMobile());
                    SharePreferenceUtils.getInstance().saveString(Constant.USER_aadhar, response.body().getInformation().getAadhar());
                    SharePreferenceUtils.getInstance().saveString(Constant.USER_address, response.body().getInformation().getAddress());
                    SharePreferenceUtils.getInstance().saveString(Constant.USER_state, response.body().getInformation().getState());
                    SharePreferenceUtils.getInstance().saveString(Constant.USER_pin, response.body().getInformation().getPin());
                    //SharePreferenceUtils.getInstance().saveString(Constant.USER_imageurl, response.body().getInformation().getImageUrl());
                    SharePreferenceUtils.getInstance().saveString(Constant.USER_profilestatus, String.valueOf(response.body().getInformation().getProfilestatus()));

                    Log.i("uid",response.body().getInformation().getUId().toString());

                    /*Intent intent = new Intent(ProfilesActivity.this, SetProfileImageActivity.class);
                    startActivity(intent);
                    finish();*/

                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(ProfilesActivity.this, "" + t.toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getData() {

        mAadhar = aadhar.getText().toString().trim();
        mAge = age.getText().toString().trim();
        mEmail = email.getText().toString().trim();
        mAddress = address.getText().toString().trim();
        genderPositionToggle = toggleSwitchGender.getCheckedTogglePosition();
        mPin = pin.getText().toString().trim();


        if (genderPositionToggle == 0) {
            mGender = "Male";
        }

        if (genderPositionToggle == 1) {
            mGender = "Female";
        }

    }

    private void setUpWidget() {
        aadhar = findViewById(R.id.editText3);
        age = findViewById(R.id.editText4);
        email = findViewById(R.id.editText);
        address = findViewById(R.id.editText2);
        toggleSwitchGender = findViewById(R.id.textView17);
        alreadyMember = findViewById(R.id.textView10);
        pBar = findViewById(R.id.progressBar3);
        submit = findViewById(R.id.button3);
        back = findViewById(R.id.imageButton3);
        spinner = findViewById(R.id.spinner2);
        pin = findViewById(R.id.editText10);
    }

    // convert aa param into plain text
    public RequestBody convertPlainString(String data) {
        RequestBody plainString = RequestBody.create(MediaType.parse("text/plain"), data);
        return plainString;
    }
}
