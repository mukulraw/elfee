package com.softcodeinfotech.helpapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rilixtech.CountryCodePicker;
import com.softcodeinfotech.helpapp.ui.EditProfile;
import com.softcodeinfotech.helpapp.ui.MainActivity;
import com.softcodeinfotech.helpapp.util.Constant;
import com.softcodeinfotech.helpapp.util.SharePreferenceUtils;
import com.softcodeinfotech.helpapp.verifyPOJO.verifyBean;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class KYC extends AppCompatActivity {

    Toolbar toolbar;
    EditText name, phone, wphone, gname, gphone, aadhar, dob, profession, address;
    CountryCodePicker wcode, gcode;
    ImageView yimage, gimage, afront, aback, eimage;
    Button submit;

    Uri yuri, guri, afuri, aburi, euri;
    ProgressDialog progress;


    File yfile;

    File gfile;

    File affile;

    File abfile;

    File efile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String languageToLoad = SharePreferenceUtils.getInstance().getString("lang"); // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kyc2);

        toolbar = findViewById(R.id.toolbar);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        wphone = findViewById(R.id.wphone);
        gname = findViewById(R.id.gname);
        gphone = findViewById(R.id.gphone);
        aadhar = findViewById(R.id.aadhar);
        dob = findViewById(R.id.dob);
        profession = findViewById(R.id.profession);
        address = findViewById(R.id.address);
        wcode = findViewById(R.id.wcode);
        gcode = findViewById(R.id.gcode);
        yimage = findViewById(R.id.yimage);
        gimage = findViewById(R.id.gimage);
        afront = findViewById(R.id.afront);
        aback = findViewById(R.id.aback);
        eimage = findViewById(R.id.eimage);
        submit = findViewById(R.id.submit);
        progress = new ProgressDialog(this);
        progress.setMessage("Please wait...");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(false);


        wcode.registerPhoneNumberTextView(wphone);
        gcode.registerPhoneNumberTextView(gphone);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setNavigationIcon(R.drawable.arrow);

        toolbar.setTitle("Update Profile");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        aadhar.setMaxEms(12);

        phone.setText(SharePreferenceUtils.getInstance().getString(Constant.USER_mobile));

        yimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = {"Take Photo from Camera",
                        "Choose from Gallery",
                        "Cancel"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(KYC.this);
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

                            yuri = FileProvider.getUriForFile(KYC.this, BuildConfig.APPLICATION_ID + ".provider" , yfile);

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


        gimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = {"Take Photo from Camera",
                        "Choose from Gallery",
                        "Cancel"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(KYC.this);
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


                            gfile = new File(file);
                            try {
                                gfile.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            guri = FileProvider.getUriForFile(KYC.this, BuildConfig.APPLICATION_ID + ".provider" , gfile);

                            Intent getpic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            getpic.putExtra(MediaStore.EXTRA_OUTPUT, guri);
                            getpic.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(getpic, 3);

                        } else if (items[item].equals("Choose from Gallery")) {
                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 4);
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();

            }
        });

        afront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = {"Take Photo from Camera",
                        "Choose from Gallery",
                        "Cancel"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(KYC.this);
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


                            affile = new File(file);
                            try {
                                affile.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            afuri = FileProvider.getUriForFile(KYC.this, BuildConfig.APPLICATION_ID + ".provider" , affile);

                            Intent getpic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            getpic.putExtra(MediaStore.EXTRA_OUTPUT, afuri);
                            getpic.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(getpic, 5);

                        } else if (items[item].equals("Choose from Gallery")) {
                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 6);
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();

            }
        });

        aback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = {"Take Photo from Camera",
                        "Choose from Gallery",
                        "Cancel"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(KYC.this);
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


                            abfile = new File(file);
                            try {
                                abfile.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            aburi = FileProvider.getUriForFile(KYC.this, BuildConfig.APPLICATION_ID + ".provider" , abfile);

                            Intent getpic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            getpic.putExtra(MediaStore.EXTRA_OUTPUT, aburi);
                            getpic.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(getpic, 7);


                        } else if (items[item].equals("Choose from Gallery")) {
                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 8);
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();

            }
        });

        eimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = {"Take Photo from Camera",
                        "Choose from Gallery",
                        "Cancel"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(KYC.this);
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


                            efile = new File(file);
                            try {
                                efile.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            euri = FileProvider.getUriForFile(KYC.this, BuildConfig.APPLICATION_ID + ".provider" , efile);

                            Intent getpic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            getpic.putExtra(MediaStore.EXTRA_OUTPUT, euri);
                            getpic.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(getpic, 9);

                        } else if (items[item].equals("Choose from Gallery")) {
                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 10);
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String na = name.getText().toString();
                String ph = phone.getText().toString();
                String wp = wcode.getFullNumber();
                String gn = gname.getText().toString();
                String gp = gcode.getFullNumber();
                String aa = aadhar.getText().toString();
                String dobb = dob.getText().toString();
                String pr = profession.getText().toString();
                String ad = address.getText().toString();


                if (na.length() > 0) {


                    if (wp.length() > 0) {
                        if (gn.length() > 0) {


                            if (gp.length() > 0) {

                                if (aa.length() > 0) {


                                    if (dobb.length() > 0) {

                                        if (pr.length() > 0) {

                                            if (ad.length() > 0) {

                                                progress.show();



                                                // API here
                                                MultipartBody.Part body1 = null;
                                                MultipartBody.Part body2 = null;
                                                MultipartBody.Part body3 = null;
                                                MultipartBody.Part body4 = null;
                                                MultipartBody.Part body5 = null;


                                                try {
                                                    RequestBody reqFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), yfile);
                                                    body1 = MultipartBody.Part.createFormData("yimage", yfile.getName(), reqFile1);

                                                }catch (Exception e)
                                                {
                                                    e.printStackTrace();
                                                }

                                                try {

                                                    RequestBody reqFile2 = RequestBody.create(MediaType.parse("multipart/form-data"), gfile);
                                                    body2 = MultipartBody.Part.createFormData("gimage", gfile.getName(), reqFile2);

                                                }catch (Exception e)
                                                {
                                                    e.printStackTrace();
                                                }

                                                try {

                                                    RequestBody reqFile3 = RequestBody.create(MediaType.parse("multipart/form-data"), affile);
                                                    body3 = MultipartBody.Part.createFormData("afront", affile.getName(), reqFile3);

                                                }catch (Exception e)
                                                {
                                                    e.printStackTrace();
                                                }

                                                try {
                                                    RequestBody reqFile4 = RequestBody.create(MediaType.parse("multipart/form-data"), abfile);
                                                    body4 = MultipartBody.Part.createFormData("aback", abfile.getName(), reqFile4);

                                                }catch (Exception e)
                                                {
                                                    e.printStackTrace();
                                                }

                                                try {

                                                    RequestBody reqFile5 = RequestBody.create(MediaType.parse("multipart/form-data"), efile);
                                                    body5 = MultipartBody.Part.createFormData("eimage", efile.getName(), reqFile5);

                                                }catch (Exception e)
                                                {
e.printStackTrace();
                                                }




                                                Retrofit retrofit = new Retrofit.Builder()
                                                        .baseUrl(Constant.BASE_URL)
                                                        .addConverterFactory(ScalarsConverterFactory.create())
                                                        .addConverterFactory(GsonConverterFactory.create())
                                                        .build();

                                                ServiceInterface serviceInterface = retrofit.create(ServiceInterface.class);


                                                Call<verifyBean> call = serviceInterface.updateKYC(
                                                        SharePreferenceUtils.getInstance().getString("userId"),
                                                        na,
                                                        dobb,
                                                        aa,
                                                        ad,
                                                        wp,
                                                        gn,
                                                        gp,
                                                        pr,
                                                        body1,
                                                        body2,
                                                        body3,
                                                        body4,
                                                        body5
                                                );

                                                call.enqueue(new Callback<verifyBean>() {
                                                    @Override
                                                    public void onResponse(Call<verifyBean> call, Response<verifyBean> response) {


                                                        if (response.body().getStatus().equals("1")) {

                                                            Toast.makeText(KYC.this, "Profile updated successfully", Toast.LENGTH_LONG).show();

                                                            SharePreferenceUtils.getInstance().saveString("userId", response.body().getData().getUserId());
                                                            SharePreferenceUtils.getInstance().saveString("name", response.body().getData().getName());
                                                            SharePreferenceUtils.getInstance().saveString("dob", response.body().getData().getDob());
                                                            SharePreferenceUtils.getInstance().saveString("aadhar", response.body().getData().getAadhar());
                                                            SharePreferenceUtils.getInstance().saveString("address", response.body().getData().getAddress());
                                                            SharePreferenceUtils.getInstance().saveString("kyc_status", response.body().getData().getKycStatus());
                                                            SharePreferenceUtils.getInstance().saveString("wphone", response.body().getData().getWphone());
                                                            SharePreferenceUtils.getInstance().saveString("g_name", response.body().getData().getGName());
                                                            SharePreferenceUtils.getInstance().saveString("gphone", response.body().getData().getGphone());
                                                            SharePreferenceUtils.getInstance().saveString("profession", response.body().getData().getProfession());
                                                            SharePreferenceUtils.getInstance().saveString("yimage", response.body().getData().getYimage());
                                                            SharePreferenceUtils.getInstance().saveString("gimage", response.body().getData().getGimage());
                                                            SharePreferenceUtils.getInstance().saveString("afront", response.body().getData().getAfront());
                                                            SharePreferenceUtils.getInstance().saveString("aback", response.body().getData().getAback());
                                                            SharePreferenceUtils.getInstance().saveString("eimage", response.body().getData().getEimage());

                                                            Intent intent = new Intent(KYC.this, MainActivity.class);
                                                            startActivity(intent);
                                                            finishAffinity();

                                                        } else {
                                                            Toast.makeText(KYC.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }

                                                        progress.dismiss();
                                                    }

                                                    @Override
                                                    public void onFailure(Call<verifyBean> call, Throwable t) {
                                                        progress.dismiss();
                                                    }
                                                });


                                            } else {
                                                Toast.makeText(KYC.this, "Invalid address", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(KYC.this, "Invalid profession", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(KYC.this, "Invalid D.O.B.", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(KYC.this, "Invalid aadhar number", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(KYC.this, "Invalid guardian number", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(KYC.this, "Invalid guardian name", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(KYC.this, "Invalid whatsapp number", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(KYC.this, "Invalid name", Toast.LENGTH_SHORT).show();
                }


            }
        });


        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(KYC.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dob_popup);
                dialog.setCancelable(true);
                dialog.show();

                Button submit = dialog.findViewById(R.id.button11);
                final DatePicker dp = dialog.findViewById(R.id.view14);

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String dd = String.valueOf(dp.getDayOfMonth()) + "-" + String.valueOf(dp.getMonth() + 1) + "-" + dp.getYear();

                        Log.d("dddd", dd);

                        dob.setText(dd);

                        dialog.dismiss();

                    }
                });

            }
        });

        loadData();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 2 && resultCode == RESULT_OK && null != data) {

            yuri = data.getData();

            String ypath = getPath(KYC.this, yuri);
            yfile = new File(ypath);

            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(yuri, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

            // Do something with the bitmap


            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close();

            yimage.setImageBitmap(bitmap);


        } else if (requestCode == 4 && resultCode == RESULT_OK && null != data) {

            guri = data.getData();

            String ypath = getPath(KYC.this, guri);
            gfile = new File(ypath);


            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(guri, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

            // Do something with the bitmap


            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close();

            gimage.setImageBitmap(bitmap);

        } else if (requestCode == 6 && resultCode == RESULT_OK && null != data) {

            afuri = data.getData();

            String ypath = getPath(KYC.this, afuri);
            affile = new File(ypath);


            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(afuri, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

            // Do something with the bitmap


            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close();

            afront.setImageBitmap(bitmap);

        } else if (requestCode == 8 && resultCode == RESULT_OK && null != data) {
            aburi = data.getData();
            String ypath = getPath(KYC.this, aburi);
            abfile = new File(ypath);

            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(aburi, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

            // Do something with the bitmap


            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close();

            aback.setImageBitmap(bitmap);

        } else if (requestCode == 10 && resultCode == RESULT_OK && null != data) {
            euri = data.getData();
            String ypath = getPath(KYC.this, euri);
            efile = new File(ypath);

            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(euri, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

            // Do something with the bitmap


            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close();

            eimage.setImageBitmap(bitmap);

        } else if (requestCode == 1 && resultCode == RESULT_OK) {

            yimage.setImageURI(yuri);

        } else if (requestCode == 3 && resultCode == RESULT_OK) {



            gimage.setImageURI(guri);

        } else if (requestCode == 5 && resultCode == RESULT_OK) {


            afront.setImageURI(afuri);

        } else if (requestCode == 7 && resultCode == RESULT_OK) {


            aback.setImageURI(aburi);

        } else if (requestCode == 9 && resultCode == RESULT_OK) {


            eimage.setImageURI(euri);

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

    void loadData() {

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
        loader.displayImage(SharePreferenceUtils.getInstance().getString("gimage"), gimage, options);
        loader.displayImage(SharePreferenceUtils.getInstance().getString("afront"), afront, options);
        loader.displayImage(SharePreferenceUtils.getInstance().getString("aback"), aback, options);
        loader.displayImage(SharePreferenceUtils.getInstance().getString("eimage"), eimage, options);


    }

}

