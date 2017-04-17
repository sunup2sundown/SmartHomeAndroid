package edu.temple.m.smarthomedroid.Handlers;

import android.util.Log;

import org.json.JSONException;
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

public class HttpHandler2 {

    private static final String TAG = HttpHandler2.class.getSimpleName();

    public HttpHandler2(){

    }
    private JSONObject respGet,respPost;
    public JSONObject makeGetCall(String reqUrl, String method){

        try{
            URL url = new URL(reqUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            //Read Response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            respGet = stream_to_string(in);
        }catch(MalformedURLException e){
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch(ProtocolException e){
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch(IOException e){
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch(Exception e){
            Log.e(TAG, "Exception: " + e.getMessage());
        }

        return respGet;
    }

    public JSONObject makePostCall(String reqUrl, JSONObject json){
        HttpsURLConnection conn;
        String data = json.toString();


        try{
            conn = (HttpsURLConnection) ((new URL(reqUrl).openConnection()));
            conn.setDoOutput(true);
            conn.setConnectTimeout(10000);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            //conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("POST");
            conn.connect();

            OutputStream outputStream = conn.getOutputStream();
            //BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            outputStream.write(data.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();

            //Read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            respPost = stream_to_string(in);

        }catch(MalformedURLException e){
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch(ProtocolException e){
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch(IOException e){
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch(Exception e){
            Log.e(TAG, "Exception: " + e.getMessage());
        }

        return respPost;

    }

    private JSONObject stream_to_string(InputStream is){
        int count=0,count2= 0;

        JSONObject res = null;
        BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
        StringBuilder sb = new StringBuilder();
        String sb2;
        String line;
        try{
            while((line = reader.readLine()) != null){
                sb.append(line);
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
        sb2=sb.toString();
        Log.d(TAG, sb2);
        if (sb2 != null) {
            while (sb2.charAt(count)!='{'){
                count++;
            }
            count2=sb2.length()-1;
            while (sb2.charAt(count2)!='}'){
                count2--;
            }
        }
        sb2= sb2.substring(count,(count2+1));
        //sb2= sb2.replaceAll("\\[", "").replaceAll("\\]","");
        try {
            res = new JSONObject(sb2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }
}
