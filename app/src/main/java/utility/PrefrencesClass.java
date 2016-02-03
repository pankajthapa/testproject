package utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by cynoteck on 1/8/2016.
 */
public class PrefrencesClass {

    static SharedPreferences sharedPreferences;
    static SharedPreferences.Editor editor;

    public static void saveDeviceId(Context context)
    {
        sharedPreferences = context.getSharedPreferences(Common.DEVICE_ID_FILE_KEY, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(Common.KEY_DEVICE_ID,Utility.getDeviceID(context));
        editor.apply();
    }

    public static String getDeviceIDPrefrenceValue(Context context){
        sharedPreferences = context.getSharedPreferences(Common.DEVICE_ID_FILE_KEY,Context.MODE_PRIVATE);
        return sharedPreferences.getString(Common.KEY_DEVICE_ID,"");
    }

    public static void saveUserProfileId(Context context, String profileId)
    {
        sharedPreferences = context.getSharedPreferences(Common.USER_PROFILE_ID_FILE_KEY,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(Common.KEY_PROFILE_ID,profileId);
        editor.apply();
    }
}
