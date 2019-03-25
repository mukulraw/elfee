package com.softcodeinfotech.helpapp.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.softcodeinfotech.helpapp.R;
import com.softcodeinfotech.helpapp.ServiceInterface;
import com.softcodeinfotech.helpapp.response.GetIndividualUserResponse;
import com.softcodeinfotech.helpapp.util.Constant;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IndividualHelpActivity extends AppCompatActivity {
    ImageButton back;
    String mName, mAge, mGender, mEmail, url, mMobile;
    TextView name, ageAndGender, email;
    ImageView image;
    Button call;

    String user_id;

    TextView headerEmail;


    ProgressBar pBar;

    Retrofit retrofit;
    ServiceInterface serviceInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_help);

        setUpWidget();
        //visibilityGone();


        Gson gson = new GsonBuilder().create();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        serviceInterface = retrofit.create(ServiceInterface.class);


        final Intent intent = getIntent();
        user_id = intent.getExtras().getString("user_id");

        //Toast.makeText(this, ""+user_id, Toast.LENGTH_SHORT).show();

        //setDataReq();
        setData();

        // user_id="9";
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }



    private void setDataReq() {
        RequestBody plainString = RequestBody.create(MediaType.parse("text/plain"), user_id);
        Call<GetIndividualUserResponse> call = serviceInterface.getIndividualUserDetails(convertPlainString(user_id));
        call.enqueue(new Callback<GetIndividualUserResponse>() {
            @Override
            public void onResponse(Call<GetIndividualUserResponse> call, Response<GetIndividualUserResponse> response) {
                if (response.body().getStatus().equals(1))
                {
                    pBar.setVisibility(View.GONE);

                    mName = String.valueOf(response.body().getInformation().getName());
                    mEmail = String.valueOf(response.body().getInformation().getEmail());
                    mAge = String.valueOf(response.body().getInformation().getAge());
                    mGender = String.valueOf(response.body().getInformation().getGender());
                    url = String.valueOf(response.body().getInformation().getImageUrl());
                    mMobile =String.valueOf(response.body().getInformation().getMobile());

                    VisibilityOn();
                    setData();



                }
            }

            @Override
            public void onFailure(Call<GetIndividualUserResponse> call, Throwable t) {
                pBar.setVisibility(View.GONE);
                Toast.makeText(IndividualHelpActivity.this, ""+t.toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setData() {

        name.setText(getIntent().getStringExtra("name"));
        ageAndGender.setText(getIntent().getStringExtra("dob")+"    |    "+getIntent().getStringExtra("phone"));

        url = getIntent().getStringExtra("image");

        //for default placeholder image in glide
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.noimage);
        requestOptions.error(R.drawable.noimage);

        Glide.with(this).setDefaultRequestOptions(requestOptions).load(url).into(image);
    }


    private void setUpWidget() {

        back= findViewById(R.id.imageButton4);
        name = findViewById(R.id.textView63);
        ageAndGender = findViewById(R.id.textView64);
        email = findViewById(R.id.textView66);
        image = findViewById(R.id.imageView6);

        call = findViewById(R.id.button6);
        pBar = findViewById(R.id.progressBar7);

        headerEmail=findViewById(R.id.textView65);
    }

    private void visibilityGone() {
        name.setVisibility(View.GONE);
        ageAndGender.setVisibility(View.GONE);
        email.setVisibility(View.GONE);
        image.setVisibility(View.GONE);
        call.setVisibility(View.GONE);
        headerEmail.setVisibility(View.GONE);

    }

    private void VisibilityOn() {
        name.setVisibility(View.VISIBLE);
        ageAndGender.setVisibility(View.VISIBLE);
        email.setVisibility(View.VISIBLE);
        image.setVisibility(View.VISIBLE);
        call.setVisibility(View.VISIBLE);
        headerEmail.setVisibility(View.VISIBLE);
    }

    public RequestBody convertPlainString(String data) {
        RequestBody plainString = RequestBody.create(MediaType.parse("text/plain"), data);
        return plainString;
    }

}
