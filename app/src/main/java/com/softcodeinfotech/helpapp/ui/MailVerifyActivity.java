package com.softcodeinfotech.helpapp.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.softcodeinfotech.helpapp.R;
import com.softcodeinfotech.helpapp.ServiceInterface;
import com.softcodeinfotech.helpapp.response.SigninResponse;
import com.softcodeinfotech.helpapp.util.Constant;
import com.softcodeinfotech.helpapp.util.SharePreferenceUtils;
import com.softcodeinfotech.helpapp.verifyPOJO.verifyBean;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MailVerifyActivity extends AppCompatActivity {

    EditText otp;
    Button verify;
    //intent data
    //String intentEmail, intentOtp;
    ImageButton back;
    TextView resend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String languageToLoad  = SharePreferenceUtils.getInstance().getString("lang"); // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_verify);

        setupWidget();

       // Toast.makeText(this, ""+intentOtp, Toast.LENGTH_SHORT).show();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (otp.getText().toString().isEmpty()) {
                    Toast.makeText(MailVerifyActivity.this, "Enter otp first", Toast.LENGTH_SHORT).show();
                } else {


                    // verify
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(Constant.BASE_URL)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    ServiceInterface serviceInterface = retrofit.create(ServiceInterface.class);

                    Call<verifyBean> call = serviceInterface.verifyOTP(SharePreferenceUtils.getInstance().getString(Constant.USER_mobile) , otp.getText().toString());

                    Log.d("mmo" , SharePreferenceUtils.getInstance().getString(Constant.USER_mobile));
                    Log.d("mmo" , otp.getText().toString());

                    call.enqueue(new Callback<verifyBean>() {
                        @Override
                        public void onResponse(Call<verifyBean> call, Response<verifyBean> response) {

                            if (response.body().getStatus().equals("1"))
                            {

                                SharePreferenceUtils.getInstance().saveString("userId" , response.body().getData().getUserId());
                                SharePreferenceUtils.getInstance().saveString("name" , response.body().getData().getName());
                                SharePreferenceUtils.getInstance().saveString("dob" , response.body().getData().getDob());
                                SharePreferenceUtils.getInstance().saveString("aadhar" , response.body().getData().getAadhar());
                                SharePreferenceUtils.getInstance().saveString("address" , response.body().getData().getAddress());
                                SharePreferenceUtils.getInstance().saveString("kyc_status" , response.body().getData().getKycStatus());
                                SharePreferenceUtils.getInstance().saveString("wphone" , response.body().getData().getWphone());
                                SharePreferenceUtils.getInstance().saveString("g_name" , response.body().getData().getGName());
                                SharePreferenceUtils.getInstance().saveString("gphone" , response.body().getData().getGphone());
                                SharePreferenceUtils.getInstance().saveString("profession" , response.body().getData().getProfession());
                                SharePreferenceUtils.getInstance().saveString("yimage" , response.body().getData().getYimage());
                                SharePreferenceUtils.getInstance().saveString("gimage" , response.body().getData().getGimage());
                                SharePreferenceUtils.getInstance().saveString("afront" , response.body().getData().getAfront());
                                SharePreferenceUtils.getInstance().saveString("aback" , response.body().getData().getAback());
                                SharePreferenceUtils.getInstance().saveString("eimage" , response.body().getData().getEimage());

                                Intent intent = new Intent(MailVerifyActivity.this , MainActivity.class);
                                startActivity(intent);
                                finishAffinity();

                            }
                            else
                            {
                                Toast.makeText(MailVerifyActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onFailure(Call<verifyBean> call, Throwable t) {

                        }
                    });


                }

            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Constant.BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ServiceInterface serviceInterface = retrofit.create(ServiceInterface.class);

                Call<SigninResponse> call = serviceInterface.resendOTP(SharePreferenceUtils.getInstance().getString(Constant.USER_mobile));
                call.enqueue(new Callback<SigninResponse>() {
                    @Override
                    public void onResponse(Call<SigninResponse> call, Response<SigninResponse> response) {

                        Toast.makeText(MailVerifyActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(Call<SigninResponse> call, Throwable t) {

                    }
                });

            }
        });


    }

    private void setupWidget() {

        otp = findViewById(R.id.editText);
        verify = findViewById(R.id.button3);
        back = findViewById(R.id.imageButton3);
        resend = findViewById(R.id.textView42);
    }


}
