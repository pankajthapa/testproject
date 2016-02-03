package com.cynoteck.petofy;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;

import adapters.CategoryAdapterClass;
import beans.CategoryItemBeanClass;
import serviceprocessor.ServiceConstants;
import serviceprocessor.ServiceProcessor;
import utility.Common;
import utility.ConnectionDetector;
import utility.MarginDecoration;
import utility.Utility;

/**
 * Created by cynoteck on 1/11/2016.
 */
public class CategoryScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Toolbar.OnMenuItemClickListener {

    private RecyclerView category_recycler_view;
    private Context context;
    private ProgressDialog prgDialog;
    private EditText toolbar_searchBox;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private TextView toolbar_title;
    private View searchContainer;
    private ImageView searchClearButton,searchBackButton;
    private static boolean isSearchOpened = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utility.setFullScreenMode(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        init();

//        Checking Internet Connection
        if (!ConnectionDetector.isConnectedToInternet(context)) {
            ConnectionDetector.checkInternetConnection(context);
        } else {
            String jsonValue = jsonConversionOfValue();
            Log.e("JSONVALUE", jsonValue);
            if (jsonValue != null) {
                CategoryService categoryService = new CategoryService();
                categoryService.execute(jsonValue);
            }
        }

    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void init() {
        context = this;
        category_recycler_view = (RecyclerView) findViewById(R.id.category_recycler_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_searchBox = (EditText)findViewById(R.id.toolbar_searchBox);
        searchContainer = findViewById(R.id.search_container);
        searchClearButton = (ImageView)findViewById(R.id.search_clear);
        searchBackButton = (ImageView)findViewById(R.id.searchBackButton);

        // setting toolbar and its items
        settingToolbar();
        // set recyclerView layout
        setCategory_recycler_view_layout();
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if(isSearchOpened){
            searchContainer.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);
            isSearchOpened = false;
        }else {
            super.onBackPressed();
        }

    }


    public void setCategory_recycler_view_layout() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        category_recycler_view.setHasFixedSize(true);
        category_recycler_view.addItemDecoration(new MarginDecoration(context));

        // use a grid layout manager
        RecyclerView.LayoutManager category_layout_manager = new GridLayoutManager(context, 2);
        category_recycler_view.setLayoutManager(category_layout_manager);
    }


    public void settingToolbar(){

        // setting custom font for textview
        Typeface custom_font = Typeface.createFromAsset(getAssets(), Common.CUSTOM_FONT_PATH);
        toolbar_title.setTypeface(custom_font);
        toolbar_title.setText(getResources().getString(R.string.category));

        // setting drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // setting navigation view and it's items
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // setting toolbar items from menu resource
        toolbar.inflateMenu(R.menu.main);
        toolbar.setOnMenuItemClickListener(this);
    }



    @SuppressLint("PrivateResource")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_search:
                isSearchOpened = true;
                toolbar.setVisibility(View.GONE);
                searchContainer.setVisibility(View.VISIBLE);
                toolbar_searchBox.requestFocus();
                searching_Method();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    public void searching_Method()
    {
        try {
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(toolbar_searchBox, getTitleColor());
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }

        toolbar_searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String searchString = toolbar_searchBox.getText().toString();
                int textLenght = searchString.length();
                if(textLenght > 1){
                    searchClearButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar_searchBox.setText("");
                searchClearButton.setVisibility(View.GONE);
            }
        });

        searchBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchContainer.setVisibility(View.GONE);
                toolbar.setVisibility(View.VISIBLE);
                Utility.hideKeyboard(CategoryScreen.this);
                isSearchOpened = false;
            }
        });
    }



    public String jsonConversionOfValue() {
//        JSONObject dataObject = new JSONObject();
        JSONObject headerObject = new JSONObject();
        JSONObject categoryObject = new JSONObject();

        try {
            headerObject.put(ServiceConstants.KEY_DeviceId, Common.devId);
            headerObject.put(ServiceConstants.KEY_Platform, ServiceConstants.Platform_Value);

            categoryObject.put(ServiceConstants.KEY_Header, headerObject);

            return categoryObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();

            return categoryObject.toString();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.seller_buyer) {
            Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
            startActivity(intent);
        } else if (id == R.id.privacy_policy) {

        } else if (id == R.id.terms_of_services) {

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //WebService hit in the background
    public class CategoryService extends AsyncTask<String, Void, ArrayList<CategoryItemBeanClass>> {
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
        protected ArrayList<CategoryItemBeanClass> doInBackground(String... params) {
            response = ServiceProcessor.register_Login_User(params[0], ServiceConstants.PET_CATEGORY_URL);
            return getJsonResultValue(response);
        }

        @Override
        protected void onPostExecute(ArrayList<CategoryItemBeanClass> responseValue) {
            prgDialog.dismiss();
            if (responseValue != null) {

                // set recyclerView adapter
                RecyclerView.Adapter category_view_adapter = new CategoryAdapterClass(context, responseValue);
                category_recycler_view.setAdapter(category_view_adapter);
            }else{
                Toast.makeText(context,getResources().getString(R.string.server_error),Toast.LENGTH_SHORT).show();
            }
        }


        public ArrayList<CategoryItemBeanClass> getJsonResultValue(String response) {
            ArrayList<CategoryItemBeanClass> categories = new ArrayList<>();
            CategoryItemBeanClass item;

            try {
                JSONObject responseObject = new JSONObject(response);
//                JSONObject header = responseObject.getJSONObject(ServiceConstants.KEY_Header);
                JSONArray data = responseObject.getJSONArray(ServiceConstants.KEY_Data);
                JSONObject categoryItemObject;
                for (int i = 0; i < data.length(); i++) {
                    String imagerUrl;
                    item = new CategoryItemBeanClass();
                    categoryItemObject = data.getJSONObject(i);

                    item.setCategoryID(categoryItemObject.getInt(ServiceConstants.KEY_CategoryId));
                    item.setCategoryTitle(categoryItemObject.getString(ServiceConstants.KEY_CategoryTitle));
                    item.setCategorySubTitle(categoryItemObject.getString(ServiceConstants.KEY_CategorySubTitle));
                    imagerUrl = categoryItemObject.getString(ServiceConstants.KEY_CategoryImage);
                    item.setCategoryImage(ServiceProcessor.retrieve_Image(imagerUrl));
                    categories.add(item);
                }
                return categories;
            } catch (JSONException e) {
                Log.e("JSONException",""+e);
                return categories;
            }
        }

    }
}
