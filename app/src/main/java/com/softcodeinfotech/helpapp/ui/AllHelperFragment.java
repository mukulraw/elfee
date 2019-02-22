package com.softcodeinfotech.helpapp.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.softcodeinfotech.helpapp.R;
import com.softcodeinfotech.helpapp.ServiceInterface;
import com.softcodeinfotech.helpapp.adapter.GetAllHelperListAdapter;
import com.softcodeinfotech.helpapp.model.GetAllHelperListModel;
import com.softcodeinfotech.helpapp.response.GetAllHelperListResponse;
import com.softcodeinfotech.helpapp.util.Constant;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.WINDOW_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllHelperFragment extends Fragment {

    ImageButton back;
    ProgressBar pBar;


    String profilestatus;

    Retrofit retrofit;
    ServiceInterface serviceInterface;

    String TAG = "AllHelperActivity";
    private RecyclerView recycler_allhelper;
    private ArrayList<GetAllHelperListModel> mHelperDetailsList = new ArrayList<GetAllHelperListModel>();
    private GetAllHelperListAdapter getAllHelperListAdapter;

    String cat , lat , lng , rad;

    public AllHelperFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_all_helper, container, false);


         cat = getArguments().getString("cat");
         lat = getArguments().getString("lat");
         lng = getArguments().getString("lng");
         rad = getArguments().getString("rad");

        back = view.findViewById(R.id.imageButton);
        pBar = view.findViewById(R.id.progressBar6);

       /* back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/


        //
        pBar.setVisibility(View.VISIBLE);


        Gson gson = new GsonBuilder().create();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        serviceInterface = retrofit.create(ServiceInterface.class);

        //user_id = SharePreferenceUtils.getInstance().getString(Constant.USER_id);
        //Log.v(TAG, user_id + user_id);
        // Toast.makeText(this, ""+user_id, Toast.LENGTH_SHORT).show();


        recycler_allhelper = (RecyclerView)view.findViewById(R.id.recyclerView);
        LinearLayoutManager mLayoutManger = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recycler_allhelper.setLayoutManager(mLayoutManger);
        recycler_allhelper.setItemAnimator(new DefaultItemAnimator());

        getAllHelperListAdapter = new GetAllHelperListAdapter(getContext(), mHelperDetailsList, GetScreenWidth());
        recycler_allhelper.setAdapter(getAllHelperListAdapter);
        recycler_allhelper.setItemAnimator(new DefaultItemAnimator());

        profilestatus = "1";




        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        getDataReq();
    }

    private void getDataReq() {

        Call<GetAllHelperListResponse> call = serviceInterface.getAllHelper(convertPlainString(profilestatus));
        call.enqueue(new Callback<GetAllHelperListResponse>() {
            @Override
            public void onResponse(Call<GetAllHelperListResponse> call, Response<GetAllHelperListResponse> response) {
                if (response.body().getStatus().equals(1)) {
                    pBar.setVisibility(View.GONE);
                    for (int i = 0; i < response.body().getInformation().size(); i++) {
                        mHelperDetailsList.add(new GetAllHelperListModel(String.valueOf(response.body().getInformation().get(i).getUserId()),
                                response.body().getInformation().get(i).getName(), response.body().getInformation().get(i).getEmail(),
                                response.body().getInformation().get(i).getMobile(), response.body().getInformation().get(i).getAge(),
                                response.body().getInformation().get(i).getGender(), response.body().getInformation().get(i).getProfilestatus(),
                                response.body().getInformation().get(i).getAadhar(), response.body().getInformation().get(i).getAddress(),
                                response.body().getInformation().get(i).getState(), response.body().getInformation().get(i).getPin(),
                                response.body().getInformation().get(i).getImageUrl()));
                    }
                    getAllHelperListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<GetAllHelperListResponse> call, Throwable t) {
                pBar.setVisibility(View.GONE);
                //Toast.makeText(getContext().this, "" + t.toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }





    // convert aa param into plain text
    public RequestBody convertPlainString(String data) {
        RequestBody plainString = RequestBody.create(MediaType.parse("text/plain"), data);
        return plainString;
    }

    private int GetScreenWidth() {
        int width = 100;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getContext().getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;

        return width;
    }

}

