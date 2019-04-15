package com.softcodeinfotech.helpapp.ui;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.softcodeinfotech.helpapp.R;
import com.softcodeinfotech.helpapp.ServiceInterface;
import com.softcodeinfotech.helpapp.response.ProfileResponse;
import com.softcodeinfotech.helpapp.util.Constant;
import com.softcodeinfotech.helpapp.util.SharePreferenceUtils;
import com.softcodeinfotech.helpapp.verifyPOJO.verifyBean;

import java.util.Locale;
import java.util.Objects;

import belka.us.androidtoggleswitch.widgets.ToggleSwitch;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class EditProfile extends AppCompatActivity {


    EditText name , email , age;

    Button submit;

    ToggleSwitch toggleSwitch;

    int g;

    ImageView back;

    String gender;

    ProgressBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String languageToLoad  = SharePreferenceUtils.getInstance().getString("lang"); // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.editprofile);

        name = findViewById(R.id.name);

        age = findViewById(R.id.age);

        submit = findViewById(R.id.submit);

        toggleSwitch = findViewById(R.id.textView17);

        bar = findViewById(R.id.progress);

        back = findViewById(R.id.imageButton3);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                finish();
            }
        });

        age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(EditProfile.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dob_popup);
                dialog.setCancelable(true);
                dialog.show();

                Button submit = dialog.findViewById(R.id.button11);
                final DatePicker dp = dialog.findViewById(R.id.view14);

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String dd = String.valueOf(dp.getDayOfMonth()) + "-" + String.valueOf(dp.getMonth() + 1) + "-" + dp.getYear();

                        Log.d("dddd" , dd);

                        age.setText(dd);

                        dialog.dismiss();

                    }
                });

            }
        });


        age.setText(SharePreferenceUtils.getInstance().getString("dob"));
        name.setText(SharePreferenceUtils.getInstance().getString("name"));


       submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String n = name.getText().toString();
                final String a = age.getText().toString();
                /*g = toggleSwitch.getCheckedTogglePosition();

                if (g == 0) {
                    gender = "Male";
                }

                if (g == 1) {
                    gender = "Female";
                }*/

                bar.setVisibility(View.VISIBLE);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Constant.BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ServiceInterface serviceInterface = retrofit.create(ServiceInterface.class);

                Call<verifyBean>call = serviceInterface.editProfile(SharePreferenceUtils.getInstance().getString("userId"), n , a);

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


                            finish();
                        }
                        else
                        {
                            Toast.makeText(EditProfile.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        bar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<verifyBean> call, Throwable t) {

                        bar.setVisibility(View.GONE);

                    }
                });


            }
        });

    }
}
