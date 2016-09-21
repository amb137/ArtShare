package com.example.artshare3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class Login extends Activity {

    private static final String SERVER_ADDRESS = "http://10.0.2.2:8888/";
    EditText email, password;
    String Email, Password;
    Context ctx = this;
    String NAME=null, PASSWORD=null, EMAIL=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        email = (EditText) findViewById(R.id.main_email);
        password = (EditText) findViewById(R.id.main_password);
        ((LoadUserArt) this.getApplication()).setMyArt(new HashMap());

    }

    public void main_register(View v){
        startActivity(new Intent(this,Register.class));
    }

    public void main_login(View v){
        Email = email.getText().toString();
        Password = password.getText().toString();
        BackGround b = new BackGround();
        b.execute(Email, Password);
    }

    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String email = params[0];
            String password = params[1];
            String myData = "";
            int tap;

            try {
                URL url = new URL(SERVER_ADDRESS + "login.php");
                String urlParams = "email="+email+"&password="+password;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                while ((tap = is.read()) != -1) {
                    myData += (char) tap;
                }

                is.close();
                httpURLConnection.disconnect();

                return myData;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject root = new JSONObject(s);
                JSONObject data = root.getJSONObject("data");

                NAME = data.getString("name");
                PASSWORD = data.getString("password");
                EMAIL = data.getString("email");

            } catch (JSONException e) {
                e.printStackTrace();
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                builder.setMessage("Incorrect Login")
                        .setNegativeButton("Retry", null)
                        .create()
                        .show();
                return;
            }

            LoadUserArtData bb = new LoadUserArtData();
            bb.execute(Email);
        }
    }

    private class LoadUserArtData extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String username = params[0];
            String myData = "";
            int tap;

            try {
                URL url = new URL(SERVER_ADDRESS + "loadPics.php");
                String urlParams = "username="+username;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                while ((tap = is.read()) != -1) {
                    myData += (char) tap;
                }

                is.close();
                httpURLConnection.disconnect();

                return myData;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            String err;
            HashMap<String, String[]> myArt = new HashMap<>();

            try {
                JSONObject root = new JSONObject(s);
                JSONArray jArray = root.getJSONArray("artwork");

                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject cur = jArray.getJSONObject(i);
                    String key = cur.getString("title");
                    String[] values = {cur.getString("materials"), cur.getString("description"), cur.getString("username")};
                    myArt.put(key, values);
                }
                Login app = (Login) ctx;
                ((LoadUserArt) app.getApplication()).setMyArt(myArt);

                Intent i = new Intent(ctx, Profile.class);
                i.putExtra("name", NAME);
                i.putExtra("password", PASSWORD);
                i.putExtra("email", EMAIL);
                startActivity(i);

            } catch (JSONException e) {
                e.printStackTrace();
                err = "Exception: " +e.getMessage();
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                builder.setMessage(err)
                        .setNegativeButton("Retry", null)
                        .create()
                        .show();
            }
        }
    }
}
