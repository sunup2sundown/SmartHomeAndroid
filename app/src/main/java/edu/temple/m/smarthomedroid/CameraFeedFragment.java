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
import android.widget.ImageView;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;

import edu.temple.m.smarthomedroid.Handlers.HttpHandler;

/**
 * Created by M on 4/23/2017.
 */

public class CameraFeedFragment extends Fragment {
    Handler mHandler = new Handler();
    Bundle bundle;
    String sessionID;
    String userID;
    ImageView feedView;
    int flag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //Create new view from relay fragment layout
        View view = inflater.inflate(R.layout.fragment_camerafeed, container, false);
        bundle = new Bundle();
        //Receive argument bundle from Home Activity
        userID = getArguments().getString("Username");
        sessionID = getArguments().getString("SessionToken");

        bundle.putString("SessionToken", sessionID);

        feedView = (ImageView)view.findViewById(R.id.imageView_camera);

        return view;
    }


    public void onBackPressed()
    {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStack();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        mHandler.postDelayed(changeImage, 1000);
    }

    @Override
    public void onResume(){
        super.onResume();
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
            String urldisplay = urls[0];
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

    Runnable changeImage = new Runnable(){
        @Override
        public void run(){
            new DownloadImageTask(feedView).execute("https://s3.amazonaws.com/smart-home-gateway/test5.jpeg");
            mHandler.postDelayed(changeImage, 1000);
        }
    };

}

