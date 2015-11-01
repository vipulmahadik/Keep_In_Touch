package com.example.vipul.splash;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class GetDirections extends ActionBarActivity {

    private GoogleMap map;
    ArrayList<LatLng> markerPoints;
    Geocoder geocoder;
    List<Address> destination;
    List<Address> source;
    String place;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_directions);

        // Initializing

        place=getIntent().getExtras().getString("place");
        Log.i("destination: ",place);
        markerPoints = new ArrayList<LatLng>();
        geocoder = new Geocoder(GetDirections.this);
        try {
            destination=geocoder.getFromLocationName(place,1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Getting reference to         SupportMapFragment of the activity_main
        SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map1);
        map=fm.getMap();

        // Getting Map for the SupportMapFragment

        if(map!=null){

            // Enable MyLocation Button in the Map
            map.setMyLocationEnabled(true);
            LocationManager locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000L,500.0f, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

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
            });
            Location location1 = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


            // Setting onclick event listener for the map

            LatLng origin = new LatLng(location1.getLatitude(),location1.getLongitude());
            LatLng dest = new LatLng(destination.get(0).getLatitude(),destination.get(0).getLongitude());

            MarkerOptions markero=new MarkerOptions().position(origin);
            MarkerOptions markerd=new MarkerOptions().position(origin);
            map.addMarker(markero);
            map.addMarker(markerd);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(origin, 5));
            map.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);

            // Getting URL to the Google Directions API
            String url = getDirectionsUrl(origin, dest);

            DownloadTask downloadTask = new DownloadTask();

            // Start downloading json data from Google Directions API
            downloadTask.execute(url);

//            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//
//                @Override
//                public void onMapClick(LatLng point) {
//
//                    // Already two locations
//                    if(markerPoints.size()>1){
//                        markerPoints.clear();
//                        map.clear();
//                    }
//
//                    // Adding new item to the ArrayList
//                    markerPoints.add(point);
//
//                    // Creating MarkerOptions
//                    MarkerOptions options = new MarkerOptions();
//
//                    // Setting the position of the marker
//                    options.position(point);
//
//                    /**
//                     * For the start location, the color of marker is GREEN and
//                     * for the end location, the color of marker is RED.
//                     */
//                    if(markerPoints.size()==1){
//                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//                    }else if(markerPoints.size()==2){
//                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//                    }
//
//
//                    // Add new marker to the Google Map Android API V2
//                    map.addMarker(options);
//
//                    // Checks, whether start and end locations are captured
//                    if(markerPoints.size() >= 2){
//                        LatLng origin = markerPoints.get(0);
//                        LatLng dest = markerPoints.get(1);
//
//                        // Getting URL to the Google Directions API
//                        String url = getDirectionsUrl(origin, dest);
//
//                        DownloadTask downloadTask = new DownloadTask();
//
//                        // Start downloading json data from Google Directions API
//                        downloadTask.execute(url);
//                    }
//
//                }
//            });
        }
    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;


        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while d.U.", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }



    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.BLUE);

            }

            // Drawing polyline in the Google Map for the i-th route
            map.addPolyline(lineOptions);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_get_directions, menu);
        return true;
    }
}