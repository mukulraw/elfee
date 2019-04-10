package com.softcodeinfotech.helpapp.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rilixtech.CountryCodePicker;
import com.softcodeinfotech.helpapp.R;
import com.softcodeinfotech.helpapp.ServiceInterface;
import com.softcodeinfotech.helpapp.response.SigninResponse;
import com.softcodeinfotech.helpapp.util.Constant;
import com.softcodeinfotech.helpapp.util.DataValidation;
import com.softcodeinfotech.helpapp.util.SharePreferenceUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class LoginActivity extends AppCompatActivity {

    EditText mobile;
    String mMobile;
    Button login;
    ProgressBar pBar;
    ImageButton back;

    Retrofit retrofit;
    ServiceInterface serviceInterface;
    CountryCodePicker code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setUpWidget();
        pBar.setVisibility(View.GONE);
        //getData();

        code.registerPhoneNumberTextView(mobile);

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder().create();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        serviceInterface = retrofit.create(ServiceInterface.class);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();

                if (DataValidation.isValidPhoneNumber(mMobile)) {
                    Toast.makeText(LoginActivity.this, "Fill Valid Mobile Number", Toast.LENGTH_SHORT).show();
                } else {
                    pBar.setVisibility(View.VISIBLE);

                    signinReq();
                   // Toast.makeText(LoginActivity.this, "" + mMobile + " " + mPassword, Toast.LENGTH_SHORT).show();

                }

            }
        });



    }

    private void signinReq() {

        Log.d("mobile" , mMobile);

        Call<SigninResponse> call = serviceInterface.userlogin(convertPlainString(mMobile));
        call.enqueue(new Callback<SigninResponse>() {
            @Override
            public void onResponse(@NonNull Call<SigninResponse> call, @NonNull Response<SigninResponse> response) {
                assert response.body() != null;
                if (response.body().getStatus().equals("1")) {
                    pBar.setVisibility(View.GONE);
                    //Toast.makeText(LoginActivity.this, "userid"+response.body().getInformation().getdUserId(), Toast.LENGTH_SHORT).show();
                    //SharePreferenceUtils.getInstance().saveString(Constant.USER_id, String.valueOf(response.body().getInformation().getUserId()));
                    SharePreferenceUtils.getInstance().saveString(Constant.USER_mobile, mMobile);
                    //SharePreferenceUtils.getInstance().saveString(Constant.USER_name, response.body().getInformation().getName());
                    //SharePreferenceUtils.getInstance().saveString(Constant.User_age, response.body().getInformation().getAge());
                    //SharePreferenceUtils.getInstance().saveString(Constant.USER_gender, response.body().getInformation().getGender());
                    //SharePreferenceUtils.getInstance().saveString(Constant.USER_mobile, response.body().getInformation().getMobile());
                    //SharePreferenceUtils.getInstance().saveString(Constant.USER_aadhar, response.body().getInformation().getAadhar());
                    //SharePreferenceUtils.getInstance().saveString(Constant.USER_address, response.body().getInformation().getAddress());
                    //SharePreferenceUtils.getInstance().saveString(Constant.USER_state, response.body().getInformation().getState());
                    //SharePreferenceUtils.getInstance().saveString(Constant.USER_pin, response.body().getInformation().getPin());
                    //SharePreferenceUtils.getInstance().saveString(Constant.USER_imageurl, response.body().getInformation().getImageUrl());
                    //SharePreferenceUtils.getInstance().saveString(Constant.USER_profilestatus, response.body().getInformation().getProfilestatus());
                    // SharePreferenceUtils.getInstance().saveString(Constant.USER);


                    Intent homeIntent = new Intent(LoginActivity.this, MailVerifyActivity.class);
                    startActivity(homeIntent);
                    finishAffinity();
                } else {
                    pBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Some error occurred", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SigninResponse> call, @NonNull Throwable t) {
                pBar.setVisibility(View.GONE);
                Log.d("faiulure" , t.toString());
                Toast.makeText(LoginActivity.this, "Some error occurred", Toast.LENGTH_SHORT).show();
                //Toast.makeText(LoginActivity.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getData() {
        mMobile = code.getFullNumber();
    }

    private void setUpWidget() {
        mobile = findViewById(R.id.editText);
        code = findViewById(R.id.spinner3);
        login = findViewById(R.id.button3);
        pBar = findViewById(R.id.progressBar2);
        back = findViewById(R.id.imageButton3);
    }

    // convert aa param into plain text
    public RequestBody convertPlainString(String data) {
        RequestBody plainString = RequestBody.create(MediaType.parse("text/plain"), data);
        return plainString;
    }
}
