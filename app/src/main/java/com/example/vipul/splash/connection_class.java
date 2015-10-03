package com.example.vipul.splash;

import android.app.Application;

import com.facebook.FacebookActivity;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;

/**
 * Created by vipul on 9/21/2015.
 */
public class connection_class extends Application {
    public void onCreate() {
        Parse.initialize(this, "Jdw4J5z7CHWuelyHWnIwbtHgEOeJkQ27TvF3VUJj", "1cl4RxKTDxISFypVnQgUyC0NakO8d2Xgk33MMa4J");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParseFacebookUtils.initialize(this);
    }
}
