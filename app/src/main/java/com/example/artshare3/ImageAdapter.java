package com.example.artshare3;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import java.util.HashMap;

public class ImageAdapter extends BaseAdapter {
    private static final String SERVER_ADDRESS = "http://10.0.2.2:8888/";
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private Context context;
    HashMap<String, String[]> myArtHash;

    // Constructor
    public ImageAdapter(Context context) {
        this.context = context;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.loadimage)
                .showImageOnFail(R.drawable.loadimage)
                .showImageOnLoading(R.drawable.loadimage)
                .build();

        myArtHash = ((LoadUserArt) ((Activity)context).getApplication()).getMyArt();
        myArtTitles = myArtHash.keySet().toArray();
        myArtDetails = new String[myArtTitles.length][3];

        for (int i=0; i<myArtTitles.length; i++) {
            String[] cur = myArtHash.get(myArtTitles[i]);
            myArtDetails[i] = cur;
        }
    }

    public int getCount(){
        return myArtTitles.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        imageLoader.displayImage(SERVER_ADDRESS + "pictures/" + myArtDetails[position][2] + "/" +  myArtTitles[position].toString() + ".JPG", imageView, options);
        return imageView;
    }

    public Object[] myArtTitles;
    public String[][] myArtDetails;

}
