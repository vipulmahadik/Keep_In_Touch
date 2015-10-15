package com.example.vipul.splash;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

public class MainActivity extends ActionBarActivity {

    private GoogleMap googleMap;
    private LocationManager locationManager;
    private ParseGeoPoint point1;
    double x,y;

    private ParseUser user= ParseUser.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        try {
            // Loading map
            initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }



            locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
            LocationListener listener= new LocationListener() {
                @Override
                public void onLocationChanged(Location l) {

                    Toast.makeText(MainActivity.this,l.getLatitude()+" and "+l.getLongitude(),Toast.LENGTH_LONG).show();

// create marker
                    MarkerOptions marker = new MarkerOptions().position(new LatLng(l.getLatitude(), l.getLongitude())).title("Hello Maps ");

// adding marker
                    googleMap.addMarker(marker);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(l.getLatitude(), l.getLongitude()), 5));
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);
                    x=l.getLatitude();
                    y=l.getLongitude();
                    point1= new ParseGeoPoint(x,y);
                    user.put("coords",point1);
                    user.saveEventually();
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,10,listener);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        initilizeMap();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
