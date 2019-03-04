package com.softcodeinfotech.helpapp.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.CursorLoader;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
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
    TextView spinCategory;
    EditText title, desc;
            //currentAddress;
    Button submit;

    String mUserid, mTitle, mDesc, mCatId, mState, item2, mCurrentAddress;

    ArrayList<String> catId = new ArrayList<>();
    ArrayList<String> catName = new ArrayList<>();
    Retrofit retrofit;
    ServiceInterface serviceInterface;
    //location
    String lati = "0", longi = "0";
    String address = "";

    //imageview
    ImageView imageView;
    //Button selectImage;
    Uri selectedImage;
    File compressedImageFile;

    File getImageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_help);
        catId = new ArrayList<>();
        catName = new ArrayList<>();

        setUpwidget();
        getData();
        pBar.setVisibility(View.GONE);

        Intent intent = getIntent();
        lati = intent.getStringExtra("lati");
        longi = intent.getStringExtra("longi");

        //Toast.makeText(this, "" + lati + "" + longi, Toast.LENGTH_SHORT).show();
        try {
            getAddress(AddHelpActivity.this, Double.parseDouble(lati), Double.parseDouble(longi));
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        //currentAddress.setText(address);
        mCurrentAddress = address;


        mUserid = SharePreferenceUtils.getInstance().getString(Constant.USER_id);
        //mState = SharePreferenceUtils.getInstance().getString(Constant.USER_state);

        //Toast.makeText(this, ""+mUserid, Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, ""+mState, Toast.LENGTH_SHORT).show();


        //okhttp client
        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        serviceInterface = retrofit.create(ServiceInterface.class);


        //getCategoryReq();


        //if you want to set any action you can do in this listener
        /*spinCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        });*/

        spinCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(AddHelpActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dialog_layout);
                dialog.show();


                final RecyclerView grid = dialog.findViewById(R.id.grid);
                final GridLayoutManager manager = new GridLayoutManager(AddHelpActivity.this , 3);

                String securecode = "1234";
                Call<GetCategoryResponse> call = serviceInterface.getCategory(convertPlainString(securecode));
                call.enqueue(new Callback<GetCategoryResponse>() {
                    @Override
                    public void onResponse(Call<GetCategoryResponse> call, Response<GetCategoryResponse> response) {
                        if (response.body() != null && response.body().getStatus().equals(1)) {


                            CategoryAdapter adapter = new CategoryAdapter(AddHelpActivity.this , response.body().getInformation() , dialog);
                            grid.setAdapter(adapter);
                            grid.setLayoutManager(manager);


                        } else {
                            Toast.makeText(AddHelpActivity.this, "not inserted", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<GetCategoryResponse> call, Throwable t) {

                    }
                });

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

        imageView.setOnClickListener(new View.OnClickListener() {
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



    }

    private void setUpwidget() {
        back = findViewById(R.id.backButton);
        pBar = findViewById(R.id.progressBar4);
        title = findViewById(R.id.editText7);
        desc = findViewById(R.id.editText8);
        submit = findViewById(R.id.button13);
        spinCategory = findViewById(R.id.recyclerView2);
        //currentAddress = findViewById(R.id.editText5);
        //
        imageView = findViewById(R.id.imageView12);
        //selectImage = findViewById(R.id.selectImage);
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

                mState = city;

                //Toast.makeText(context, "" + address, Toast.LENGTH_SHORT).show();


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
                convertPlainString(address), convertPlainString(lati), convertPlainString(longi));

        call.enqueue(new Callback<AddHelpListResponse>() {
            @Override
            public void onResponse(Call<AddHelpListResponse> call, Response<AddHelpListResponse> response) {
                pBar.setVisibility(View.GONE);
                if (response.body() != null) {
                    if (response.body().getStatus().equals(1)) {
                        title.setText("");
                        desc.setText("");
                        Toast.makeText(AddHelpActivity.this, "help data added successfully", Toast.LENGTH_SHORT).show();
                        finish();
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

    class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>
    {

        Context context;
        List<GetCategoryResponse.Information> list = new ArrayList<>();
        Dialog dialog;

        public CategoryAdapter(Context context , List<GetCategoryResponse.Information> list , Dialog dialog)
        {
            this.context = context;
            this.list = list;
            this.dialog = dialog;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.spinner_layout , viewGroup , false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

            final GetCategoryResponse.Information item = list.get(i);

            viewHolder.text.setText(item.getCategoryName());

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(Constant.BASE_URL + "helpapp/admin/upload/streams/" + item.getImage() , viewHolder.image , options);


            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mCatId = String.valueOf(item.getCategoryId());
                    //item = String.valueOf(arg0.getItemAtPosition(position));

                    spinCategory.setText(item.getCategoryName());

                    dialog.dismiss();

                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {

            ImageView image;
            TextView text;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.imageView11);
                text = itemView.findViewById(R.id.textView34);
            }
        }
    }

}
