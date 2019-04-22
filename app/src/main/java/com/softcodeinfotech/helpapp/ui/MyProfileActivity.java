package com.softcodeinfotech.helpapp.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.softcodeinfotech.helpapp.BuildConfig;
import com.softcodeinfotech.helpapp.EditHelp;
import com.softcodeinfotech.helpapp.KYC;
import com.softcodeinfotech.helpapp.R;
import com.softcodeinfotech.helpapp.ServiceInterface;
import com.softcodeinfotech.helpapp.util.Constant;
import com.softcodeinfotech.helpapp.util.SharePreferenceUtils;
import com.softcodeinfotech.helpapp.verifyPOJO.verifyBean;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MyProfileActivity extends AppCompatActivity {

    ImageButton back;

    String mName, mAge, mGender, mEmail, url;

    TextView edit;

    ImageView image;
    ProgressDialog progress;

    static final int RC_TAKE_PHOTO = 1;

    private final int PICK_IMAGE_REQUEST = 2;

    TextView name, phone, wphone, gname, gphone, aadhar, dob, profession, address;
    ImageView yimage, gimage, afront, aback, eimage;

    File yfile;

    Uri yuri;

    TextView change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String languageToLoad  = SharePreferenceUtils.getInstance().getString("lang"); // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);


        setUpWidget();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MyProfileActivity.this, KYC.class);
                startActivity(i);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        mName = SharePreferenceUtils.getInstance().getString("name");
        mAge = SharePreferenceUtils.getInstance().getString("dob");
        mGender = SharePreferenceUtils.getInstance().getString(Constant.USER_mobile);
        mEmail = SharePreferenceUtils.getInstance().getString(Constant.USER_email);
        url = SharePreferenceUtils.getInstance().getString("yimage");

        loadData();

        //for default placeholder image in glide
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.noimage);
        requestOptions.error(R.drawable.noimage);

        Glide.with(this).setDefaultRequestOptions(requestOptions).load(url).into(image);


        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final CharSequence[] items = {"Take Photo from Camera",
                        "Choose from Gallery",
                        "Cancel"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MyProfileActivity.this);
                builder.setTitle("Add Photo!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Take Photo from Camera")) {

                            final String dir =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ "/Folder/";
                            File newdir = new File(dir);
                            try {
                                newdir.mkdirs();
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }


                            String file = dir+ DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString()+".jpg";


                            yfile = new File(file);
                            try {
                                yfile.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            yuri = FileProvider.getUriForFile(MyProfileActivity.this, BuildConfig.APPLICATION_ID + ".provider" , yfile);

                            Intent getpic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            getpic.putExtra(MediaStore.EXTRA_OUTPUT, yuri);
                            getpic.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(getpic, 1);

                        } else if (items[item].equals("Choose from Gallery")) {
                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 2);
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();

            }
        });

    }

    private void setUpWidget() {

        back = findViewById(R.id.imageButton4);

        image = findViewById(R.id.imageView6);
        edit = findViewById(R.id.textView33);
        change = findViewById(R.id.textView45);
        progress = new ProgressDialog(this);

        progress.setMessage("Please wait...");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(false);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        wphone = findViewById(R.id.wphone);
        gname = findViewById(R.id.gname);
        gphone = findViewById(R.id.gphone);
        aadhar = findViewById(R.id.aadhar);
        dob = findViewById(R.id.dob);
        profession = findViewById(R.id.profession);
        address = findViewById(R.id.address);
        yimage = findViewById(R.id.yimage);
        gimage = findViewById(R.id.gimage);
        afront = findViewById(R.id.afront);
        aback = findViewById(R.id.aback);
        eimage = findViewById(R.id.eimage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK ) {

            progress.show();

            MultipartBody.Part body1 = null;

            RequestBody reqFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), yfile);
            body1 = MultipartBody.Part.createFormData("yimage", yfile.getName(), reqFile1);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ServiceInterface serviceInterface = retrofit.create(ServiceInterface.class);

            Call<verifyBean> call = serviceInterface.updateProfilePic(SharePreferenceUtils.getInstance().getString("userId") , body1);
            call.enqueue(new Callback<verifyBean>() {
                @Override
                public void onResponse(Call<verifyBean> call, Response<verifyBean> response) {

                    if (response.body().getStatus().equals("1"))
                    {

                        SharePreferenceUtils.getInstance().saveString("userId" , response.body().getData().getUserId());
                        SharePreferenceUtils.getInstance().saveString("name" , response.body().getData().getName());
                        SharePreferenceUtils.getInstance().saveString("dob" , response.body().getData().getDob());
                        SharePreferenceUtils.getInstance().saveString("aadhar" , response.body().getData().getAadhar());
                        SharePreferenceUtils.getInstance().saveString("address" , response.body().getData().getAddress());
                        SharePreferenceUtils.getInstance().saveString("kyc_status" , response.body().getData().getKycStatus());
                        SharePreferenceUtils.getInstance().saveString("wphone" , response.body().getData().getWphone());
                        SharePreferenceUtils.getInstance().saveString("g_name" , response.body().getData().getGName());
                        SharePreferenceUtils.getInstance().saveString("gphone" , response.body().getData().getGphone());
                        SharePreferenceUtils.getInstance().saveString("profession" , response.body().getData().getProfession());
                        SharePreferenceUtils.getInstance().saveString("yimage" , response.body().getData().getYimage());
                        SharePreferenceUtils.getInstance().saveString("gimage" , response.body().getData().getGimage());
                        SharePreferenceUtils.getInstance().saveString("afront" , response.body().getData().getAfront());
                        SharePreferenceUtils.getInstance().saveString("aback" , response.body().getData().getAback());
                        SharePreferenceUtils.getInstance().saveString("eimage" , response.body().getData().getEimage());


                        url = SharePreferenceUtils.getInstance().getString("yimage");


                        //for default placeholder image in glide
                        RequestOptions requestOptions = new RequestOptions();
                        requestOptions.placeholder(R.drawable.noimage);
                        requestOptions.error(R.drawable.noimage);

                        Glide.with(MyProfileActivity.this).setDefaultRequestOptions(requestOptions).load(url).into(image);


                    }
                    else
                    {
                     //   Toast.makeText(EditProfile.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<verifyBean> call, Throwable t) {
progress.dismiss();
                }
            });

        }
        else if (requestCode == 2 && resultCode == RESULT_OK && null != data)
        {

            progress.show();
            yuri = data.getData();
            String ypath = getPath(MyProfileActivity.this, yuri);
            File yfile = new File(ypath);
            MultipartBody.Part body1 = null;
            RequestBody reqFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), yfile);
            body1 = MultipartBody.Part.createFormData("yimage", yfile.getName(), reqFile1);


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ServiceInterface serviceInterface = retrofit.create(ServiceInterface.class);
            Call<verifyBean> call = serviceInterface.updateProfilePic(SharePreferenceUtils.getInstance().getString("userId") , body1);
            call.enqueue(new Callback<verifyBean>() {
                @Override
                public void onResponse(Call<verifyBean> call, Response<verifyBean> response) {

                    if (response.body().getStatus().equals("1"))
                    {

                        SharePreferenceUtils.getInstance().saveString("userId" , response.body().getData().getUserId());
                        SharePreferenceUtils.getInstance().saveString("name" , response.body().getData().getName());
                        SharePreferenceUtils.getInstance().saveString("dob" , response.body().getData().getDob());
                        SharePreferenceUtils.getInstance().saveString("aadhar" , response.body().getData().getAadhar());
                        SharePreferenceUtils.getInstance().saveString("address" , response.body().getData().getAddress());
                        SharePreferenceUtils.getInstance().saveString("kyc_status" , response.body().getData().getKycStatus());
                        SharePreferenceUtils.getInstance().saveString("wphone" , response.body().getData().getWphone());
                        SharePreferenceUtils.getInstance().saveString("g_name" , response.body().getData().getGName());
                        SharePreferenceUtils.getInstance().saveString("gphone" , response.body().getData().getGphone());
                        SharePreferenceUtils.getInstance().saveString("profession" , response.body().getData().getProfession());
                        SharePreferenceUtils.getInstance().saveString("yimage" , response.body().getData().getYimage());
                        SharePreferenceUtils.getInstance().saveString("gimage" , response.body().getData().getGimage());
                        SharePreferenceUtils.getInstance().saveString("afront" , response.body().getData().getAfront());
                        SharePreferenceUtils.getInstance().saveString("aback" , response.body().getData().getAback());
                        SharePreferenceUtils.getInstance().saveString("eimage" , response.body().getData().getEimage());


                        url = SharePreferenceUtils.getInstance().getString("yimage");


                        //for default placeholder image in glide
                        RequestOptions requestOptions = new RequestOptions();
                        requestOptions.placeholder(R.drawable.noimage);
                        requestOptions.error(R.drawable.noimage);

                        Glide.with(MyProfileActivity.this).setDefaultRequestOptions(requestOptions).load(url).into(image);


                    }
                    else
                    {
                        //   Toast.makeText(EditProfile.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<verifyBean> call, Throwable t) {
                    progress.dismiss();
                }
            });


        }

    }

    private static String getPath(final Context context, final Uri uri) {
        final boolean isKitKatOrAbove = true;

        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    void loadData()
    {

        name.setText(SharePreferenceUtils.getInstance().getString("name"));
        phone.setText(SharePreferenceUtils.getInstance().getString(Constant.USER_mobile));
        wphone.setText(SharePreferenceUtils.getInstance().getString("wphone"));
        gname.setText(SharePreferenceUtils.getInstance().getString("g_name"));
        gphone.setText(SharePreferenceUtils.getInstance().getString("gphone"));
        aadhar.setText(SharePreferenceUtils.getInstance().getString("aadhar"));
        dob.setText(SharePreferenceUtils.getInstance().getString("dob"));
        profession.setText(SharePreferenceUtils.getInstance().getString("profession"));
        address.setText(SharePreferenceUtils.getInstance().getString("address"));


        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
        ImageLoader loader = ImageLoader.getInstance();
        loader.displayImage(SharePreferenceUtils.getInstance().getString("gimage") , gimage , options);
        loader.displayImage(SharePreferenceUtils.getInstance().getString("afront") , afront , options);
        loader.displayImage(SharePreferenceUtils.getInstance().getString("aback") , aback , options);
        loader.displayImage(SharePreferenceUtils.getInstance().getString("eimage") , eimage , options);


    }

}
