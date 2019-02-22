package com.softcodeinfotech.helpapp.ui;

import android.app.Dialog;
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

       submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String n = name.getText().toString();
                final String a = age.getText().toString();
                g = toggleSwitch.getCheckedTogglePosition();

                if (g == 0) {
                    gender = "Male";
                }

                if (g == 1) {
                    gender = "Female";
                }

                bar.setVisibility(View.VISIBLE);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Constant.BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ServiceInterface serviceInterface = retrofit.create(ServiceInterface.class);

                Call<ProfileResponse>call = serviceInterface.profile("", n , a , gender,"" ,  ""  , "" , "" , "");

                call.enqueue(new Callback<ProfileResponse>() {
                    @Override
                    public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {

                        if (Objects.equals(response.body().getStatus() , "1")){

                            Toast.makeText(EditProfile.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();


                            SharePreferenceUtils.getInstance().saveString(Constant.USER_name , n);
                            SharePreferenceUtils.getInstance().saveString(Constant.USER_gender , gender);
                            SharePreferenceUtils.getInstance().saveString(Constant.User_age , a);

                            finish();
                            name.setText("");
                            age.setText("");


                        }else {

                            Toast.makeText(EditProfile.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }

                        bar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<ProfileResponse> call, Throwable t) {

                        bar.setVisibility(View.GONE);

                    }
                });


            }
        });

    }
}
