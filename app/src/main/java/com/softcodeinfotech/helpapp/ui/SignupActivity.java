package com.softcodeinfotech.helpapp.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.softcodeinfotech.helpapp.R;
import com.softcodeinfotech.helpapp.ServiceInterface;
import com.softcodeinfotech.helpapp.beanresponse.GetmobileverifyResponse;
import com.softcodeinfotech.helpapp.util.Constant;
import com.softcodeinfotech.helpapp.util.DataValidation;
import com.softcodeinfotech.helpapp.util.SharePreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.facebook.appevents.UserDataStore.EMAIL;


public class SignupActivity extends AppCompatActivity {


    EditText mobile, password, username;
    Button submit;
    ImageButton back;
    TextView alreadymember;
    String mMobile, mPassword, mUsername;
    String val;
    ProgressBar pBar;

    //gmail facebook Integration

    ImageView gmail;
    LoginButton facebook;
    private static final int RC_SIGN_IN = 101;
    // CallbackManager callbackManager;

    private String TAG = "SignupActivity";

    //Google SignIn
    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    CallbackManager callbackManager;

    Retrofit retrofit;

    ConstraintLayout rootlayout;

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
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_signup);

        setUpWidget();
        getdata();

        pBar.setVisibility(View.GONE);


        //Retrofit
        //pBar.setVisibility(View.GONE);

        Gson gson = new GsonBuilder().create();

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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        alreadymember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signinIntent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(signinIntent);
                finish();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mMobile = mobile.getText().toString().trim();
                getdata();

                if (DataValidation.isValidPhoneNumber(mMobile)) {
                    Toast.makeText(SignupActivity.this, "Input valid mobile number", Toast.LENGTH_SHORT).show();
                } else if (mPassword.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Input valid password", Toast.LENGTH_SHORT).show();

                } else if (mUsername.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Input valid Username", Toast.LENGTH_SHORT).show();

                } else {
                    int randomPIN = (int) (Math.random() * 9000) + 1000;
                    val = "" + randomPIN;
                    pBar.setVisibility(View.VISIBLE);
                    sendDataReq();


                }
            }
        });


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        //gmail
        gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
                Toast.makeText(SignupActivity.this, "Gmail", Toast.LENGTH_SHORT).show();
            }
        });



        //facebook
        callbackManager = CallbackManager.Factory.create();
        facebook.setReadPermissions(Arrays.asList(EMAIL));
        facebook.setReadPermissions("email", "public_profile");

        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        facebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String userId = loginResult.getAccessToken().getUserId();
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                displayUserInfo(object);
                            }
                        });

                Bundle bundle = new Bundle();
                bundle.putString("fields", "first_name,last_name,email,id");
                graphRequest.setParameters(bundle);
                graphRequest.executeAsync();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private void displayUserInfo(JSONObject object) {
        String first_name = "";
        String last_name = "", email = "", id = "";

        try {
            first_name = object.getString("first_name");
            last_name = object.getString("last_name");
            email = object.getString("email");
            id = object.getString("id");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Toast.makeText(SignupActivity.this, "" + first_name + "" + last_name, Toast.LENGTH_SHORT).show();
        Toast.makeText(SignupActivity.this, "" + email, Toast.LENGTH_SHORT).show();

    }


    private void getdata() {
        mMobile = mobile.getText().toString().trim();
        mUsername = username.getText().toString().trim();
        mPassword = password.getText().toString().trim();

    }

    private void sendDataReq() {

        Call<GetmobileverifyResponse> call = serviceInterface.mobileVerify(convertPlainString(mMobile), convertPlainString(val), convertPlainString(mUsername), convertPlainString(mPassword));
        call.enqueue(new Callback<GetmobileverifyResponse>() {
            @Override
            public void onResponse(Call<GetmobileverifyResponse> call, Response<GetmobileverifyResponse> response) {

                if (response.body().getStatus() == 1) {
                    pBar.setVisibility(View.GONE);
                    SharePreferenceUtils.getInstance().saveString("USER_otp", response.body().getInformation().getOtp());
                    SharePreferenceUtils.getInstance().saveString("USER_mobile", response.body().getInformation().getMobile());
                    SharePreferenceUtils.getInstance().saveString("USER_name", response.body().getInformation().getName());
                    SharePreferenceUtils.getInstance().saveString("USER_password", response.body().getInformation().getPassword());

                    Intent intent = new Intent(SignupActivity.this, MailVerifyActivity.class);
                    intent.putExtra("mobile", mMobile);
                    intent.putExtra("otp", val);
                    startActivity(intent);
                    finish();


                    // Toast.makeText(SignupActivity.this, "" + response.body().getMsg() + " " + val, Toast.LENGTH_SHORT).show();
                } else {
                    pBar.setVisibility(View.GONE);

                    Toast.makeText(SignupActivity.this, "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<GetmobileverifyResponse> call, Throwable t) {
                pBar.setVisibility(View.GONE);
                Toast.makeText(SignupActivity.this, "" + t.toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void setUpWidget() {

        mobile = findViewById(R.id.editText);
        username = findViewById(R.id.editText3);
        password = findViewById(R.id.editText2);
        submit = findViewById(R.id.button3);
        alreadymember = findViewById(R.id.textView10);
        pBar = findViewById(R.id.progressBar2);
        back = findViewById(R.id.imageButton3);


        gmail = findViewById(R.id.imageView7);
        facebook = findViewById(R.id.login_button);
    }

    // convert aa param into plain text
    public RequestBody convertPlainString(String data) {
        RequestBody plainString = RequestBody.create(MediaType.parse("text/plain"), data);
        return plainString;
    }


    //gmail

    //firebase signin
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // Toast.makeText(SigninActivity.this, "sign in sucess", Toast.LENGTH_SHORT).show();
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    public void updateUI() {

        if (mAuth.getCurrentUser() != null) {
            FirebaseUser user = mAuth.getCurrentUser();
            String first_name, email, uid, phone_number, address;
            first_name = user.getDisplayName();
            email = user.getEmail();
            uid = user.getUid();
            // address=user.getPhotoUrl().toString();
            phone_number = user.getPhoneNumber();


            Uri photoUrl = user.getPhotoUrl();
            String abc = photoUrl.toString();

            // type = "Gmail";

            Toast.makeText(this, "name=" + first_name, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "email=" + email, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "phone=" + phone_number, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "" + photoUrl.toString(), Toast.LENGTH_SHORT).show();
            Log.v("uri", abc);


        }
    }


}
