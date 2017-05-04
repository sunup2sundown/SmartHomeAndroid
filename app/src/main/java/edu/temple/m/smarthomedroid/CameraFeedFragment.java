package edu.temple.m.smarthomedroid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;

import edu.temple.m.smarthomedroid.Handlers.HttpHandler;

/**
 * Created by M on 4/23/2017.
 */

public class CameraFeedFragment extends Fragment {
    private final String TAG = "CameraFeedFragment";
    private final String URL = "https://s3.amazonaws.com/smart-home-gateway/";
    private String cameraUrl;
    Handler mHandler = new Handler();
    Bundle bundle;

    String sessionID;
    String userID;
    String cameraName;
    String houseName;

    ImageView feedView;
    Button takePictureBtn;
    Button startFeedBtn;
    Button stopFeedBtn;

    int flag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //Create new view from relay fragment layout
        View view = inflater.inflate(R.layout.fragment_camerafeed, container, false);
        bundle = new Bundle();
        //Receive argument bundle from Home Activity
        userID = getArguments().getString("Username");
        sessionID = getArguments().getString("SessionToken");
        cameraName = getArguments().getString("CameraName");
        houseName = getArguments().getString("HouseName");
        sessionID = getArguments().getString("SessionToken");

        feedView = (ImageView)view.findViewById(R.id.imageView_camera);

        takePictureBtn = (Button)view.findViewById(R.id.button_camera_feed_takepicture);
        startFeedBtn = (Button)view.findViewById(R.id.button_startcamera);
        stopFeedBtn = (Button)view.findViewById(R.id.button_stopcamera);

        takePictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture(cameraUrl);
            }
        });

        startFeedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFeed(sessionID, cameraName, houseName);
                mHandler.postDelayed(changeImage, 1000);
            }
        });

        stopFeedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopFeed(sessionID, cameraName, houseName);
                mHandler.removeCallbacks(changeImage);
            }
        });

        getCameraFeed(sessionID, cameraName, houseName);

        return view;
    }

    public void onBackPressed()
    {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStack();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

    }

    @Override
    public void onResume(){
        super.onResume();
    }

    Runnable changeImage = new Runnable(){
        @Override
        public void run(){
            new DownloadImageTask(feedView).execute(cameraUrl);
            mHandler.postDelayed(changeImage, 1000);
        }
    };

    private String getCameraFeed(String sessionToken, String peripheralName, String houseName){
        String url = "";
        JSONObject json = new JSONObject();

        try{
            json.put("sessionToken", sessionToken);
            json.put("peripheralName", peripheralName);
            json.put("houseName", houseName);

            url = new GetCameraUrl().execute(json).get();
        } catch(JSONException e){
            e.printStackTrace();
        } catch(InterruptedException e){
            e.printStackTrace();
        } catch(ExecutionException e){
            e.printStackTrace();
        }

        cameraUrl = URL + url + ".jpeg";

        Log.d(TAG, cameraUrl);

        return url;
    }

    private void takePicture(String url){
        JSONObject jsonObject = new JSONObject();

        try{
            jsonObject.put("sessionToken", sessionID);
            jsonObject.put("peripheralName", cameraName);
            jsonObject.put("houseName", houseName);
        } catch(JSONException e){
            e.printStackTrace();
        }

        new TakePicture().execute(jsonObject);

        new DownloadImageTask(feedView).execute(url);
    }

    private void startFeed(String sessionToken, String peripheralName, String houseName){
        final String TURNON = "1";
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("sessionToken", sessionToken);
            jsonObject.put("peripheralName", peripheralName);
            jsonObject.put("houseName", houseName);
            jsonObject.put("cameraFeedValue", TURNON);
            jsonObject.put("cameraFeedFPS", "2");
        } catch(JSONException e){
            e.printStackTrace();
        }

        new SetCameraFeed().execute(jsonObject);
    }

    private void stopFeed(String sessionToken, String peripheralName, String houseName){
        final String TURNOFF = "0";
        JSONObject jsonObject = new JSONObject();

        try{
            jsonObject.put("sessionToken", sessionToken);
            jsonObject.put("peripheralName", peripheralName);
            jsonObject.put("houseName", houseName);
            jsonObject.put("cameraFeedValue", TURNOFF);
            jsonObject.put("cameraFeedFPS", "0");
        } catch(JSONException e){
            e.printStackTrace();
        }

        new SetCameraFeed().execute(jsonObject);
    }

    /**
     * Tells Camera to take a picture
     */
    private class TakePicture extends AsyncTask<JSONObject, Void, Void>{
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(JSONObject...args){
            String response = new HttpHandler().makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/takepicture", args[0]);

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
        }
    }

    /**
     * Tells Camera to take a picture
     */
    private class SetCameraFeed extends AsyncTask<JSONObject, Void, Void>{
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(JSONObject...args){

            String response = new HttpHandler().makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/takepicture", args[0]);

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        //ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            //this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            //String urldisplay = urls[0];
            String urldisplay = "https://s3.amazonaws.com/smart-home-gateway/test5.jpeg";
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("BitmapError", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            feedView.setImageBitmap(result);
        }
    }

    private class GetCameraUrl extends AsyncTask<JSONObject, Void, String>{
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(JSONObject...args){
            JSONObject jsonResponse;
            String returnedValue = "";
            JSONArray jArray;

            String response = new HttpHandler().makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/getimagecamera", args[0]);

            if (response!=null){
                try {
                    jArray=new JSONArray(response);
                    jArray=jArray.getJSONArray(0);
                    jsonResponse = jArray.getJSONObject(0);
                    returnedValue = jsonResponse.getString("ImageName");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return returnedValue;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
        }
    }
}

