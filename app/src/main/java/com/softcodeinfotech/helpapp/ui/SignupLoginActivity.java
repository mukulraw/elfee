package com.softcodeinfotech.helpapp.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.softcodeinfotech.helpapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;

public class SignupLoginActivity extends AppCompatActivity {

    Button Signup, Login;

    TextView skip;

    Button google, facebook;

    GoogleSignInClient mGoogleSignInClient;

    int RC_SIGN_IN = 12;

    private CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this.getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Log.d("Success", "Login");
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        Log.i("MainActivity", "@@@response: " + response.toString());

                                        try {

                                            final String name = object.getString("name");
                                            final String id = object.getString("id");
                                            final String email = object.getString("email");



                                            Log.d("name" , object.getString("name"));
                                            Log.d("id" , object.getString("id"));
                                            Log.d("email" , object.getString("email"));

                                            //socialSignin(name , id , email);




                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender,birthday"); // id,first_name,last_name,email,gender,birthday,cover,picture.type(large)
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {

                       Log.d("failure" ,exception.toString());
                        // App code
                    }
                });

        
        setContentView(R.layout.activity_signup_login);

        google = findViewById(R.id.button4);

        facebook = findViewById(R.id.button9);

        Signup = findViewById(R.id.button);

        Login = findViewById(R.id.button2);

        skip = findViewById(R.id.textView15);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupIntent = new Intent(SignupLoginActivity.this, SignupActivity.class);
                startActivity(signupIntent);
                // finish();

            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent loginIntent = new Intent(SignupLoginActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                //finish();

            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent mainIntent = new Intent(SignupLoginActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finishAffinity();

            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                signIn();

            }
        });


        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               //LoginManager.getInstance().logInWithReadPermissions(SignupLoginActivity.this, Arrays.asList("public_profile"));
                LoginManager.getInstance().logInWithReadPermissions(SignupLoginActivity.this, Collections.singletonList("public_profile"));




            }
        });


        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException ignored) {

        }


    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            String email = account.getEmail();
            String id = account.getId();


            Log.d("email", account.getEmail());
            Log.d("id", account.getId());
            // Signed in successfully, show authenticated UI.
            //updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("response", "signInResult:failed code=" + e.toString());
            //updateUI(null);
        }
    }

    private void displayUserInfo(JSONObject object) {
        String first_name = "";
        String last_name = "", email = "", id = "";

        try {
            first_name = object.getString("first_name");
            last_name = object.getString("last_name");
            email = object.getString("email");
            id = object.getString("id");

            Log.d("name" , object.getString("first_name"));
            Log.d("last" , object.getString("last_name"));
            Log.d("email" , object.getString("email"));
            Log.d("id" , object.getString("id"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Toast.makeText(SignupLoginActivity.this, "" + first_name + "" + last_name, Toast.LENGTH_SHORT).show();
        Toast.makeText(SignupLoginActivity.this, "" + email, Toast.LENGTH_SHORT).show();

    }

}
