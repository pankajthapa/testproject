package com.cynoteck.petofy;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;

import org.json.JSONException;
import org.json.JSONObject;

import serviceprocessor.ServiceConstants;
import serviceprocessor.ServiceProcessor;
import utility.Common;
import utility.ConnectionDetector;
import utility.FacebookLoginClass;
import utility.GoogleApiClass;
import utility.Utility;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener {

    private Button login;
    private EditText username_edittext, password_edittext;
    private Context context;
    private ProgressDialog prgDialog;
    private String uname,pswd;
    private LoginButton fbLoginButton;
    private String fbUserId, jsonValues;
    private SignInButton googleSignIn;
    private GoogleApiClass googleApiClass;
    private Toolbar toolbar;
    private TextView toolbar_title, forget_password, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Facebook-Sdk
        FacebookLoginClass.FBSdkInitialization(this);
        FacebookLoginClass.getFbKeyHash(getString(R.string.package_name), this);

        setContentView(R.layout.activity_login_screen);

        init();

        login.setOnClickListener(this);
        register.setOnClickListener(this);
        forget_password.setOnClickListener(this);
        googleSignIn.setOnClickListener(this);

        loginFacebook();
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void init() {
        context = this;

        login = (Button)findViewById(R.id.LoginButton);
        username_edittext = (EditText)findViewById(R.id.LoginUserName);
        password_edittext = (EditText)findViewById(R.id.LoginPassword);
        fbLoginButton = (LoginButton)findViewById(R.id.facebookLoginButton);
        googleSignIn = (SignInButton)findViewById(R.id.googleLoginButton);
        forget_password = (TextView)findViewById(R.id.forget_Password);
        register = (TextView)findViewById(R.id.register);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);

        googleSignIn.setSize(SignInButton.SIZE_WIDE);
        googleApiClass = new GoogleApiClass(this);
        googleApiClass.googleLoginApi();

        // setting toolbar and its items
        settingToolbar();
    }

    private void settingToolbar()
    {
        // setting toolbar title and its custom font
        Typeface custom_font = Typeface.createFromAsset(getAssets(), Common.CUSTOM_FONT_PATH);
        toolbar_title.setTypeface(custom_font);
        toolbar_title.setText(getResources().getString(R.string.app_name));

        // setting back button of toolbar
        toolbar.setNavigationIcon(R.mipmap.ic_action_back_light);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClass.connect();
        clear();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(googleApiClass.isConnected()){
            googleApiClass.disconnect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // checking internet connection
        ConnectionDetector.checkInternetConnection(context);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.LoginButton:
                uname = username_edittext.getText().toString();
                pswd = password_edittext.getText().toString();
                Utility.hideKeyboard(this);
                jsonValues = jsonConversionOfValues();

                if(jsonValues != null) {
                    Log.e("Login JsonValue",""+jsonValues);
                    LoginUser loginUser = new LoginUser();
                    loginUser.execute(jsonValues);
                }
                break;

            case R.id.register:
                Intent intent = new Intent(context, RegistrationScreen.class);
                startActivity(intent);
                break;

            case R.id.googleLoginButton:
                Intent signInIntent = googleApiClass.signInGoogle();
                startActivityForResult(signInIntent, GoogleApiClass.RC_SIGN_IN);
                break;

            case R.id.forget_Password:

                break;

        }
    }


    // JsonObject is created here using EditText values to send to webservice for process.
    private String jsonConversionOfValues()
    {
        JSONObject dataObject = new JSONObject();
        JSONObject headerObject = new JSONObject();
        JSONObject registrationObject = new JSONObject();

        try {
            headerObject.put(ServiceConstants.KEY_DeviceId, Common.devId);
            headerObject.put(ServiceConstants.KEY_Platform, ServiceConstants.Platform_Value);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        // checking mandatory fields are filled or not.
        if(Utility.isNotNull(uname) && Utility.isNotNull(pswd))
        {
                try {
                    dataObject.put(ServiceConstants.KEY_UserName, uname);
                    dataObject.put(ServiceConstants.KEY_Password, pswd);

                    registrationObject.put(ServiceConstants.KEY_Header, headerObject);
                    registrationObject.put(ServiceConstants.KEY_Data, dataObject);

                    return registrationObject.toString();

                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
        }else if(Utility.isNotNull(fbUserId)){
                try{
                    dataObject.put(ServiceConstants.KEY_FBID, fbUserId);

                    registrationObject.put(ServiceConstants.KEY_Header, headerObject);
                    registrationObject.put(ServiceConstants.KEY_Data, dataObject);

                    return registrationObject.toString();

                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
        }else {
            Toast.makeText(context,getResources().getString(R.string.mandatory_fields),Toast.LENGTH_LONG).show();
            return null;
        }
    }


    private void clear()
    {
        username_edittext.requestFocus();
        username_edittext.setText("");
        password_edittext.setText("");
    }


    public void loginFacebook(){
        fbLoginButton.setReadPermissions("user_photos", "email", "user_birthday", "user_friends");
        fbLoginButton.registerCallback(FacebookLoginClass.callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                fbUserId = loginResult.getAccessToken().getUserId();
                Log.e("User ID  : ", "" + fbUserId);
                Log.e("Authentication Token : ", "" + loginResult.getAccessToken().getToken());

                jsonValues = jsonConversionOfValues();

                if(jsonValues != null) {
                    LoginUser loginUser = new LoginUser();
                    loginUser.execute(jsonValues);
                    clear();
                }

                Toast.makeText(context, "Login Successful!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(context, "Login cancelled by user!", Toast.LENGTH_LONG).show();
                Log.e("FBLogin Canceled", "Facebook Login failed!!");
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(context, "Login unsuccessful!", Toast.LENGTH_LONG).show();
                Log.e("FBLogin Error", "Facebook Login failed!!");
            }

        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        FacebookLoginClass.callbackManager.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GoogleApiClass.RC_SIGN_IN)
        {
            if(resultCode == RESULT_OK) {
                googleApiClass.handleSignInResult(data);
            }

            if(!googleApiClass.isConnecting()){
                googleApiClass.connect();
            }
        }
    }



    // WebService hit in the background
    public class LoginUser extends AsyncTask<String, Void, String>
    {
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgDialog = new ProgressDialog(context);
            try {
                prgDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            prgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prgDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            prgDialog.setContentView(R.layout.progress);
            prgDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            response =  ServiceProcessor.register_Login_User(params[0], ServiceConstants.LOGIN_URL);
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            prgDialog.dismiss();
            int responseCode = 0;
            String responseMessage;

            if(response != null)
            {
                try {
                    JSONObject responseObject = new JSONObject(response);
                    JSONObject header = responseObject.getJSONObject(ServiceConstants.KEY_Header);
                    String profileId = header.getString(ServiceConstants.KEY_ProfileId);
                    Log.e("ProfileId",profileId);
                    JSONObject serverResponse = responseObject.getJSONObject(ServiceConstants.KEY_Response);
                        responseCode = serverResponse.getInt(ServiceConstants.KEY_ResponseCode);
                        responseMessage = serverResponse.getString(ServiceConstants.KEY_ResponseMessage);
                    Toast.makeText(context, responseMessage, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Log.e("JSONException",""+e);
                    Toast.makeText(context,getResources().getString(R.string.server_error),Toast.LENGTH_SHORT).show();
                }

                if(responseCode == 109){
                    /*Intent category_intent = new Intent(context,CategoryScreen.class);
                    startActivity(category_intent);*/
                    clear();
                    finish();
                }else if(responseCode == 110){
                    password_edittext.setText("");
                    password_edittext.requestFocus();
                }
            }
        }
    }
}
