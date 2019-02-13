package com.softcodeinfotech.helpapp.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.softcodeinfotech.helpapp.R;
import com.softcodeinfotech.helpapp.ServiceInterface;
import com.softcodeinfotech.helpapp.beanresponse.AddHelpListResponse;
import com.softcodeinfotech.helpapp.response.GetCategoryResponse;
import com.softcodeinfotech.helpapp.response.HelpDataInsertResponse;
import com.softcodeinfotech.helpapp.util.Constant;
import com.softcodeinfotech.helpapp.util.SharePreferenceUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AddHelpActivity extends AppCompatActivity {
    private static final String TAG = "addHelp";
    private static final int IMAGE_PICKER = 1;
    ImageButton back;
    ProgressBar pBar;
    Spinner spinCategory;
    EditText title, desc, currentAddress;
    Button submit;

    String mUserid, mTitle, mDesc, mCatId, mState, item, mCurrentAddress;

    ArrayList<String> catId = new ArrayList<>();
    ArrayList<String> catName = new ArrayList<>();
    Retrofit retrofit;
    ServiceInterface serviceInterface;
    //location
    String lati = "0", longi = "0";
    String address = "";

    //imageview
    ImageView imageView;
    Button selectImage;
    Uri selectedImage;
    File compressedImageFile;

    File getImageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_help);
        catId = new ArrayList<>();
        catName = new ArrayList<>();

        setUpwidget();
        getData();
        pBar.setVisibility(View.GONE);

        Intent intent = getIntent();
        lati = intent.getStringExtra("lati");
        longi = intent.getStringExtra("longi");

        Toast.makeText(this, "" + lati + "" + longi, Toast.LENGTH_SHORT).show();
        try {
            getAddress(AddHelpActivity.this, Double.parseDouble(lati), Double.parseDouble(longi));
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        currentAddress.setText(address);
        mCurrentAddress = currentAddress.getText().toString().trim();


        mUserid = SharePreferenceUtils.getInstance().getString(Constant.USER_id);
        mState = SharePreferenceUtils.getInstance().getString(Constant.USER_state);

        //Toast.makeText(this, ""+mUserid, Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, ""+mState, Toast.LENGTH_SHORT).show();


        //okhttp client
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


        getCategoryReq();


        //if you want to set any action you can do in this listener
        spinCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {

                mCatId = catId.get(position);
                item = String.valueOf(arg0.getItemAtPosition(position));
                // Toast.makeText(AddNoteActivity.this, ""+item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
                if (mTitle.isEmpty()) {
                    Toast.makeText(AddHelpActivity.this, "Fill Title", Toast.LENGTH_SHORT).show();
                } else if (mDesc.isEmpty()) {
                    Toast.makeText(AddHelpActivity.this, "Fill Desc", Toast.LENGTH_SHORT).show();
                } else {

                    pBar.setVisibility(View.VISIBLE);
                    getData();
                    //   Toast.makeText(AddHelpActivity.this, ""+mCatId+""+mUserid+""+mTitle+""+
                    //          ""+mDesc+""+mState, Toast.LENGTH_SHORT).show();
                    //saveData();

                    saveUserData();
                }
            }
        });

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();

            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void saveData() {
        Call<HelpDataInsertResponse> call = serviceInterface.helpDataInsert(convertPlainString(mUserid),
                convertPlainString(mTitle), convertPlainString(mDesc), convertPlainString(mCatId), convertPlainString(mState));
        call.enqueue(new Callback<HelpDataInsertResponse>() {
            @Override
            public void onResponse(Call<HelpDataInsertResponse> call, Response<HelpDataInsertResponse> response) {
                pBar.setVisibility(View.GONE);
                if (response.body() != null && response.body().getStatus().equals(1)) {
                    title.setText("");
                    desc.setText("");
                    Toast.makeText(AddHelpActivity.this, "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddHelpActivity.this, "not inserted", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HelpDataInsertResponse> call, Throwable t) {
                pBar.setVisibility(View.GONE);
                Toast.makeText(AddHelpActivity.this, "network issue", Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void getCategoryReq() {
        String securecode = "1234";
        Call<GetCategoryResponse> call = serviceInterface.getCategory(convertPlainString(securecode));
        call.enqueue(new Callback<GetCategoryResponse>() {
            @Override
            public void onResponse(Call<GetCategoryResponse> call, Response<GetCategoryResponse> response) {
                if (response.body() != null && response.body().getStatus().equals(1)) {
                    for (int i = 0; i < response.body().getInformation().size(); i++) {
                        catId.add(String.valueOf(response.body().getInformation().get(i).getCategoryId()));
                        catName.add(response.body().getInformation().get(i).getCategoryName());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddHelpActivity.this,
                            android.R.layout.simple_spinner_item, catName);//setting the country_array to spinner
                    // string value
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinCategory.setAdapter(adapter);
                } else {
                    Toast.makeText(AddHelpActivity.this, "not inserted", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetCategoryResponse> call, Throwable t) {

            }
        });


    }

    private void setUpwidget() {
        back = findViewById(R.id.backButton);
        pBar = findViewById(R.id.progressBar);
        title = findViewById(R.id.title_text);
        desc = findViewById(R.id.desc);
        submit = findViewById(R.id.submit);
        spinCategory = findViewById(R.id.spinCategory);
        currentAddress = findViewById(R.id.editText5);
        //
        imageView = findViewById(R.id.imageView8);
        selectImage = findViewById(R.id.selectImage);
    }

    private void getData() {
        mTitle = title.getText().toString().trim();
        mDesc = desc.getText().toString().trim();
    }


    // convert aa param into plain text
    public RequestBody convertPlainString(String data) {
        return RequestBody.create(MediaType.parse("text/plain"), data);
    }

    //
    public void getAddress(Context context, double LATITUDE, double LONGITUDE) {

        //Set Address
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null && addresses.size() > 0) {


                address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                Log.d(TAG, "getAddress:  address" + address);
                Log.d(TAG, "getAddress:  city" + city);
                Log.d(TAG, "getAddress:  state" + state);
                Log.d(TAG, "getAddress:  postalCode" + postalCode);
                Log.d(TAG, "getAddress:  knownName" + knownName);

                Toast.makeText(context, "" + address, Toast.LENGTH_SHORT).show();


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

    //image load
    private void OpenGallery() {
        //opening file chooser
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            //the image URI
            selectedImage = data.getData();
            Log.i("image", selectedImage.toString());

            imageView.setImageURI(selectedImage);
/*
            //calling the upload file method after choosing the file
            uploadFile(selectedImage, "My Image");*/
        }
    }

    //
    private void saveUserData() {
        File file = new File(getRealPathFromURI(selectedImage));
        try {
            compressedImageFile = new Compressor(this).compressToFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(selectedImage)), file);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), compressedImageFile);

        Call<AddHelpListResponse> call = serviceInterface.help_DataInsert(convertPlainString(mUserid),
                convertPlainString(mTitle), convertPlainString(mDesc), convertPlainString(mCatId), convertPlainString(mState), requestFile,
                address, lati, longi);

        call.enqueue(new Callback<AddHelpListResponse>() {
            @Override
            public void onResponse(Call<AddHelpListResponse> call, Response<AddHelpListResponse> response) {
                pBar.setVisibility(View.GONE);
                if (response.body() != null) {
                    if (response.body().getStatus().equals(1)) {
                        title.setText("");
                        desc.setText("");
                        Toast.makeText(AddHelpActivity.this, "help data added sucessfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("error_in upload", "not uploaded");

                    }
                } else {
                    Toast.makeText(AddHelpActivity.this, "not inserted", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddHelpListResponse> call, Throwable t) {
                pBar.setVisibility(View.GONE);
                Toast.makeText(AddHelpActivity.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
                Log.e("error", t.toString());

            }
        });
    }


    //This method is fetching the absolute path of the image file
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

}
