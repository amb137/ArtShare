package com.example.artshare3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Register extends Activity {
    private static final String SERVER_ADDRESS = "http://10.0.2.2:8888/";
    EditText name, password, email;
    String Name, Password, Email;
    Context ctx=this;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        name = (EditText) findViewById(R.id.register_name);
        password = (EditText) findViewById(R.id.register_password);
        email = (EditText) findViewById(R.id.register_email);
    }

    public void register_register(View v){
        Name = name.getText().toString();
        Password = password.getText().toString();
        Email = email.getText().toString();
        BackGround b = new BackGround();
        b.execute(Name, Password, Email);
    }

    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params){
            String name = params[0];
            String password = params[1];
            String email = params[2];
            String data = "";
            int tap;

            try {
                URL url = new URL(SERVER_ADDRESS + "register.php");
                String urlParams = "name="+name+"&password="+password+"&email="+email;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();
                InputStream is = httpURLConnection.getInputStream();
                while((tap=is.read())!=-1){
                    data += (char)tap;
                }
                is.close();
                httpURLConnection.disconnect();

                return data;
            } catch (MalformedURLException e){
                e.printStackTrace();
                return "Exception: " +e.getMessage();
            } catch (IOException e){
                e.printStackTrace();
                return "Exception: " +e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("taken")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                builder.setMessage("Error: Username already taken")
                        .setNegativeButton("Retry", null)
                        .create()
                        .show();
            }
            else if(s.equals("")){
                s = "Data saved successfully";
            }
            Toast.makeText(ctx, s, Toast.LENGTH_LONG).show();

            Intent i = new Intent(ctx, Login.class);
            startActivity(i);
        }

    }
}
