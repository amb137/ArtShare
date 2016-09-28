package com.example.artshare3;

import android.app.Application;
import java.util.HashMap;

public class LoadUserArt extends Application {
    private HashMap<String, String[]> myArt;

    public HashMap<String, String[]> getMyArt(){
        return myArt;
    }

    public void setMyArt(HashMap myArt) {
        this.myArt = myArt;
    }

    public void addMyArt(String key, String[] values) {
        myArt.put(key, values);
    }
}
