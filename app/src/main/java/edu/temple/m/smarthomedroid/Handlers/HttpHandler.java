package edu.temple.m.smarthomedroid.Handlers;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by M on 3/12/2017.
 */

public class HttpHandler {

    private static final String TAG = HttpHandler.class.getSimpleName();

    public HttpHandler(){

    }

    public String makeGetCall(String reqUrl, String method){
        String response = null;

        try{
            URL url = new URL(reqUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            //Read Response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = stream_to_string(in);
        }catch(MalformedURLException e){
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch(ProtocolException e){
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch(IOException e){
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch(Exception e){
            Log.e(TAG, "Exception: " + e.getMessage());
        }

        return response;
    }

    public String makePostCall(String reqUrl, JSONObject json){
        HttpsURLConnection conn;
        String data = json.toString();
        String result = null;

        try{
            conn = (HttpsURLConnection) ((new URL(reqUrl).openConnection()));
            conn.setDoOutput(true);
            conn.setConnectTimeout(10000);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            //conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("POST");
            //conn.connect();

            OutputStream outputStream = conn.getOutputStream();
            //BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            outputStream.write(data.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();

            //Read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            result = stream_to_string(in);

        }catch(MalformedURLException e){
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch(ProtocolException e){
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch(IOException e){
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch(Exception e){
            Log.e(TAG, "Exception: " + e.getMessage());
        }

        return result;

    }

    private String stream_to_string(InputStream is){
        BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
        StringBuilder sb = new StringBuilder();

        String line;
        try{
            while((line = reader.readLine()) != null){
                sb.append(line).append('\n');
            }
        }catch(IOException e){
            e.printStackTrace();
        } finally{
            try{
                is.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
