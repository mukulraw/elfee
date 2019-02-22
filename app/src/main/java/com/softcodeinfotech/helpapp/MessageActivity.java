package com.softcodeinfotech.helpapp;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.softcodeinfotech.helpapp.allMessagePOJO.Datum;
import com.softcodeinfotech.helpapp.allMessagePOJO.allMessageBean;
import com.softcodeinfotech.helpapp.util.Constant;
import com.softcodeinfotech.helpapp.util.SharePreferenceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MessageActivity extends AppCompatActivity {

    MassageAdapter holder;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ProgressBar progress;
    List<Datum> list;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        recyclerView = findViewById(R.id.messagerecycler);
        progress = findViewById(R.id.progress);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.arrow);

        toolbar.setTitle("My Messages");





        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        list = new ArrayList<>();

        holder = new MassageAdapter(this, list);
        recyclerView.setAdapter(holder);
        recyclerView.setLayoutManager(linearLayoutManager);


        Typeface typeFace = Typeface.MONOSPACE;
        ((TextView)toolbar.getChildAt(1)).setTypeface(typeFace);



    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        //bean.getInstance().setConnectivityListener(this);
        loadData();

    }

    ///////////////////internet connectivity check///////////////






    public void loadData() {
        progress.setVisibility(View.VISIBLE);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ServiceInterface serviceInterface = retrofit.create(ServiceInterface.class);



        Call<allMessageBean> call = serviceInterface.allMessageList(SharePreferenceUtils.getInstance().getString(Constant.USER_id));

        call.enqueue(new Callback<allMessageBean>() {
            @Override
            public void onResponse(@NonNull Call<allMessageBean> call, @NonNull Response<allMessageBean> response) {


                if (response.body() != null) {
                    holder.setgrid(response.body().getData());
                }

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(@NonNull Call<allMessageBean> call, @NonNull Throwable t) {

                progress.setVisibility(View.GONE);

            }
        });
    }


}
