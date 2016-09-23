package com.example.artshare3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class Details extends Activity {
    private static final String SERVER_ADDRESS = "http://10.0.2.2:8888/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        //initialize variables for layout references
        ImageView imageView = (ImageView) findViewById(R.id.DetailImage);
        TextView curTitle = (TextView) findViewById(R.id.tvTitle);
        TextView curMat = (TextView) findViewById(R.id.tvMaterials);
        TextView curDesc = (TextView) findViewById(R.id.tvDescription);

        //load Android Universal Image loader library
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(Details.this));
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.loadimage)
                .showImageOnFail(R.drawable.loadimage)
                .showImageOnLoading(R.drawable.loadimage)
                .build();

        //get image position on grid from ImageAdapter
        Intent i = getIntent();
        int position = i.getExtras().getInt("id");
        ImageAdapter imageAdapter = new ImageAdapter(this);

        //set variables for additional image's artwork information
        String title = imageAdapter.myArtTitles[position].toString();
        String materials = imageAdapter.myArtDetails[position][0];
        String description = imageAdapter.myArtDetails[position][1];
        String username = imageAdapter.myArtDetails[position][2];

        //load image and information into current page
        imageLoader.displayImage(SERVER_ADDRESS + "pictures/" + username + "/" + title + ".JPG", imageView, options);
        curTitle.setText(title);
        curMat.setText(materials);
        curDesc.setText(description);
    }
}

