package edu.temple.m.smarthomedroid.Handlers;

import android.app.ProgressDialog;
import android.content.Context;

import java.util.concurrent.ExecutionException;

/**
 * Created by M on 5/4/2017.
 */

public class ProgressHandler {
    private static ProgressDialog mProgressDialog;

    public static void showProgressDialog(Context context, String title,
                                          String msg, boolean isCancelable){
        try{
            if(mProgressDialog == null){
                mProgressDialog = ProgressDialog.show(context, title, msg);
                mProgressDialog.setCancelable(isCancelable);
            }

            if(!mProgressDialog.isShowing()){
                mProgressDialog.show();
            }
        } catch(IllegalArgumentException ie){
            ie.printStackTrace();
        } catch(RuntimeException re){
            re.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void removeProgressDialog(){
        try{
            if(mProgressDialog != null){
                if(mProgressDialog.isShowing()){
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }
            }
        } catch(IllegalArgumentException ie){
            ie.printStackTrace();
        } catch(RuntimeException re){
            re.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
