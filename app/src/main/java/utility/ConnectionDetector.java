package utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.cynoteck.petofy.R;

/**
 * Created by cynoteck on 1/4/2016.
 */
public class ConnectionDetector {

    public static boolean isConnectedToInternet(Context context){
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivity != null)
        {
            NetworkInfo [] infos = connectivity.getAllNetworkInfo();
            if(infos != null){
                for(int i=0; i<infos.length; i++)
                {
                    if(infos[i].getState() == NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void checkInternetConnection(Context context)
    {
        if(!isConnectedToInternet(context)){
            DialogBox dialogBox = new DialogBox(context);
            dialogBox.showAlertDialog(context.getResources().getString(R.string.no_internet_connection), context.getResources().getString(R.string.no_connection_message));
        }
    }
}
