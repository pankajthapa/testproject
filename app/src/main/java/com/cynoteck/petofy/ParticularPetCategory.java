package com.cynoteck.petofy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import adapters.ParticularPetAdapterClass;
import beans.ParticularPetBeanClass;
import serviceprocessor.ServiceConstants;
import serviceprocessor.ServiceProcessor;
import utility.Common;
import utility.ConnectionDetector;

/**
 * Created by cynoteck on 1/27/2016.
 */
public class ParticularPetCategory extends AppCompatActivity {

    private Context context;
    private RecyclerView particular_category_recyclerView;
    private ArrayList<ParticularPetBeanClass> particular_pet_category_dataset;
    private Toolbar toolbar;
    private TextView toolbar_title;
    private ProgressDialog prgDialog;
    private int categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_particular_pet);

        Intent intent = getIntent();
        categoryId = intent.getIntExtra(Common.KEY_CATEGORYID,1);

        init();

        // Checking Internet Connection
        if (!ConnectionDetector.isConnectedToInternet(context)) {
            ConnectionDetector.checkInternetConnection(context);
        } else {
            String jsonValue = jsonConversionOfValue();
            Log.e("JSONVALUE", jsonValue);
            if (jsonValue != null) {
                ParticularPetService particularPetService = new ParticularPetService();
                particularPetService.execute(jsonValue);
            }
        }
    }


    private void init() {
        context = this;
        particular_category_recyclerView = (RecyclerView)findViewById(R.id.particularPet_recycler_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);

        // setting toolbar and its items
        settingToolbar();

        // set recyclerView layout
        setCategory_recycler_view_layout();
    }


    private void settingToolbar(){
        // setting custom font for textview
        Typeface custom_font = Typeface.createFromAsset(getAssets(), Common.CUSTOM_FONT_PATH);
        toolbar_title.setTypeface(custom_font);
        toolbar_title.setText(getResources().getString(R.string.app_name));

        // setting toolbar back button
        toolbar.setNavigationIcon(R.mipmap.ic_action_back_light);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setCategory_recycler_view_layout() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        particular_category_recyclerView.setHasFixedSize(true);
//        particular_category_recyclerView.addItemDecoration(new MarginDecoration(context));

        // use a grid layout manager
        RecyclerView.LayoutManager particular_category_layout_manager = new LinearLayoutManager(context);
        particular_category_recyclerView.setLayoutManager(particular_category_layout_manager);
    }

    public String jsonConversionOfValue() {
        JSONObject dataObject = new JSONObject();
        JSONObject headerObject = new JSONObject();
        JSONObject particularPetObject = new JSONObject();

        try {
            headerObject.put(ServiceConstants.KEY_DeviceId, Common.devId);
            headerObject.put(ServiceConstants.KEY_Platform, ServiceConstants.Platform_Value);

            dataObject.put(ServiceConstants.KEY_CategoryId, categoryId);

            particularPetObject.put(ServiceConstants.KEY_Header, headerObject);
            particularPetObject.put(ServiceConstants.KEY_Data, dataObject);

            return particularPetObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();

            return particularPetObject.toString();
        }
    }


    // WebService hit in the background
    public class ParticularPetService extends AsyncTask<String, Void, ArrayList<ParticularPetBeanClass>> {
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
        protected ArrayList<ParticularPetBeanClass>  doInBackground(String... params) {
            response = ServiceProcessor.register_Login_User(params[0], ServiceConstants.PARTICULAR_PET_CATEGORY_URL);
            return getJsonResultValue(response);
        }

        @Override
        protected void onPostExecute(ArrayList<ParticularPetBeanClass> response) {
            prgDialog.dismiss();
            Log.e("petCategory JsonValue",""+response);
            if (response != null) {

                // set recyclerView adapter
                RecyclerView.Adapter particular_category_adapter = new ParticularPetAdapterClass(context, response);
                particular_category_recyclerView.setAdapter(particular_category_adapter);
            }else{
                Toast.makeText(context, getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        }

        private ArrayList<ParticularPetBeanClass> getJsonResultValue(String response)
        {
            particular_pet_category_dataset = new ArrayList<>();
            ParticularPetBeanClass item;
            try{
                JSONObject responseObject = new JSONObject(response);
//                JSONObject header = responseObject.getJSONObject(ServiceConstants.KEY_Header);
                JSONArray data = responseObject.getJSONArray(ServiceConstants.KEY_Data);
                JSONObject particular_category_ItemObject;
                for(int i=0; i<data.length();i++){
                    String imageUrl;
                    item = new ParticularPetBeanClass();
                    particular_category_ItemObject = data.getJSONObject(i);

                    item.setPet_serial_Id(particular_category_ItemObject.getInt(ServiceConstants.KEY_PetId));
                    item.setBreed(particular_category_ItemObject.getString(ServiceConstants.KEY_PetBreed));
                    item.setYears(particular_category_ItemObject.getInt(ServiceConstants.KEY_PetAgeYears));
                    item.setMonths(particular_category_ItemObject.getInt(ServiceConstants.KEY_PetAgeMonths));
                    item.setPet_price(particular_category_ItemObject.getInt(ServiceConstants.KEY_PetPrice));
                    item.setPet_Title(particular_category_ItemObject.getString(ServiceConstants.KEY_PetTitle));
                    imageUrl = particular_category_ItemObject.getString(ServiceConstants.KEY_PetProfileImageUrl);
                    item.setPet_profile_Image(ServiceProcessor.retrieve_Image(imageUrl));

                    particular_pet_category_dataset.add(item);
                }
                return particular_pet_category_dataset;
            } catch (JSONException e) {
                e.printStackTrace();
                return particular_pet_category_dataset;
            }
        }
    }
}
