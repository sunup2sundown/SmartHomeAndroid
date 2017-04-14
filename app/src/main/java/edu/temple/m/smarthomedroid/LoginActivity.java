package edu.temple.m.smarthomedroid;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import edu.temple.m.smarthomedroid.Dialogs.LoginDialogFragment;
import edu.temple.m.smarthomedroid.Dialogs.SignupDialogFragment;
import edu.temple.m.smarthomedroid.Handlers.HttpHandler;

import static java.lang.Thread.sleep;


public class LoginActivity extends AppCompatActivity implements SignupDialogFragment.SignupDialogListener, LoginDialogFragment.LoginDialogListener{

    private Button btnSignup, btnSignin;
    private EditText username, password, confirmPassword;
    private String TAG = LoginActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private LoginDialogFragment loginFrag;
    private SignupDialogFragment signupFrag;
    private boolean goodUser;
    private String userStr, passStr;
    private String good ="1",bad="0";

    FragmentManager dialogManager;

    private void setgood(){
        this.goodUser=true;
    }
    private void setbad(){
        this.goodUser=false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dialogManager = getSupportFragmentManager();

        btnSignin = (Button)findViewById(R.id.sign_in);
        btnSignup = (Button)findViewById(R.id.sign_up);

        //goodUser = false;

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginFrag = new LoginDialogFragment();
                loginFrag.show(dialogManager, "LoginDialogFragment");
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupFrag = new SignupDialogFragment();
                signupFrag.show(dialogManager, "SignupDialogFragment");

            }
        });
    }

    /* Dialog Fragment Listeners Implementations
    *
     */
    //Signup Dialog Listener Implementation
    public void showSignupDialog(FragmentManager fm){
        //create instance of dialog fragment and show it
        DialogFragment signupDialog = new SignupDialogFragment();
        signupDialog.show(fm, "SignupDialogFragment");
    }

    @Override
    public void onSignupDialogPositiveClick(DialogFragment dialog){
        //Signup Function
        username = (EditText) dialog.getDialog().findViewById(R.id.signup_dialog_username);
        password = (EditText) dialog.getDialog().findViewById(R.id.signup_dialog_password);
        confirmPassword = (EditText) dialog.getDialog().findViewById(R.id.signup_dialog_confirm);

        //Log.d(TAG, password.getText().toString());
        //Log.d(TAG, confirmPassword.getText().toString());
        userStr = username.getText().toString();
        passStr = password.getText().toString();
        String confirm = confirmPassword.getText().toString();

        new CheckUsername().execute();
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(Objects.equals(passStr, confirm)){
            if(username_error(userStr)){
                //Toast.makeText(this, "Please enter a proper username", Toast.LENGTH_LONG);
                //Show Dialog that info was wrong
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage("Please enter a proper username")
                        .setTitle("Account Creation Failed...")
                        .setNegativeButton("OK", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                dialog.cancel();
                            }
                        });

                AlertDialog aDialog = builder.show();
            }else if(!goodUser){
                //Toast.makeText(this, "That Username already exists.", Toast.LENGTH_LONG);
                //Show Dialog that info was wrong
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage("The Username you want is already taken. Try again")
                        .setTitle("Account Creation Failed...")
                        .setNegativeButton("OK", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                dialog.cancel();
                            }
                        });

                AlertDialog aDialog = builder.show();
            }else{
                //Log.d(TAG, "Got to Account Creation");
                //Log.d(TAG, hash_pass(password.getText().toString()));
                new CreateAccount().execute();

                new LoginAccount().execute();
            }
        }
        else{
            //Password Mismatch functionality
            //Toast.makeText(this, "Password's do not match", Toast.LENGTH_LONG);
            //Show Dialog that info was wrong
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setMessage("The Passwords do not match")
                    .setTitle("Account Creation Failed...")
                    .setNegativeButton("OK", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int id){
                            dialog.cancel();
                        }
                    });

            AlertDialog aDialog = builder.show();
        }
    }

    @Override
    public void onSignupDialogNegativeClick(DialogFragment dialog){
        dialog.getDialog().cancel();
    }

    //Login Dialog Listener implementation
    public void showLoginDialog(FragmentManager fm){
        //create instance of dialog fragment and show it
        DialogFragment loginDialog = new LoginDialogFragment();
        loginDialog.show(fm, "LoginDialogFragment");
    }

    @Override
    public void onLoginDialogPositiveClick(DialogFragment dialog){
        //Login Function
        username = (EditText)dialog.getDialog().findViewById(R.id.login_dialog_username);
        password = (EditText)dialog.getDialog().findViewById(R.id.login_dialog_password);

        userStr = username.getText().toString();
        passStr = password.getText().toString();

        new LoginAccount().execute();
        //Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        //startActivity(i);
    }

    @Override
    public void onLoginDialogNegativeClick(DialogFragment dialog){
        dialog.getDialog().cancel();
    }

    /*
    ** Asynchronous Tasks -- HTTP Calls
     *
     */

    private class CheckUsername extends AsyncTask<Void, Void, Void> {
        String user = userStr;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            //Show progress dialog
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            //Make a request to url and get response
            String resp = sh.makeGetCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/checkusername/" + user, "GET");

            if(resp != null){
                Log.d(TAG, "Check Username Response: " + resp);

                if(resp.equals(good)) {
                    setgood();
                }
                else{
                    setbad();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            //Dismiss the progress dialog
            if(pDialog.isShowing()){
                pDialog.dismiss();
            }
        }
    }

    private class CreateAccount extends AsyncTask<Void, Void, Void> {
        JSONObject jsonObject = new JSONObject();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Show progress dialog
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

            try{
                jsonObject.put("username", userStr);
                jsonObject.put("password", hash_pass(passStr));
            } catch(JSONException e){
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();

            Log.d(TAG, jsonObject.toString());

            //Make a request to url and get response
            String resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/register", jsonObject);

            if(resp != null){
                Log.d(TAG, "Account Creation: " + resp);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //Dismiss the progress dialog
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }
    }

    private class LoginAccount extends AsyncTask<Void, Void, Void> {
        JSONObject jsonObject = new JSONObject();
        boolean start = false;
        String resp;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Show progress dialog
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

            try{
                jsonObject.put("username", userStr);
                jsonObject.put("password", hash_pass(passStr));
            } catch(JSONException e){
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();

            Log.d(TAG, jsonObject.toString());

            //Make a request to url and get response
            resp = sh.makePostCall("https://zvgalu45ka.execute-api.us-east-1.amazonaws.com/prod/login", jsonObject);

            if(resp != null){
                if(login_success(resp))
                    start = true;

                Log.d(TAG, resp);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //Dismiss the progress dialog
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if(start){
                //Go to Home Activity Screen with Session Token
                //Send user info and session token to Home Activity or create a user instance
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                i.putExtra("Username", username.getText().toString());
                i.putExtra("SessionId", resp);
                //Go to House Activity Screen with Session Token
                //Send user info and session token to House Activity or create a user instance
                startActivity(i);
            }
            else{
                //Show Dialog that info was wrong
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage("The username or password given was incorrect.")
                        .setTitle("Login Attempt Failed...")
                        .setNegativeButton("OK", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                dialog.cancel();
                            }
                        });

                AlertDialog aDialog = builder.show();
            }
        }
    }

    /*
    * Misc. Functions
    *
     */

    //Check if login is successful
    public boolean login_success(String resp){
        String temp = "\"Error: Username and password do NOT match\"\n";
        if(resp.equals(temp)){
            Log.d(TAG, "False");
           return false;
        }
        else{
            Log.d(TAG, "True");
            return true;
        }
    }

    //Check if Username contains any spaces
    private boolean username_error(String user){
        if(user.length() >= 40 ){
            return true;
        }
        else if(user.contains(" ")){
            return true;
        }
        else{
            return false;
        }
    }

    private static String hash_pass(String pw) {
        try {
            StringBuffer hexStr = new StringBuffer();
            byte[] hash;

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            hash = digest.digest(pw.getBytes(StandardCharsets.UTF_8));
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xFF & hash[i]);
                if (hex.length() == 1) {
                    hexStr.append('0');
                }
                hexStr.append(hex);
            }

            return hexStr.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
