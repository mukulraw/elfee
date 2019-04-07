package com.softcodeinfotech.helpapp;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.softcodeinfotech.helpapp.addHelpPOJO.addHelpBean;
import com.softcodeinfotech.helpapp.response.GetCategoryResponse;
import com.softcodeinfotech.helpapp.util.Constant;
import com.softcodeinfotech.helpapp.util.SharePreferenceUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class EditHelp extends AppCompatActivity {

    private static final String TAG = "editHelp";
    private static final int IMAGE_PICKER = 1;
    ImageButton back , delete;
    ProgressBar pBar;
    TextView spinCategory;
    EditText title, desc;
    //currentAddress;
    Button submit;

    String mUserid, mTitle, mDesc, mCatId, item2, mCurrentAddress;

    ArrayList<String> catId = new ArrayList<>();
    ArrayList<String> catName = new ArrayList<>();
    Retrofit retrofit;
    ServiceInterface serviceInterface;


    ImageView file1, file2, file3, file4, file5, file6, file7, file8, file9, file10;

    //Button selectImage;
    Uri uri1, uri2, uri3, uri4, uri5, uri6, uri7, uri8, uri9, uri10;

    String i1 , i2 , i3 , i4 , i5 , i6 , i7 , i8 , i9 ,i10;
    String tt , nn , helpId;

    TextView ptitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_help);

        catId = new ArrayList<>();
        catName = new ArrayList<>();

        setUpwidget();
        getData();
        pBar.setVisibility(View.GONE);


        mCatId = getIntent().getStringExtra("catId");
        helpId = getIntent().getStringExtra("helpId");
        spinCategory.setText(getIntent().getStringExtra("cat"));

        ptitle.setText("Edit " + getIntent().getStringExtra("cat") + " photos");

        i1 = getIntent().getStringExtra("i1");
        i2 = getIntent().getStringExtra("i2");
        i3 = getIntent().getStringExtra("i3");
        i4 = getIntent().getStringExtra("i4");
        i5 = getIntent().getStringExtra("i5");
        i6 = getIntent().getStringExtra("i6");
        i7 = getIntent().getStringExtra("i7");
        i8 = getIntent().getStringExtra("i8");
        i9 = getIntent().getStringExtra("i9");
        i10 = getIntent().getStringExtra("i10");

        tt = getIntent().getStringExtra("tt");
        nn = getIntent().getStringExtra("nn");

        title.setText(tt);
        desc.setText(nn);



        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).resetViewBeforeLoading(false).showImageForEmptyUri(R.drawable.addimg).build();

        ImageLoader loader = ImageLoader.getInstance();

        loader.displayImage(i1 , file1 , options);
        loader.displayImage(i2 , file2 , options);
        loader.displayImage(i3 , file3 , options);
        loader.displayImage(i4 , file4 , options);
        loader.displayImage(i5 , file5 , options);
        loader.displayImage(i6 , file6 , options);
        loader.displayImage(i7 , file7 , options);
        loader.displayImage(i8 , file8 , options);
        loader.displayImage(i9 , file9 , options);
        loader.displayImage(i10 , file10 , options);


        mUserid = SharePreferenceUtils.getInstance().getString("userId");
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

        spinCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(EditHelp.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dialog_layout);
                dialog.show();


                final RecyclerView grid = dialog.findViewById(R.id.grid);
                final GridLayoutManager manager = new GridLayoutManager(EditHelp.this, 3);

                String securecode = "1234";
                Call<GetCategoryResponse> call = serviceInterface.getCategory();
                call.enqueue(new Callback<GetCategoryResponse>() {
                    @Override
                    public void onResponse(Call<GetCategoryResponse> call, Response<GetCategoryResponse> response) {
                        if (response.body() != null && response.body().getStatus().equals("1")) {


                            CategoryAdapter adapter = new CategoryAdapter(EditHelp.this, response.body().getInformation(), dialog);
                            grid.setAdapter(adapter);
                            grid.setLayoutManager(manager);


                        } else {
                            Toast.makeText(EditHelp.this, "not inserted", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<GetCategoryResponse> call, Throwable t) {

                    }
                });

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
                if (mCatId.isEmpty()) {
                    Toast.makeText(EditHelp.this, "Invalid category", Toast.LENGTH_SHORT).show();
                }
                else if (mTitle.isEmpty()) {
                    Toast.makeText(EditHelp.this, "Invalid title", Toast.LENGTH_SHORT).show();
                } else if (mDesc.isEmpty()) {
                    Toast.makeText(EditHelp.this, "Invalid need", Toast.LENGTH_SHORT).show();
                } else {

                    pBar.setVisibility(View.VISIBLE);
                    getData();
                    //   Toast.makeText(EditHelp.this, ""+mCatId+""+mUserid+""+mTitle+""+
                    //          ""+mDesc+""+mState, Toast.LENGTH_SHORT).show();
                    //saveData();



                    MultipartBody.Part body1 = null;
                    MultipartBody.Part body2 = null;
                    MultipartBody.Part body3 = null;
                    MultipartBody.Part body4 = null;
                    MultipartBody.Part body5 = null;
                    MultipartBody.Part body6 = null;
                    MultipartBody.Part body7 = null;
                    MultipartBody.Part body8 = null;
                    MultipartBody.Part body9 = null;
                    MultipartBody.Part body10 = null;

                    try {

                        String ypath = getPath(EditHelp.this, uri1);
                        File f1 = new File(ypath);

                        RequestBody reqFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), f1);
                        body1 = MultipartBody.Part.createFormData("file1", f1.getName(), reqFile1);


                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    try {

                        String ypath = getPath(EditHelp.this, uri2);
                        File f2 = new File(ypath);

                        RequestBody reqFile2 = RequestBody.create(MediaType.parse("multipart/form-data"), f2);
                        body2 = MultipartBody.Part.createFormData("file2", f2.getName(), reqFile2);


                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    try {

                        String ypath = getPath(EditHelp.this, uri3);
                        File f3 = new File(ypath);

                        RequestBody reqFile3 = RequestBody.create(MediaType.parse("multipart/form-data"), f3);
                        body3 = MultipartBody.Part.createFormData("file3", f3.getName(), reqFile3);


                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    try {

                        String ypath = getPath(EditHelp.this, uri4);
                        File f4 = new File(ypath);

                        RequestBody reqFile4 = RequestBody.create(MediaType.parse("multipart/form-data"), f4);
                        body4 = MultipartBody.Part.createFormData("file4", f4.getName(), reqFile4);


                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    try {

                        String ypath = getPath(EditHelp.this, uri5);
                        File f5 = new File(ypath);

                        RequestBody reqFile5 = RequestBody.create(MediaType.parse("multipart/form-data"), f5);
                        body5 = MultipartBody.Part.createFormData("file5", f5.getName(), reqFile5);


                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    try {

                        String ypath = getPath(EditHelp.this, uri6);
                        File f6 = new File(ypath);

                        RequestBody reqFile6 = RequestBody.create(MediaType.parse("multipart/form-data"), f6);
                        body6 = MultipartBody.Part.createFormData("file6", f6.getName(), reqFile6);


                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    try {

                        String ypath = getPath(EditHelp.this, uri7);
                        File f7 = new File(ypath);

                        RequestBody reqFile7 = RequestBody.create(MediaType.parse("multipart/form-data"), f7);
                        body7 = MultipartBody.Part.createFormData("file7", f7.getName(), reqFile7);


                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    try {

                        String ypath = getPath(EditHelp.this, uri8);
                        File f8 = new File(ypath);

                        RequestBody reqFile8 = RequestBody.create(MediaType.parse("multipart/form-data"), f8);
                        body8 = MultipartBody.Part.createFormData("file8", f8.getName(), reqFile8);


                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    try {

                        String ypath = getPath(EditHelp.this, uri9);
                        File f9 = new File(ypath);

                        RequestBody reqFile9 = RequestBody.create(MediaType.parse("multipart/form-data"), f9);
                        body9 = MultipartBody.Part.createFormData("file9", f9.getName(), reqFile9);


                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    try {

                        String ypath = getPath(EditHelp.this, uri10);
                        File f10 = new File(ypath);

                        RequestBody reqFile10 = RequestBody.create(MediaType.parse("multipart/form-data"), f10);
                        body10 = MultipartBody.Part.createFormData("file10", f10.getName(), reqFile10);


                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }



                    Call<addHelpBean> call = serviceInterface.editHelp(
                            helpId,
                            mCatId,
                            mTitle,
                            mDesc,
                            body1,
                            body2,
                            body3,
                            body4,
                            body5,
                            body6,
                            body7,
                            body8,
                            body9,
                            body10
                    );

                    call.enqueue(new Callback<addHelpBean>() {
                        @Override
                        public void onResponse(Call<addHelpBean> call, Response<addHelpBean> response) {


                            if (response.body().getStatus().equals("1"))
                            {

                                Toast.makeText(EditHelp.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else
                            {
                                Toast.makeText(EditHelp.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }


                            pBar.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(Call<addHelpBean> call, Throwable t) {
                            pBar.setVisibility(View.GONE);
                        }
                    });


                    //saveUserData();
                }
            }
        });


        file1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = {"Take Photo from Camera",
                        "Choose from Gallery",
                        "Cancel"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EditHelp.this);
                builder.setTitle("Add Photo!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Take Photo from Camera")) {
                            Intent getpic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            getpic.putExtra(MediaStore.EXTRA_OUTPUT, uri1);
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

        file2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = {"Take Photo from Camera",
                        "Choose from Gallery",
                        "Cancel"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EditHelp.this);
                builder.setTitle("Add Photo!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Take Photo from Camera")) {
                            Intent getpic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            getpic.putExtra(MediaStore.EXTRA_OUTPUT, uri2);
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

        file3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = {"Take Photo from Camera",
                        "Choose from Gallery",
                        "Cancel"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EditHelp.this);
                builder.setTitle("Add Photo!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Take Photo from Camera")) {
                            Intent getpic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            getpic.putExtra(MediaStore.EXTRA_OUTPUT, uri3);
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

        file4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = {"Take Photo from Camera",
                        "Choose from Gallery",
                        "Cancel"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EditHelp.this);
                builder.setTitle("Add Photo!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Take Photo from Camera")) {
                            Intent getpic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            getpic.putExtra(MediaStore.EXTRA_OUTPUT, uri4);
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

        file5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = {"Take Photo from Camera",
                        "Choose from Gallery",
                        "Cancel"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EditHelp.this);
                builder.setTitle("Add Photo!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Take Photo from Camera")) {
                            Intent getpic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            getpic.putExtra(MediaStore.EXTRA_OUTPUT, uri5);
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

        file6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = {"Take Photo from Camera",
                        "Choose from Gallery",
                        "Cancel"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EditHelp.this);
                builder.setTitle("Add Photo!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Take Photo from Camera")) {
                            Intent getpic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            getpic.putExtra(MediaStore.EXTRA_OUTPUT, uri6);
                            startActivityForResult(getpic, 11);
                        } else if (items[item].equals("Choose from Gallery")) {
                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 12);
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();

            }
        });

        file7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = {"Take Photo from Camera",
                        "Choose from Gallery",
                        "Cancel"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EditHelp.this);
                builder.setTitle("Add Photo!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Take Photo from Camera")) {
                            Intent getpic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            getpic.putExtra(MediaStore.EXTRA_OUTPUT, uri7);
                            startActivityForResult(getpic, 13);
                        } else if (items[item].equals("Choose from Gallery")) {
                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 14);
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();

            }
        });

        file8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = {"Take Photo from Camera",
                        "Choose from Gallery",
                        "Cancel"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EditHelp.this);
                builder.setTitle("Add Photo!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Take Photo from Camera")) {
                            Intent getpic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            getpic.putExtra(MediaStore.EXTRA_OUTPUT, uri8);
                            startActivityForResult(getpic, 15);
                        } else if (items[item].equals("Choose from Gallery")) {
                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 16);
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();

            }
        });

        file9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = {"Take Photo from Camera",
                        "Choose from Gallery",
                        "Cancel"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EditHelp.this);
                builder.setTitle("Add Photo!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Take Photo from Camera")) {
                            Intent getpic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            getpic.putExtra(MediaStore.EXTRA_OUTPUT, uri9);
                            startActivityForResult(getpic, 17);
                        } else if (items[item].equals("Choose from Gallery")) {
                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 18);
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();

            }
        });

        file10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = {"Take Photo from Camera",
                        "Choose from Gallery",
                        "Cancel"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EditHelp.this);
                builder.setTitle("Add Photo!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Take Photo from Camera")) {
                            Intent getpic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            getpic.putExtra(MediaStore.EXTRA_OUTPUT, uri10);
                            startActivityForResult(getpic, 19);
                        } else if (items[item].equals("Choose from Gallery")) {
                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 20);
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pBar.setVisibility(View.VISIBLE);

                Call<addHelpBean> call = serviceInterface.deleteHelp(helpId);

                call.enqueue(new Callback<addHelpBean>() {
                    @Override
                    public void onResponse(Call<addHelpBean> call, Response<addHelpBean> response) {

                        Toast.makeText(EditHelp.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        if (response.body().getStatus().equals("1"))
                        {
                            finish();
                        }

                        pBar.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(Call<addHelpBean> call, Throwable t) {
                        pBar.setVisibility(View.GONE);
                    }
                });


            }
        });

    }

    private void setUpwidget() {
        back = findViewById(R.id.backButton);
        delete = findViewById(R.id.delete);
        pBar = findViewById(R.id.progressBar4);
        title = findViewById(R.id.editText7);
        desc = findViewById(R.id.editText8);
        submit = findViewById(R.id.button13);
        ptitle = findViewById(R.id.textView35);
        spinCategory = findViewById(R.id.recyclerView2);

        file1 = findViewById(R.id.file1);
        file2 = findViewById(R.id.file2);
        file3 = findViewById(R.id.file3);
        file4 = findViewById(R.id.file4);
        file5 = findViewById(R.id.file5);
        file6 = findViewById(R.id.file6);
        file7 = findViewById(R.id.file7);
        file8 = findViewById(R.id.file8);
        file9 = findViewById(R.id.file9);
        file10 = findViewById(R.id.file10);
        //currentAddress = findViewById(R.id.editText5);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && null != data) {

            uri1 = data.getData();

            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri1, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

            // Do something with the bitmap


            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close();

            file1.setImageBitmap(bitmap);


        } else if (requestCode == 4 && resultCode == RESULT_OK && null != data) {

            uri2 = data.getData();

            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri2, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

            // Do something with the bitmap


            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close();

            file2.setImageBitmap(bitmap);


        } else if (requestCode == 6 && resultCode == RESULT_OK && null != data) {

            uri3 = data.getData();

            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri3, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

            // Do something with the bitmap


            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close();

            file3.setImageBitmap(bitmap);


        } else if (requestCode == 8 && resultCode == RESULT_OK && null != data) {

            uri4 = data.getData();

            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri4, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

            // Do something with the bitmap


            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close();

            file4.setImageBitmap(bitmap);


        } else if (requestCode == 10 && resultCode == RESULT_OK && null != data) {

            uri5 = data.getData();

            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri5, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

            // Do something with the bitmap


            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close();

            file5.setImageBitmap(bitmap);


        } else if (requestCode == 12 && resultCode == RESULT_OK && null != data) {

            uri6 = data.getData();

            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri6, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

            // Do something with the bitmap


            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close();

            file6.setImageBitmap(bitmap);


        } else if (requestCode == 14 && resultCode == RESULT_OK && null != data) {

            uri7 = data.getData();

            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri7, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

            // Do something with the bitmap


            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close();

            file7.setImageBitmap(bitmap);


        } else if (requestCode == 16 && resultCode == RESULT_OK && null != data) {

            uri8 = data.getData();

            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri8, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

            // Do something with the bitmap


            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close();

            file8.setImageBitmap(bitmap);


        } else if (requestCode == 18 && resultCode == RESULT_OK && null != data) {

            uri9 = data.getData();

            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri9, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

            // Do something with the bitmap


            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close();

            file9.setImageBitmap(bitmap);


        } else if (requestCode == 20 && resultCode == RESULT_OK && null != data) {

            uri10 = data.getData();

            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri10, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

            // Do something with the bitmap


            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close();

            file10.setImageBitmap(bitmap);


        } else if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            file1.setImageBitmap(photo);
        } else if (requestCode == 3 && resultCode == RESULT_OK && null != data) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            file2.setImageBitmap(photo);
        } else if (requestCode == 5 && resultCode == RESULT_OK && null != data) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            file3.setImageBitmap(photo);
        } else if (requestCode == 7 && resultCode == RESULT_OK && null != data) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            file4.setImageBitmap(photo);
        } else if (requestCode == 9 && resultCode == RESULT_OK && null != data) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            file5.setImageBitmap(photo);
        } else if (requestCode == 11 && resultCode == RESULT_OK && null != data) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            file6.setImageBitmap(photo);
        } else if (requestCode == 13 && resultCode == RESULT_OK && null != data) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            file7.setImageBitmap(photo);
        } else if (requestCode == 15 && resultCode == RESULT_OK && null != data) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            file8.setImageBitmap(photo);
        } else if (requestCode == 17 && resultCode == RESULT_OK && null != data) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            file9.setImageBitmap(photo);
        } else if (requestCode == 19 && resultCode == RESULT_OK && null != data) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            file10.setImageBitmap(photo);
        }


    }
    class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

        Context context;
        List<GetCategoryResponse.Information> list = new ArrayList<>();
        Dialog dialog;

        public CategoryAdapter(Context context, List<GetCategoryResponse.Information> list, Dialog dialog) {
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

                    mCatId = String.valueOf(item.getCategoryId());
                    //item = String.valueOf(arg0.getItemAtPosition(position));

                    spinCategory.setText(item.getCategoryName());

                    ptitle.setText("Edit " + item.getCategoryName() + " photos");

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

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.imageView11);
                text = itemView.findViewById(R.id.textView34);
            }
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

}