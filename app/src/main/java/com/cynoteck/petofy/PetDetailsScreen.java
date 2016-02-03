package com.cynoteck.petofy;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import utility.Common;

/**
 * Created by cynoteck on 2/3/2016.
 */
public class PetDetailsScreen extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbar_title;
    private Context context;
    private ProgressDialog prgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_details_screen);

        init();
    }


    private void init() {
        context = this;
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);

        // setting toolbar and its items
        settingToolbar();
    }


    private void settingToolbar() {
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
}
