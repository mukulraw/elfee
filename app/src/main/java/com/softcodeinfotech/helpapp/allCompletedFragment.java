package com.softcodeinfotech.helpapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.softcodeinfotech.helpapp.adapter.GetHelpListAdapter;
import com.softcodeinfotech.helpapp.myHelpsPOJO.Datum;
import com.softcodeinfotech.helpapp.myHelpsPOJO.myHelpsBean;
import com.softcodeinfotech.helpapp.util.Constant;
import com.softcodeinfotech.helpapp.util.SharePreferenceUtils;

import java.util.ArrayList;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.content.Context.WINDOW_SERVICE;


public class allCompletedFragment extends Fragment {

    String email;
    String name;
    String age;
    String gender;
    String mobile;
    String imageurl;
    String uid;

    //RecylerView
    ProgressDialog pBar;


    String cat , lat , lng , rad , sta;


    Retrofit retrofit;
    ServiceInterface serviceInterface;

    private ArrayList<Datum> mHelpDetailsList = new ArrayList<>();
    private GetHelpListAdapter getHelpListAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_help, container, false);


        assert getArguments() != null;
        cat = getArguments().getString("cat");
        lat = getArguments().getString("lat");
        lng = getArguments().getString("lng");
        rad = getArguments().getString("rad");
        sta = getArguments().getString("state");


        RecyclerView replaceRecyler = view.findViewById(R.id.replaceRecycler);
        pBar = new ProgressDialog(getActivity());

        pBar.setMessage("Please wait...");
        pBar.setCancelable(false);
        pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pBar.setIndeterminate(false);


        email = SharePreferenceUtils.getInstance().getString(Constant.USER_email);
        name = SharePreferenceUtils.getInstance().getString(Constant.USER_name);
        age = SharePreferenceUtils.getInstance().getString(Constant.User_age);
        gender = SharePreferenceUtils.getInstance().getString(Constant.USER_gender);
        mobile = SharePreferenceUtils.getInstance().getString(Constant.USER_mobile);
        imageurl = SharePreferenceUtils.getInstance().getString(Constant.USER_imageurl);
        uid = SharePreferenceUtils.getInstance().getString(Constant.USER_id);


        //



        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        serviceInterface = retrofit.create(ServiceInterface.class);

        //state = SharePreferenceUtils.getInstance().getString(Constant.USER_state);
        //state = "delhi";
        // Toast.makeText(this, ""+state, Toast.LENGTH_SHORT).show();
        //Log.v("state", state);


        // replaceRecyler = (RecyclerView) findViewById(R.id.recyclerView);
        StaggeredGridLayoutManager mLayoutManger = new StaggeredGridLayoutManager(2 , StaggeredGridLayoutManager.VERTICAL);
        replaceRecyler.setLayoutManager(mLayoutManger);
        replaceRecyler.setItemAnimator(new DefaultItemAnimator());

        getHelpListAdapter = new GetHelpListAdapter(getContext(), mHelpDetailsList, GetScreenWidth());
        replaceRecyler.setAdapter(getHelpListAdapter);
        replaceRecyler.setItemAnimator(new DefaultItemAnimator());



        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        getHelpListReq();
    }

    private void getHelpListReq() {

        Log.d("cat" , cat);
        pBar.show();

        Call<myHelpsBean> call = serviceInterface.sompletedHelps(SharePreferenceUtils.getInstance().getString("userId") , sta , cat , lat , lng , rad);
        call.enqueue(new Callback<myHelpsBean>() {
            @Override
            public void onResponse(@NonNull Call<myHelpsBean> call, @NonNull Response<myHelpsBean> response) {

                pBar.dismiss();

                try {
                    assert response.body() != null;
                    if (response.body().getStatus().equals("1")) {

                        Log.d("asdasdsad" , String.valueOf(response.body().getData().size()));

                        getHelpListAdapter.setData(response.body().getData());

                    /*
                    mHelpDetailsList.addAll(response.body().getInformation());

                    *//*for (int i = 0; i < response.body().getInformation().size(); i++) {
                        mHelpDetailsList.add(new GetHelpListModel(response.body().getInformation().get(i).getHelpTitle(),
                                String.valueOf(response.body().getInformation().get(i).getTimestamp()),
                                response.body().getInformation().get(i).getHelpDescription()
                                , String.valueOf(response.body().getInformation().get(i).getHelpCategoryId()),
                                response.body().getInformation().get(i).getStatus()
                                , response.body().getInformation().get(i).getState(),
                                String.valueOf(response.body().getInformation().get(i).getUserId())));
                    }*//*
                    getHelpListAdapter.notifyDataSetChanged();*/
                    }
                    else
                    {
                        Toast.makeText(getContext() , response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(@NonNull Call<myHelpsBean> call, @NonNull Throwable t) {
                pBar.dismiss();
                // Toast.makeText(MainActivity.this, "" + t.toString(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private int GetScreenWidth() {
        int width;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) Objects.requireNonNull(getContext()).getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;

        return width;
    }

    public RequestBody convertPlainString(String data) {
        return RequestBody.create(MediaType.parse("text/plain"), data);
    }


}
