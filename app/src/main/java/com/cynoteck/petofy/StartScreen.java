package com.cynoteck.petofy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import utility.Common;
import utility.ConnectionDetector;
import utility.PrefrencesClass;
import utility.Utility;

/**
 * Created by cynoteck on 1/15/2016.
 */
public class StartScreen extends Activity implements View.OnClickListener {

    TextView start_go_button;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.setFullScreenMode(this);
        setContentView(R.layout.start_screen);

        init();

        // Saving deviceID in sharedPrefrences
        if(PrefrencesClass.getDeviceIDPrefrenceValue(context).equals("")){
            PrefrencesClass.saveDeviceId(context);
            Common.devId = PrefrencesClass.getDeviceIDPrefrenceValue(context);
        }else{
            Common.devId = PrefrencesClass.getDeviceIDPrefrenceValue(context);
        }
        Log.e("Device Id", "" + Common.devId);

        start_go_button.setOnClickListener(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        // checking internet connection
        ConnectionDetector.checkInternetConnection(context);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // checking internet connection
        ConnectionDetector.checkInternetConnection(context);
    }

    private void init() {
        context = this;
        start_go_button = (TextView)findViewById(R.id.startGoButton);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.startGoButton:
                Intent intent = new Intent(context, CategoryScreen.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){

        }
    }
}
