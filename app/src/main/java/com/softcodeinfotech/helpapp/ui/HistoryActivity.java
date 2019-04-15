package com.softcodeinfotech.helpapp.ui;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.softcodeinfotech.helpapp.R;
import com.softcodeinfotech.helpapp.ServiceInterface;
import com.softcodeinfotech.helpapp.adapter.HistoryAdapter;
import com.softcodeinfotech.helpapp.model.HelpModel;
import com.softcodeinfotech.helpapp.myHelpsPOJO.Datum;
import com.softcodeinfotech.helpapp.myHelpsPOJO.myHelpsBean;
import com.softcodeinfotech.helpapp.response.HelpHistoryResponse;
import com.softcodeinfotech.helpapp.util.Constant;
import com.softcodeinfotech.helpapp.util.SharePreferenceUtils;

import java.util.ArrayList;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class HistoryActivity extends AppCompatActivity {
    ImageButton back;


    String TAG = "HistoryActivity";
    private RecyclerView recycler_helpHistory;
    private ArrayList<Datum> mHelpDetailsList = new ArrayList<Datum>();
    private HistoryAdapter historyAdapter;

    ProgressBar pBar;

    String user_id;

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
        setContentView(R.layout.activity_history);

        setUpWidget();


        //get user id from Sharedpreferences

        pBar.setVisibility(View.VISIBLE);


        Gson gson = new GsonBuilder().create();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        serviceInterface = retrofit.create(ServiceInterface.class);

        user_id = SharePreferenceUtils.getInstance().getString("userId");
        Log.v(TAG, user_id + user_id);
        // Toast.makeText(this, ""+user_id, Toast.LENGTH_SHORT).show();


        recycler_helpHistory = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager mLayoutManger = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler_helpHistory.setLayoutManager(mLayoutManger);
        recycler_helpHistory.setItemAnimator(new DefaultItemAnimator());

        historyAdapter = new HistoryAdapter(this, mHelpDetailsList, GetScreenWidth());
        recycler_helpHistory.setAdapter(historyAdapter);
        recycler_helpHistory.setItemAnimator(new DefaultItemAnimator());




        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getHelpHistory() {
        Call<myHelpsBean> call = serviceInterface.myHelps(user_id);
        call.enqueue(new Callback<myHelpsBean>() {
            @Override
            public void onResponse(Call<myHelpsBean> call, Response<myHelpsBean> response) {
                pBar.setVisibility(View.GONE);

                if (response.body().getStatus().equals("1")) {


                    historyAdapter.setData(response.body().getData());


                } else {
                    Toast.makeText(HistoryActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<myHelpsBean> call, Throwable t) {
                pBar.setVisibility(View.GONE);
                //Toast.makeText(HistoryActivity.this, "" + t.toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private int GetScreenWidth() {
        int width = 100;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;

        return width;
    }

    private void setUpWidget() {

        back = findViewById(R.id.imageButton);
        pBar = findViewById(R.id.progressBar6);
    }

    // convert aa param into plain text
    public RequestBody convertPlainString(String data) {
        RequestBody plainString = RequestBody.create(MediaType.parse("text/plain"), data);
        return plainString;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getHelpHistory();
    }
}
