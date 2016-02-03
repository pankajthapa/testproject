package utility;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.cynoteck.petofy.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by cynoteck on 1/6/2016.
 */
public class FacebookLoginClass {

    public static AccessToken accessToken;
    public static CallbackManager callbackManager;
    public static String fbUserId, fbloginObject, email, fname, lname, data, imageUrl;
    public static List<String> permissionNeeds = Arrays.asList("user_photos", "email", "user_birthday", "user_friends");
    static Context context;

    public static void FBSdkInitialization(Context context)
    {
        FacebookSdk.sdkInitialize(context);
        callbackManager = CallbackManager.Factory.create();
    }

    public static void facebookLogin(Activity activity)
    {
        context = activity;
        LoginManager.getInstance().logInWithReadPermissions(activity, permissionNeeds);
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                accessToken = loginResult.getAccessToken();
                fbUserId = loginResult.getAccessToken().getUserId();
                Log.e("User ID  : ", "" + fbUserId);
                Log.e("Authentication Token : ", "" + loginResult.getAccessToken().getToken());
                Toast.makeText(context, context.getString(R.string.fb_login_successful), Toast.LENGTH_LONG).show();

                GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            Log.e("Facebook Login", "" + response.toString());
                            Log.e("Facebook Object", "" + object.toString());
                            JSONObject object1 = response.getJSONObject();
                            if (object1 != null) {
                                fbloginObject = object1.toString();

                                if (object1.getString("email") == null) {
                                    Log.d("THE EMAIL IS NULL", "EMAIL IS NULL");
                                } else {
                                    email = object1.getString("email");
                                }
                                fname = object.getString("first_name");
                                lname = object.getString("last_name");
                                String picture = object.getString("picture");
                                JSONObject object2 = new JSONObject(picture);
                                data = object2.getString("data");
                                JSONObject object3 = new JSONObject(data);
                                imageUrl = object3.getString("url");
                            }
                        } catch (Exception e) {
                            Toast.makeText(context, context.getString(R.string.fb_error), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                        Log.e("Facebook data", "" + fbloginObject + "\n" + email + "\n" + fname + "\n" + lname + "\n" + data + "\n" + imageUrl);
                    }
                });


                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,first_name,last_name,email,gender,birthday,picture");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(context, context.getString(R.string.fb_login_cancelled), Toast.LENGTH_LONG).show();
                Log.e("FBLogin Canceled", "Facebook Login failed!!");
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(context, context.getString(R.string.fb_login_unsuccessful), Toast.LENGTH_LONG).show();
                Log.e("FBLogin Error", "Facebook Login failed!!");
            }
        });
    }


    public static void getFbKeyHash(String packageName, Context context) {

        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    packageName,
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("YourKeyHash :", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                System.out.println("YourKeyHash: "+ Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();

        }
    }

}
