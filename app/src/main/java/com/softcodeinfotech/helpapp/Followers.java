package com.softcodeinfotech.helpapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.softcodeinfotech.helpapp.getFollowersPOJO.Datum;
import com.softcodeinfotech.helpapp.getFollowersPOJO.getFollowersBean;
import com.softcodeinfotech.helpapp.ui.HistoryActivity;
import com.softcodeinfotech.helpapp.util.Constant;
import com.softcodeinfotech.helpapp.util.SharePreferenceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Followers extends AppCompatActivity {

    ImageButton back;


    String TAG = "HistoryActivity";
    private RecyclerView recycler_helpHistory;
    private ArrayList<Datum> mHelpDetailsList = new ArrayList<Datum>();
    private HistoryAdapter historyAdapter;

    ProgressDialog pBar;

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
        setContentView(R.layout.activity_followers);

        setUpWidget();


        //get user id from Sharedpreferences

        pBar.show();


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
        GridLayoutManager mLayoutManger = new GridLayoutManager(this, 1);
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
        Call<getFollowersBean> call = serviceInterface.followers(user_id);
        call.enqueue(new Callback<getFollowersBean>() {
            @Override
            public void onResponse(Call<getFollowersBean> call, Response<getFollowersBean> response) {
                pBar.dismiss();

                if (response.body().getStatus().equals("1")) {


                    historyAdapter.setData(response.body().getData());


                } else {
                    Toast.makeText(Followers.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<getFollowersBean> call, Throwable t) {
                pBar.dismiss();
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
        pBar = new ProgressDialog(this);

        pBar.setMessage("Please wait...");
        pBar.setCancelable(false);
        pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pBar.setIndeterminate(false);
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

    class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        //
        private Context mContext;
        private List<Datum> mHelpHistoryModel;
        private String TAG = "HistoryAdapter";
        private int mScrenwidth;

        public HistoryAdapter(Context mContext, List<Datum> mHelpHistoryModel, int mScrenwidth) {
            this.mContext = mContext;
            this.mHelpHistoryModel = mHelpHistoryModel;
            this.mScrenwidth = mScrenwidth;
        }

        public void setData(List<Datum> list)
        {
            this.mHelpHistoryModel = list;
            notifyDataSetChanged();
        }

        class HistoryAdapterHolder extends RecyclerView.ViewHolder {

            TextView name , phone;
            CircleImageView image;


            HistoryAdapterHolder(@NonNull View itemView) {
                super(itemView);

                name = itemView.findViewById(R.id.username);
                phone = itemView.findViewById(R.id.phone);
                image = itemView.findViewById(R.id.profile);
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.follower_list_model, viewGroup, false);
            return new HistoryAdapterHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            final Datum  item =mHelpHistoryModel.get(i);
            ((HistoryAdapterHolder) viewHolder).name.setText(item.getName());
            ((HistoryAdapterHolder) viewHolder).phone.setText(item.getPhone());


            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).showImageForEmptyUri(R.drawable.noimage).build();

            ImageLoader loader = ImageLoader.getInstance();


            loader.displayImage(item.getImage(), ((HistoryAdapterHolder) viewHolder).image, options);


        }

        @Override
        public int getItemCount() {
            return mHelpHistoryModel.size();
        }
    }

}
