package com.example.vipul.splash;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by vipul on 11/14/2015.
 */
public class ChatHeadService extends Service {

    private WindowManager windowManager;
    private ImageView chatHead;
    WindowManager.LayoutParams params;
    private GestureDetector gestureDetector;
    @Override
    public void onCreate() {
        super.onCreate();
        gestureDetector = new GestureDetector(this, new SingleTapConfirm());

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        chatHead = new ImageView(this);
        chatHead.setImageResource(R.drawable.ic_action);

        params= new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;

        //this code is for dragging the chat head
        chatHead.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    // single tap
                    Toast.makeText(getApplicationContext(),"Touched Widget",Toast.LENGTH_LONG).show();
                    Log.d("acd"," ADCAD");
                    return true;
                } else {
                    // your code for move and drag
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            initialX = params.x;
                            initialY = params.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            return true;
                        case MotionEvent.ACTION_UP:
                            return true;
                        case MotionEvent.ACTION_MOVE:
                            params.x = initialX
                                    + (int) (event.getRawX() - initialTouchX);
                            params.y = initialY
                                    + (int) (event.getRawY() - initialTouchY);
                            windowManager.updateViewLayout(chatHead, params);
                            return true;
                    }
                }
                return false;
            }
        });
        windowManager.addView(chatHead, params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chatHead != null)
            windowManager.removeView(chatHead);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            Toast.makeText(getApplicationContext(),"Touched Widget me",Toast.LENGTH_LONG).show();
            EmergencyContact e1=new EmergencyContact();
            List<Address> addresses;
            SmsManager sms = SmsManager.getDefault();
            SharedPreferences sharedpreferences = getSharedPreferences("ContactDetails", Context.MODE_PRIVATE);

            Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(Double.parseDouble(sharedpreferences.getString("latitude", "")),
                        Double.parseDouble(sharedpreferences.getString("longitude", "")), 1);
                Address a=addresses.get(0);
                String add=a.getAddressLine(0);
                String date = (DateFormat.format("dd-MM-yyyy hh:mm:ss", new java.util.Date()).toString());
                sms.sendTextMessage(sharedpreferences.getString("number", ""), null, "Need Help. I am at "
                        +add+" \n Latitude: "+sharedpreferences.getString("latitude", "")+"\n & Longitude: "
                        +sharedpreferences.getString("longitude", "")+"\n"+date, null,null);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
    }
}
