package com.cynoteck.petofy;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import serviceprocessor.ServiceConstants;
import serviceprocessor.ServiceProcessor;
import utility.Common;
import utility.Utility;

/**
 * Created by cynoteck on 12/29/2015.
 */
public class RegistrationScreen extends AppCompatActivity implements View.OnClickListener {

    private EditText firstname,lastname,username,password,confirm_password,email;
    private Button registerButton,seller_toggle,buyer_toggle;
    private Context context;
    private ProgressDialog prgDialog;
    private Toolbar toolbar;
    private TextView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_screen);

        init();

        seller_toggle.setOnClickListener(this);
        buyer_toggle.setOnClickListener(this);
        buyer_toggle.performClick();
        registerButton.setOnClickListener(this);
    }


    private void init() {
        context = this;

        registerButton = (Button)findViewById(R.id.registrationButton);
        firstname = (EditText)findViewById(R.id.userFirstName);
        lastname = (EditText)findViewById(R.id.userLastName);
        username = (EditText)findViewById(R.id.registrationUsername);
        password = (EditText)findViewById(R.id.password);
        confirm_password = (EditText)findViewById(R.id.confirmPassword);
        email = (EditText)findViewById(R.id.emailId);
        seller_toggle = (Button)findViewById(R.id.seller_toggle_button);
        buyer_toggle = (Button)findViewById(R.id.buyer_toggle_button);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);

        // setting toolbar and its items
        settingToolbar();
    }


    private void settingToolbar()
    {
        // setting custom font for textview
        Typeface custom_font = Typeface.createFromAsset(getAssets(), Common.CUSTOM_FONT_PATH);
        toolbar_title.setTypeface(custom_font);
        toolbar_title.setText(getResources().getString(R.string.app_name));

        // setting back button for toolbar
        toolbar.setNavigationIcon(R.mipmap.ic_action_back_light);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.registrationButton:
                Utility.hideKeyboard(this);
                String jsonValues = jsonConversionOfValues();

                if(jsonValues != null) {
                    Log.e("JsonValue Regitration", "" + jsonValues);
                    RegisterUser registerUser = new RegisterUser();
                    registerUser.execute(jsonValues);
                }
                break;
            case R.id.buyer_toggle_button:
                buyer_toggle.setSelected(true);
                seller_toggle.setSelected(false);
                break;
            case R.id.seller_toggle_button:
                buyer_toggle.setSelected(false);
                seller_toggle.setSelected(true);
                break;

        }
    }


    // JsonObject is created here using EditText values to send to webservice for process.
    private String jsonConversionOfValues()
    {
        String fname = firstname.getText().toString();
        String lname = lastname.getText().toString();
        String uname = username.getText().toString();
        String pswd = password.getText().toString();
        String conf_pswd = confirm_password.getText().toString();
        String eml = email.getText().toString();

        JSONObject dataObject = new JSONObject();
        JSONObject headerObject = new JSONObject();
        JSONObject registrationObject = new JSONObject();

        // checking mandatory fields are filled or not.
        if(Utility.isNotNull(fname) && Utility.isNotNull(lname) && Utility.isNotNull(uname) && Utility.isNotNull(pswd) && Utility.isNotNull(conf_pswd) && Utility.isNotNull(eml))
        {
            // validating emailId
            if(Utility.validate(eml))
            {
            if(pswd.equalsIgnoreCase(conf_pswd))
            {
                try {
                    dataObject.put(ServiceConstants.KEY_UserName, uname);
                    dataObject.put(ServiceConstants.KEY_Password, pswd);
                    dataObject.put(ServiceConstants.KEY_ConfirmPassword, conf_pswd);
                    dataObject.put(ServiceConstants.KEY_Email, eml);
                    dataObject.put(ServiceConstants.KEY_FirstName, fname);
                    dataObject.put(ServiceConstants.KEY_LastName, lname);

                    headerObject.put(ServiceConstants.KEY_DeviceId, Common.devId);
                    headerObject.put(ServiceConstants.KEY_Platform, ServiceConstants.Platform_Value);

                    registrationObject.put(ServiceConstants.KEY_Header, headerObject);
                    registrationObject.put(ServiceConstants.KEY_Data, dataObject);

                    return registrationObject.toString();

                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }else{
                Toast.makeText(context,getResources().getString(R.string.password_equalization),Toast.LENGTH_LONG).show();
                return null;
            }
            }else{
                Toast.makeText(context,getResources().getString(R.string.email_validation),Toast.LENGTH_LONG).show();
                return null;
            }
        }else{
            Toast.makeText(context,getResources().getString(R.string.mandatory_fields),Toast.LENGTH_LONG).show();
            return null;
        }
    }


    private void clear()
    {
        firstname.requestFocus();
        firstname.setText("");
        lastname.setText("");
        username.setText("");
        password.setText("");
        confirm_password.setText("");
        email.setText("");
    }



    // WebService hit in the background
    public class RegisterUser extends AsyncTask<String, Void, String>
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
            Log.e("Json sent",params[0]);
            response = ServiceProcessor.register_Login_User(params[0], ServiceConstants.REGISTRATION_URL);
            Log.e("Response",""+response);
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            prgDialog.dismiss();
            int responseCode = 0;
            String responseMessage = null;
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
                    Log.e("try and catch",""+responseCode);
                    Log.e("Response Message",""+responseMessage);
                    Toast.makeText(context,responseMessage,Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    Log.e("JSONException",""+e);
                    Toast.makeText(context,getResources().getString(R.string.server_error),Toast.LENGTH_LONG).show();
                }
                Log.e("Response Code",""+responseCode);
                if(responseCode == 109){
                    clear();
                    finish();
                }
            }
        }
    }

}
