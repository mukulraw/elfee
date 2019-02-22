package com.softcodeinfotech.helpapp;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.softcodeinfotech.helpapp.sendMessagePOJO.sendMessageBean;
import com.softcodeinfotech.helpapp.singleMessagePOJO.Datum;
import com.softcodeinfotech.helpapp.singleMessagePOJO.singleMessageBean;
import com.softcodeinfotech.helpapp.util.Constant;
import com.softcodeinfotech.helpapp.util.SharePreferenceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ChatScreen extends AppCompatActivity {

    Toolbar toolbar;

    RecyclerView grid;

    LinearLayoutManager manager;

    String id, name, image, chat;

    ChatAdapter adapter;

    List<Datum> list;

    ProgressBar progress;

    EditText comment;

    FloatingActionButton send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);


        list = new ArrayList<>();

        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        image = getIntent().getStringExtra("image");
        chat = getIntent().getStringExtra("chat");

        toolbar = findViewById(R.id.toolbar);
        grid = findViewById(R.id.grid);
        progress = findViewById(R.id.progress);
        comment = findViewById(R.id.comment);
        send = findViewById(R.id.send);

        manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.arrow);

        toolbar.setTitle(name);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter = new ChatAdapter(this, list);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String comm = comment.getText().toString();

                if (comm.length() > 0) {

                    progress.setVisibility(View.VISIBLE);


                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(Constant.BASE_URL)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    ServiceInterface serviceInterface = retrofit.create(ServiceInterface.class);


                    Call<sendMessageBean> call = serviceInterface.sendMessage(SharePreferenceUtils.getInstance().getString(Constant.USER_id), id, comm);

                    call.enqueue(new Callback<sendMessageBean>() {
                        @Override
                        public void onResponse(@NonNull Call<sendMessageBean> call, @NonNull Response<sendMessageBean> response) {

                            comment.setText("");
                            progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(@NonNull Call<sendMessageBean> call, @NonNull Throwable t) {

                            progress.setVisibility(View.GONE);

                        }
                    });

                }

            }
        });


        Typeface typeFace = Typeface.MONOSPACE;
        ((TextView)toolbar.getChildAt(1)).setTypeface(typeFace);



    }

    @Override
    protected void onResume() {
        super.onResume();

        loadData();

    }

    public void loadData() {

        progress.setVisibility(View.VISIBLE);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ServiceInterface serviceInterface = retrofit.create(ServiceInterface.class);

        //  Toast.makeText(b, "" + chat + " frnd id" + id + " usrid" + SharePreferenceUtils.getInstance().getString("userId"), Toast.LENGTH_SHORT).show();
        Call<singleMessageBean> call = serviceInterface.singleChatList(SharePreferenceUtils.getInstance().getString(Constant.USER_id), id, chat);

        call.enqueue(new Callback<singleMessageBean>() {
            @Override
            public void onResponse(@NonNull Call<singleMessageBean> call, @NonNull Response<singleMessageBean> response) {

                if (response.body() != null) {
                    adapter.setGridData(response.body().getData());
                }
                progress.setVisibility(View.GONE);

                //     Toast.makeText(ChatScreen.this, "loaddata", Toast.LENGTH_SHORT).show();
                schedule();

            }

            @Override
            public void onFailure(@NonNull Call<singleMessageBean> call, @NonNull Throwable t) {

                progress.setVisibility(View.GONE);

            }
        });


    }


    public void schedule() {

        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Constant.BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ServiceInterface serviceInterface = retrofit.create(ServiceInterface.class);

                Call<singleMessageBean> call = serviceInterface.singleChatList(SharePreferenceUtils.getInstance().getString(Constant.USER_id), id, chat);

                call.enqueue(new Callback<singleMessageBean>() {
                    @Override
                    public void onResponse(@NonNull Call<singleMessageBean> call, @NonNull Response<singleMessageBean> response) {


                        try {
                            if (response.body() != null && response.body().getData() != null) {
                                adapter.setGridData(response.body().getData());
                                grid.scrollBy(0, 200);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(@NonNull Call<singleMessageBean> call, @NonNull Throwable t) {

                    }
                });

            }
        }, 0, 1000);

    }

}
