package serviceprocessor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by cynoteck on 12/30/2015.
 */
public class ServiceProcessor {


    public static String register_Login_User(String jsonObject, String Url)
    {
        StringBuilder response = new StringBuilder();
        HttpURLConnection con = null;
Log.e(Url,jsonObject);
        try
        {
            URL url = new URL(Url);
            con = (HttpURLConnection)url.openConnection();
            con.setReadTimeout(50000);
            con.setConnectTimeout(50000);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);

            OutputStream os = con.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(jsonObject);
            writer.flush();
            writer.close();
            os.close();

            con.connect();
            InputStream content = con.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            String line;
            while((line = reader.readLine())!=null){
                response.append(line+"\n");
            }
            reader.close();
            Log.e("RESPONSE_CHECK",""+response.toString());
            return response.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return response.toString();
        }
    }


    public static Bitmap retrieve_Image(String url)
    {
        Bitmap imageIcon = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            imageIcon = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return imageIcon;
    }
}
