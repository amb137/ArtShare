package com.example.artshare3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class Profile extends Activity implements View.OnClickListener {

    private static final int RESULT_LOAD_IMAGE = 1;
    private static final String SERVER_ADDRESS = "http://10.0.2.2:8888/";

    ImageView chooseImage;
    Button bUploadImage;
    EditText uploadName, uploadDesc, uploadMat;
    String name, username, title, materials, description;
    TextView nameTV, emailTV;
    Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        //initialize variables for layout references
        nameTV = (TextView) findViewById(R.id.Name);
        emailTV = (TextView) findViewById(R.id.Username);
        chooseImage = (ImageView) findViewById(R.id.chooseImage);
        bUploadImage = (Button) findViewById(R.id.bUpload);

        //get name & username input from Login
        name = getIntent().getStringExtra("name");
        username = getIntent().getStringExtra("email");
        nameTV.setText(name);
        emailTV.setText(username);

        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        //create tabs
        TabHost.TabSpec spec = host.newTabSpec("Tab 1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("My Art");
        host.addTab(spec);

        spec = host.newTabSpec("Tab 2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("My Pins");
        host.addTab(spec);

        spec = host.newTabSpec("Tab 3");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Upload");
        host.addTab(spec);

        //helper method
        loadGridViewHelper();

        //listen for image selection or upload
        chooseImage.setOnClickListener(this);
        bUploadImage.setOnClickListener(this);
    }

    //called when app is initialized and after new image is uploaded
    public void loadGridViewHelper(){

        uploadName = (EditText) findViewById(R.id.etName);
        uploadMat = (EditText) findViewById(R.id.etDescription);
        uploadDesc = (EditText) findViewById(R.id.etMaterials);
        chooseImage = (ImageView) findViewById(R.id.chooseImage);

        //initialize gridview and call helper ImageAdapter class
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        //for image click, open activity 'Details' and pass id parameter
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id){
                Intent i = new Intent(getApplicationContext(), Details.class);
                i.putExtra("id", position);
                startActivity(i);
            }
        });

        //clear entry fields after upload
        uploadName.setText("");
        uploadMat.setText("");
        uploadDesc.setText("");
        chooseImage.setImageResource(R.drawable.uploadimage);
    }

    @Override
    public void onClick(View v) {

        title = ((EditText) findViewById(R.id.etName)).getText().toString();
        materials = ((EditText) findViewById(R.id.etMaterials)).getText().toString();
        description = ((EditText) findViewById(R.id.etDescription)).getText().toString();

        switch(v.getId()){

            case R.id.chooseImage:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                break;

            case R.id.bUpload:
                Bitmap image = ((BitmapDrawable) chooseImage.getDrawable()).getBitmap();
                new UploadImage(username, image, title, materials, description).execute();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //chosen image from gallery, set image in view
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            chooseImage.setImageURI(selectedImage);
        }
    }

    private class UploadImage extends AsyncTask<Void, Void, Void> {
        String username;
        Bitmap image;
        String title;
        String materials;
        String description;

        //create constructor
        public UploadImage(String username, Bitmap image, String title, String materials, String description){
            this.username = username;
            this.image = image;
            this.title = title;
            this.materials = materials;
            this.description = description;
        }

        //upload image: run task asynchronously, don't slow down UI thread
        @Override
        protected Void doInBackground(Void... params) {

            //compress bitmap to string
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

            //create data map to send to the server
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("image", encodedImage);
            dataToSend.put("name", title);
            String encodedStr = getEncodedData(dataToSend);

            try {

                //connecting to server & pass parameters
                URL url = new URL(SERVER_ADDRESS + "savePic.php");
                String urlParams = "username=" + username + "&image=" + encodedStr + "&title=" + title + "&materials=" + materials + "&description=" + description;
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                Profile app = (Profile) ctx;
                String[] values = {materials, description, username};

                //add uploaded image to our HashMap class of artwork data
                ((LoadUserArt) app.getApplication()).addMyArt(title, values);

                con.setDoOutput(true);
                OutputStream os = con.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is = con.getInputStream();
                is.close();
                con.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //display success message to user
            Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
            loadGridViewHelper();
        }
    }

    private String getEncodedData(Map<String,String> data) {
        StringBuilder sb = new StringBuilder();
        for(String key : data.keySet()) {
            String value = null;
            try {
                value = URLEncoder.encode(data.get(key),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if(sb.length()>0)
                sb.append("&");

            sb.append(key + "=" + value);
        }
        return sb.toString();
    }

}
