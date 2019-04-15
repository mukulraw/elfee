package com.softcodeinfotech.helpapp.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.softcodeinfotech.helpapp.R;
import com.softcodeinfotech.helpapp.ServiceInterface;
import com.softcodeinfotech.helpapp.beanresponse.GetforgotpassResponse;
import com.softcodeinfotech.helpapp.util.Constant;
import com.softcodeinfotech.helpapp.util.DataValidation;
import com.softcodeinfotech.helpapp.util.SharePreferenceUtils;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForgotPassword extends AppCompatActivity {

    EditText mobile;
    Button send;
    String mMobile;
    ImageButton getBack;
    ProgressBar pBar;

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
        setContentView(R.layout.activity_forgot_password);

        setUpWidget();
        getData();
        pBar.setVisibility(View.GONE);

        Gson gson = new GsonBuilder().create();
        //okhttp client
        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        serviceInterface = retrofit.create(ServiceInterface.class);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
                if (DataValidation.isValidPhoneNumber(mMobile)) {
                    Toast.makeText(ForgotPassword.this, "Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();

                } else {
                    getData();
                    sendReq();
                    pBar.setVisibility(View.VISIBLE);
                }
            }
        });

        getBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }


    private void sendReq() {
        Call<GetforgotpassResponse> call = serviceInterface.getPasswordOnMobile(convertPlainString(mMobile));
        call.enqueue(new Callback<GetforgotpassResponse>() {
            @Override
            public void onResponse(Call<GetforgotpassResponse> call, Response<GetforgotpassResponse> response) {

                Toast.makeText(ForgotPassword.this, "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                if (response.body().getStatus() == 1) {
                    pBar.setVisibility(View.GONE);
                    Log.i("sucess", response.body().getMsg());
                    Intent loginIntent = new Intent(ForgotPassword.this, LoginActivity.class);
                    startActivity(loginIntent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<GetforgotpassResponse> call, Throwable t) {
                pBar.setVisibility(View.GONE);
                Toast.makeText(ForgotPassword.this, "" + t.toString(), Toast.LENGTH_SHORT).show();

            }
        });

        Toast.makeText(this, "Call serever for password", Toast.LENGTH_SHORT).show();
    }


    private void getData() {
        mMobile = mobile.getText().toString().trim();
    }

    private void setUpWidget() {

        mobile = findViewById(R.id.editText);
        send = findViewById(R.id.button3);
        getBack = findViewById(R.id.imageButton3);
        pBar = findViewById(R.id.pBar);

    }

    // convert aa param into plain text
    public RequestBody convertPlainString(String data) {
        RequestBody plainString = RequestBody.create(MediaType.parse("text/plain"), data);
        return plainString;
    }
}
