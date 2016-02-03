package utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

/**
 * Created by cynoteck on 1/8/2016.
 */
public class GoogleApiClass implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    GoogleApiClient mGoogleApiClient;
    Context context;
    Activity activity;
    boolean googleSignInClicked;
    public static final int RC_SIGN_IN = 0;
    private ConnectionResult mConnectionResult;

    public GoogleApiClass(Activity activity)
    {
        this.context = activity;
        this.activity = activity;
    }


    public void googleLoginApi()
    {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .build();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.e("onConnectionFailed","GoogleApiClient connectionFailed");
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(),activity,
                    0).show();
        }
        mConnectionResult = result;
        if(googleSignInClicked){
            resolveSignInError();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.e("onConnected","GoogleApiClient connected");
        googleSignInClicked = false;
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("onConnectionSuspended","GoogleApiClient connectionSuspended");
        connect();
    }

    public void connect()
    {
        mGoogleApiClient.connect();
    }

    public void disconnect() {
        mGoogleApiClient.disconnect();
    }

    public boolean isConnected()
    {
        return mGoogleApiClient.isConnected();
    }

    public boolean isConnecting()
    {
        return mGoogleApiClient.isConnecting();
    }



    public Intent signInGoogle() {
        googleSignInClicked = true;
        return Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
    }


    public void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Log.e("signOut Status", "" + status);
                    }
                });
    }

    public void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Log.e("signOut Status", "" + status);
                    }
                });
    }

    public void handleSignInResult(Intent data) {

        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        Log.e("google sign in result", "" + result.isSuccess());

        if(result.isSuccess())
        {
            GoogleSignInAccount acct = result.getSignInAccount();
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            Log.e("google signin result",""+personName+"\n"+personEmail+"\n"+personId+"\n"+personPhoto);
        }else {
            Log.e("google login failed", "Login failed");
        }
    }


    public void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mConnectionResult.startResolutionForResult(activity, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mGoogleApiClient.connect();
            }
        }
    }
}
