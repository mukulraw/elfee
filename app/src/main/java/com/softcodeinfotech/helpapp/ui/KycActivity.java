package com.softcodeinfotech.helpapp.ui;

import android.content.res.Configuration;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.softcodeinfotech.helpapp.R;
import com.softcodeinfotech.helpapp.ServiceInterface;
import com.softcodeinfotech.helpapp.response.AadharUpdateResponse;
import com.softcodeinfotech.helpapp.util.Constant;
import com.softcodeinfotech.helpapp.util.SharePreferenceUtils;

import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KycActivity extends AppCompatActivity {
    ImageButton back;
    Button update;
    TextInputEditText aadharNumber;
    ProgressBar pBar;
    String mAadhar, mEmail, mkyc;

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
        setContentView(R.layout.activity_kyc);
        setUpWidget();
        getData();
        pBar.setVisibility(View.GONE);

        mEmail = SharePreferenceUtils.getInstance().getString(Constant.USER_email);
        mkyc = SharePreferenceUtils.getInstance().getString(Constant.USER_aadhar);

        aadharNumber.setText(mkyc);

        Gson gson = new GsonBuilder().create();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serviceInterface = retrofit.create(ServiceInterface.class);

        // Toast.makeText(this, ""+mEmail, Toast.LENGTH_SHORT).show();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
                pBar.setVisibility(View.VISIBLE);
                if (mAadhar.isEmpty()) {
                    Toast.makeText(KycActivity.this, "Fill Data First", Toast.LENGTH_SHORT).show();
                } else {
                    pBar.setVisibility(View.VISIBLE);
                    sendDataReq();
                }
            }
        });
    }

    private void sendDataReq() {

        Call<AadharUpdateResponse> call = serviceInterface.aadharUpdate(convertPlainString(mEmail), convertPlainString(mAadhar));
        call.enqueue(new Callback<AadharUpdateResponse>() {
            @Override
            public void onResponse(Call<AadharUpdateResponse> call, Response<AadharUpdateResponse> response) {
                pBar.setVisibility(View.GONE);
                if (response.body().getStatus().equals(1)) {
                    Toast.makeText(KycActivity.this, "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    SharePreferenceUtils.getInstance().saveString(Constant.USER_aadhar, response.body().getInformation().getAadhar());

                } else {
                    Toast.makeText(KycActivity.this, "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AadharUpdateResponse> call, Throwable t) {
                pBar.setVisibility(View.GONE);
                Toast.makeText(KycActivity.this, "" + t.toString(), Toast.LENGTH_SHORT).show();


            }
        });
    }

    private void getData() {
        mAadhar = aadharNumber.getText().toString().trim();
    }

    private void setUpWidget() {
        back = findViewById(R.id.imageButton);
        update = findViewById(R.id.button5);
        aadharNumber = findViewById(R.id.aadharnNumber);
        pBar = findViewById(R.id.progressBar5);
    }

    // convert aa param into plain text
    public RequestBody convertPlainString(String data) {
        RequestBody plainString = RequestBody.create(MediaType.parse("text/plain"), data);
        return plainString;
    }
}
