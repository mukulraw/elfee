package com.softcodeinfotech.helpapp.ui;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.softcodeinfotech.helpapp.About;
import com.softcodeinfotech.helpapp.Followers;
import com.softcodeinfotech.helpapp.KYC;
import com.softcodeinfotech.helpapp.LocaleHelper;
import com.softcodeinfotech.helpapp.Notice;
import com.softcodeinfotech.helpapp.PagerFragment;
import com.softcodeinfotech.helpapp.R;
import com.softcodeinfotech.helpapp.ServiceInterface;
import com.softcodeinfotech.helpapp.response.GetCategoryResponse;
import com.softcodeinfotech.helpapp.util.Constant;
import com.softcodeinfotech.helpapp.util.SharePreferenceUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    String name, age, mobile, imageurl, uid, state, kycstatus;

    DrawerLayout drawer;
    ImageButton toggle;
    TextView profile, orders, logout , notice , about , followers;
    //BottomNavigationView bottom;
    //TextView toolbar;

    CircleImageView image;
    TextView dName;
    TextView fabButton;

    TextView addre;

    //RecylerView
    ProgressDialog pBar;

    Spinner location;

    String address = "";

    TextView category;
    TextView loca;

    TextView language;

    Retrofit retrofit;
    ServiceInterface serviceInterface;


    List<String> catName;
    List<String> catId;

    List<String> locName;
    List<String> locId;

    String cat = "0", rad;

    String TAG = "MainActivity";

    //location
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int MY_PERMISSION_REQUEST_FINE_LOCATION = 101;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private String latitude, longitude;

    LinearLayout helps, helpers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String languageToLoad  = SharePreferenceUtils.getInstance().getString("lang"); // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        catName = new ArrayList<>();
        catId = new ArrayList<>();

        locName = new ArrayList<>();
        locId = new ArrayList<>();

        locName.add(getString(R.string.nearby5km));
        locName.add(getString(R.string.nearby10km));
        locName.add(getString(R.string.nearby20km));
        locName.add(getString(R.string.nearby50km));


        locId.add("5");
        locId.add("10");
        locId.add("20");
        locId.add("50");

        setUpWidget();


        //
        pBar.dismiss();

        //location

        locationRequest = new LocationRequest();
        locationRequest.setInterval(7500); //use a value fo about 10 to 15s for a real app
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        latitude = String.valueOf(location.getLatitude());
                        longitude = String.valueOf(location.getLongitude());
                        try {
                            getAddress(MainActivity.this, location.getLatitude(), location.getLongitude());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            });
        } else {
            // request permissions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_FINE_LOCATION);
            }
        }

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                for (Location location : locationResult.getLocations()) {
                    //Update UI with location data
                    if (location != null) {
                        latitude = (String.valueOf(location.getLatitude()));
                        longitude = (String.valueOf(location.getLongitude()));


                    }
                }
            }

        };


        //retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        serviceInterface = retrofit.create(ServiceInterface.class);

        //state = SharePreferenceUtils.getInstance().getString(Constant.USER_state);
        state = "delhi";
        // Toast.makeText(this, ""+state, Toast.LENGTH_SHORT).show();
        Log.v("state", state);

      /*  // replaceRecyler = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager mLayoutManger = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        replaceRecyler.setLayoutManager(mLayoutManger);
        replaceRecyler.setItemAnimator(new DefaultItemAnimator());

        getHelpListAdapter = new GetHelpListAdapter(this, mHelpDetailsList, GetScreenWidth());
        replaceRecyler.setAdapter(getHelpListAdapter);
        replaceRecyler.setItemAnimator(new DefaultItemAnimator());
      //  getHelpListReq();*/


        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDrawer();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kycstatus.equals("0")) {
                    Toast.makeText(MainActivity.this, getString(R.string.kkyycc), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, KYC.class);
                    startActivity(intent);

                } else {

                    Intent intent = new Intent(MainActivity.this, MyProfileActivity.class);
                    startActivity(intent);
                    drawer.closeDrawer(GravityCompat.START);

                }
            }
        });


        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Notice.class);
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);


            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, About.class);
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);


            }
        });


        /*kyc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mobile.isEmpty()) {
                    Toast.makeText(MainActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);

                } else {

                    Intent kycIntent = new Intent(MainActivity.this, KycActivity.class);
                    startActivity(kycIntent);
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });*/
        /*account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mobile.isEmpty()) {
                    Toast.makeText(MainActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);

                } else {

                    Intent accountIntent = new Intent(MainActivity.this, AccountActivity.class);
                    startActivity(accountIntent);
                    drawer.closeDrawer(GravityCompat.START);
                }

            }
        });*/
        /*myHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mobile.isEmpty()) {
                    Toast.makeText(MainActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);

                } else {

                    Intent myhistoryIntent = new Intent(MainActivity.this, HistoryActivity.class);
                    startActivity(myhistoryIntent);
                    drawer.closeDrawer(GravityCompat.START);
                }

            }
        });*/

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (kycstatus.equals("0")) {

                    Toast.makeText(MainActivity.this, getString(R.string.kkyycc), Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(MainActivity.this, KYC.class);
                    startActivity(intent);

                } else {
                    Intent addHelpIntent = new Intent(MainActivity.this, AddHelpActivity.class);
                    addHelpIntent.putExtra("lati", latitude);
                    addHelpIntent.putExtra("longi", longitude);
                    startActivity(addHelpIntent);
                }
            }
        });


        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        serviceInterface = retrofit.create(ServiceInterface.class);


        //getCategoryReq();

        FragmentManager fm1 = getSupportFragmentManager();
        FragmentTransaction ft1 = fm1.beginTransaction();
        PagerFragment PagerFragment = new PagerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("cat", cat);
        bundle.putString("lat", latitude);
        bundle.putString("lng", longitude);
        bundle.putString("rad", rad);
        bundle.putString("state", loca.getText().toString());
        PagerFragment.setArguments(bundle);
        ft1.replace(R.id.replace, PagerFragment);
        ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        //ft.addToBackStack(null);
        ft1.commit();
        drawer.closeDrawer(GravityCompat.START);

        rad = "2";
        //category.setText(catName.get(0));

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(MainActivity.this,
                R.layout.spinner_item, locName);//setting the country_array to spinner
        // string value
        adapter1.setDropDownViewResource(R.layout.spinner_item);
        location.setAdapter(adapter1);


        /*category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                cat = catId.get(position);

                FragmentManager fm1 = getSupportFragmentManager();
                FragmentTransaction ft1 = fm1.beginTransaction();
                PagerFragment PagerFragment=new PagerFragment();
                Bundle bundle = new Bundle();
                bundle.putString("cat" , cat);
                bundle.putString("lat" , latitude);
                bundle.putString("lng" , longitude);
                bundle.putString("rad" , rad);
                PagerFragment.setArguments(bundle);
                ft1.replace(R.id.replace, PagerFragment);
                ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                //ft.addToBackStack(null);
                ft1.commit();
                drawer.closeDrawer(GravityCompat.START);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/


        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dialog_layout);
                dialog.show();


                final RecyclerView grid = dialog.findViewById(R.id.grid);
                TextView reset = dialog.findViewById(R.id.textView47);
                final GridLayoutManager manager = new GridLayoutManager(MainActivity.this, 3);

                reset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        category.setText(getString(R.string.all_categories));
                        cat = "0";
                        FragmentManager fm1 = getSupportFragmentManager();
                        FragmentTransaction ft1 = fm1.beginTransaction();
                        PagerFragment PagerFragment = new PagerFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("cat", cat);
                        bundle.putString("lat", latitude);
                        bundle.putString("lng", longitude);
                        bundle.putString("rad", rad);
                        bundle.putString("state", loca.getText().toString());
                        PagerFragment.setArguments(bundle);
                        ft1.replace(R.id.replace, PagerFragment);
                        ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                        //ft.addToBackStack(null);
                        ft1.commit();
                        drawer.closeDrawer(GravityCompat.START);
                        dialog.dismiss();

                    }
                });

                Call<GetCategoryResponse> call = serviceInterface.getCategory();
                call.enqueue(new Callback<GetCategoryResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<GetCategoryResponse> call, @NonNull Response<GetCategoryResponse> response) {
                        if (response.body() != null && response.body().getStatus().equals("1")) {


                            CategoryAdapter adapter = new CategoryAdapter(MainActivity.this, response.body().getInformation(), dialog);
                            grid.setAdapter(adapter);
                            grid.setLayoutManager(manager);


                        } else {
                            Toast.makeText(MainActivity.this, "not inserted", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<GetCategoryResponse> call, @NonNull Throwable t) {

                    }
                });


            }
        });


        helps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toolbar.setText("Help Me");
                //  Toast.makeText(MainActivity.this, "help me", Toast.LENGTH_SHORT).show();
                // return true;
                // break;


                FragmentManager fm1 = getSupportFragmentManager();
                FragmentTransaction ft1 = fm1.beginTransaction();
                PagerFragment PagerFragment = new PagerFragment();
                ft1.replace(R.id.replace, PagerFragment);
                ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                //ft.addToBackStack(null);
                ft1.commit();
                drawer.closeDrawer(GravityCompat.START);

            }
        });


        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.language_dialog);
                dialog.show();

                final Button en = dialog.findViewById(R.id.button17);
                final Button hi = dialog.findViewById(R.id.button18);

                String l = SharePreferenceUtils.getInstance().getString("lang");

                if (l.equals("en"))
                {
                    en.setBackground(getResources().getDrawable(R.drawable.green_back_round));
                    hi.setBackground(getResources().getDrawable(R.drawable.white_back_round));
                    en.setTextColor(Color.WHITE);
                    hi.setTextColor(Color.BLACK);
                }
                else
                {
                    en.setBackground(getResources().getDrawable(R.drawable.white_back_round));
                    hi.setBackground(getResources().getDrawable(R.drawable.green_back_round));
                    en.setTextColor(Color.BLACK);
                    hi.setTextColor(Color.WHITE);
                }

                en.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        en.setBackground(getResources().getDrawable(R.drawable.green_back_round));
                        hi.setBackground(getResources().getDrawable(R.drawable.white_back_round));
                        en.setTextColor(Color.WHITE);
                        hi.setTextColor(Color.BLACK);

                        String languageToLoad  = "en"; // your language
                        Locale locale = new Locale(languageToLoad);
                        Locale.setDefault(locale);
                        Configuration config = new Configuration();
                        config.locale = locale;
                        getBaseContext().getResources().updateConfiguration(config,
                                getBaseContext().getResources().getDisplayMetrics());

                        SharePreferenceUtils.getInstance().saveString("lang" , languageToLoad);

                        recreate();

                    }
                });

                hi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        en.setBackground(getResources().getDrawable(R.drawable.white_back_round));
                        hi.setBackground(getResources().getDrawable(R.drawable.green_back_round));
                        en.setTextColor(Color.BLACK);
                        hi.setTextColor(Color.WHITE);

                        String languageToLoad  = "hi"; // your language
                        Locale locale = new Locale(languageToLoad);
                        Locale.setDefault(locale);
                        Configuration config = new Configuration();
                        config.locale = locale;
                        getBaseContext().getResources().updateConfiguration(config,
                                getBaseContext().getResources().getDisplayMetrics());

                        SharePreferenceUtils.getInstance().saveString("lang" , languageToLoad);

                        recreate();

                    }
                });

            }
        });


        helpers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toolbar.setText("Helpers List");
                FragmentManager fm2 = getSupportFragmentManager();
                FragmentTransaction ft2 = fm2.beginTransaction();
                AllHelperFragment allHelperFragment = new AllHelperFragment();
                ft2.replace(R.id.replace, allHelperFragment);
                ft2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                //ft.addToBackStack(null);
                ft2.commit();
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (kycstatus.equals("0")) {
                    Toast.makeText(MainActivity.this, getString(R.string.kkyycc), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, KYC.class);
                    startActivity(intent);

                } else {
                    Intent intent = new Intent(MainActivity.this, Followers.class);
                    startActivity(intent);

                }
            }
        });

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (kycstatus.equals("0")) {
                    Toast.makeText(MainActivity.this, getString(R.string.kkyycc), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, KYC.class);
                    startActivity(intent);

                } else {
                    Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                    startActivity(intent);

                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharePreferenceUtils.getInstance().deletePref();
                SharePreferenceUtils.getInstance().saveString(Constant.USER_profilestatus, "1");
                Intent signuploginIntent = new Intent(MainActivity.this, SignupLoginActivity.class);
                startActivity(signuploginIntent);
                finishAffinity();
            }
        });


        /*bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId())
                {
                    case R.id.helpMe:
                        toolbar.setText("Help Me");
                        //  Toast.makeText(MainActivity.this, "help me", Toast.LENGTH_SHORT).show();
                        // return true;
                        // break;

                        FragmentManager fm1 = getSupportFragmentManager();
                        FragmentTransaction ft1 = fm1.beginTransaction();
                        PagerFragment PagerFragment=new PagerFragment();
                        ft1.replace(R.id.replace, PagerFragment);
                        ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                        //ft.addToBackStack(null);
                        ft1.commit();
                        drawer.closeDrawer(GravityCompat.START);

                        break;

                    case R.id.helpersList:
                        toolbar.setText("Helpers List");
                      *//*  Intent intent = new Intent(MainActivity.this, AllHelperActivity.class);
                        startActivity(intent);
                        //  Toast.makeText(MainActivity.this, "Helpers List", Toast.LENGTH_SHORT).show();
                        break;*//*


                        FragmentManager fm2 = getSupportFragmentManager();
                        FragmentTransaction ft2 = fm2.beginTransaction();
                        AllHelperFragment allHelperFragment = new AllHelperFragment();
                        ft2.replace(R.id.replace, allHelperFragment);
                        ft2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                        //ft.addToBackStack(null);
                        ft2.commit();
                        drawer.closeDrawer(GravityCompat.START);
                        break;

                }
                return true;
            }
        });
*/

        location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                rad = locId.get(position);
                FragmentManager fm1 = getSupportFragmentManager();
                FragmentTransaction ft1 = fm1.beginTransaction();
                PagerFragment PagerFragment = new PagerFragment();
                Bundle bundle = new Bundle();
                bundle.putString("cat", cat);
                bundle.putString("lat", latitude);
                bundle.putString("lng", longitude);
                bundle.putString("rad", rad);
                bundle.putString("state", loca.getText().toString());
                PagerFragment.setArguments(bundle);
                ft1.replace(R.id.replace, PagerFragment);
                ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                //ft.addToBackStack(null);
                ft1.commit();
                drawer.closeDrawer(GravityCompat.START);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void setUpWidget() {
        ///
        drawer = findViewById(R.id.drawer);
        toggle = findViewById(R.id.imageButton10);
        profile = findViewById(R.id.account);
        orders = findViewById(R.id.posts);
        logout = findViewById(R.id.logout);
        language = findViewById(R.id.language);
        notice = findViewById(R.id.notice);
        about = findViewById(R.id.about);
        followers = findViewById(R.id.followers);


        // settings = findViewById(R.id.imageButton6);
        fabButton = findViewById(R.id.floatingActionButton3);

        helps = findViewById(R.id.button10);
        helpers = findViewById(R.id.button11);

        //drawer design
        image = findViewById(R.id.pic);
        dName = findViewById(R.id.name);


        addre = findViewById(R.id.textView41);

        category = findViewById(R.id.textView43);
        location = findViewById(R.id.spinner);

        loca = findViewById(R.id.location);
        //recyler
        pBar = new ProgressDialog(this);

        pBar.setMessage("Please wait...");
        pBar.setCancelable(false);
        pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pBar.setIndeterminate(false);

    }


    //
    void toggleDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    public RequestBody convertPlainString(String data) {
        return RequestBody.create(MediaType.parse("text/plain"), data);
    }


    //method to get location

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSION_REQUEST_FINE_LOCATION:

                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), getString(R.string.permission), Toast.LENGTH_SHORT).show();
                    finish();
                }  //permission was granted do nothing and carry on


                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();


        /*LocaleHelper.setLocale(MainActivity.this, "hi");

        //It is required to recreate the activity to reflect the change in UI.
        recreate();*/



        name = SharePreferenceUtils.getInstance().getString("name");
        age = SharePreferenceUtils.getInstance().getString("dob");
        kycstatus = SharePreferenceUtils.getInstance().getString("kyc_status");

        mobile = SharePreferenceUtils.getInstance().getString(Constant.USER_mobile);
        imageurl = SharePreferenceUtils.getInstance().getString("yimage");
        uid = SharePreferenceUtils.getInstance().getString("userId");


        //  Toast.makeText(this, ""+uid, Toast.LENGTH_SHORT).show();
        // Toast.makeText(this, ""+email+""+name+""+""+age+""+gender+""+mobile, Toast.LENGTH_SHORT).show();

        //for default placeholder image in glide
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.noimage);
        requestOptions.error(R.drawable.noimage);

        dName.setText(name);

        Glide.with(this).setDefaultRequestOptions(requestOptions).load(imageurl).into(image);


        startLocationUpdates();



    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_FINE_LOCATION);
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

        Context context;
        List<GetCategoryResponse.Information> list;
        Dialog dialog;

        CategoryAdapter(Context context, List<GetCategoryResponse.Information> list, Dialog dialog) {
            this.context = context;
            this.list = list;
            this.dialog = dialog;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.spinner_layout, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

            final GetCategoryResponse.Information item = list.get(i);

            viewHolder.text.setText(item.getCategoryName());

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(item.getImage(), viewHolder.image, options);


            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    category.setText(item.getCategoryName());

                    cat = String.valueOf(item.getCategoryId());

                    FragmentManager fm1 = getSupportFragmentManager();
                    FragmentTransaction ft1 = fm1.beginTransaction();
                    PagerFragment PagerFragment = new PagerFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("cat", cat);
                    bundle.putString("lat", latitude);
                    bundle.putString("lng", longitude);
                    bundle.putString("rad", rad);
                    bundle.putString("state", loca.getText().toString());
                    PagerFragment.setArguments(bundle);
                    ft1.replace(R.id.replace, PagerFragment);
                    ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    //ft.addToBackStack(null);
                    ft1.commit();
                    drawer.closeDrawer(GravityCompat.START);

                    dialog.dismiss();

                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView image;
            TextView text;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.imageView11);
                text = itemView.findViewById(R.id.textView34);
            }
        }
    }

    public void getAddress(Context context, double LATITUDE, double LONGITUDE) {

        //Set Address
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null && addresses.size() > 0) {


                address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getSubAdminArea();
                String area = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                Log.d(TAG, "getAddress:  address  " + address);
                Log.d(TAG, "getAddress:  city  " + city);
                Log.d(TAG, "getAddress:  state  " + area);
                Log.d(TAG, "getAddress:  postalCode  " + postalCode);
                Log.d(TAG, "getAddress:  knownName  " + knownName);

                addre.setText(city + " , " + state);

                loca.setText(city);
                //Toast.makeText(context, "" + address, Toast.LENGTH_SHORT).show();
                FragmentManager fm1 = getSupportFragmentManager();
                FragmentTransaction ft1 = fm1.beginTransaction();
                PagerFragment PagerFragment = new PagerFragment();
                Bundle bundle = new Bundle();
                bundle.putString("cat", cat);
                bundle.putString("lat", latitude);
                bundle.putString("lng", longitude);
                bundle.putString("rad", rad);
                bundle.putString("state", loca.getText().toString());
                PagerFragment.setArguments(bundle);
                ft1.replace(R.id.replace, PagerFragment);
                ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                //ft.addToBackStack(null);
                ft1.commit();
                drawer.closeDrawer(GravityCompat.START);


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
