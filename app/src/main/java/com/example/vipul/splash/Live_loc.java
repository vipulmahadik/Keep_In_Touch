package com.example.vipul.splash;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class Live_loc extends ActionBarActivity {

    GoogleMap map;
    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.drawer_recyclerView)
    RecyclerView drawerRecyclerView;
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    ActionBarDrawerToggle mDrawerToggle;
    ArrayList<String> s1;
    ArrayList<ParseObject> grpmem,locmem;
    ArrayList<ParseUser> ulist;
    String objid;
    Collection<String> memincoll;
    ArrayList<LatLng> markerPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_loc);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        ActionNew();
        SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map_live);
        map=fm.getMap();

        objid=getIntent().getExtras().getString("objid");
        Log.d("Object id from past: ",objid);

        ParseQuery<ParseObject> mem=ParseQuery.getQuery("eventmember");
        mem.whereEqualTo("e_name",objid).findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> parseObjects, ParseException e) {
                grpmem=new ArrayList<ParseObject>(parseObjects);
                s1=new ArrayList<String>();
                for (int x=0;x<grpmem.size();x++){
                    Log.d("Count : ", grpmem.get(x).getString("event_members"));
                    s1.add(grpmem.get(x).getString("event_members"));
                }
                Collection<String> c=new ArrayList(s1);
                ParseUser.getQuery().whereContainedIn("objectId",c).findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> parseUsers, ParseException e) {
                        ulist = new ArrayList<ParseUser>(parseUsers);
                        for (ParseUser u : ulist){
                            Log.d("User : ",u.getObjectId()+" "+u.getUsername());
                            ParseGeoPoint g=(ParseGeoPoint)u.get("coords");
                            MarkerOptions markerd=new MarkerOptions().position(new LatLng(g.getLatitude(),g.getLongitude())).title(u.getUsername());
                            map.addMarker(markerd);
                        }
                    }
                });
            }
        });

        if(map!=null){
            markerPoints = new ArrayList<LatLng>();
            map.setMyLocationEnabled(true);

        }






    }

    private void ActionNew() {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        List<String> rows = new ArrayList<>();
        rows.add("Find Friends");
        rows.add("Create Event");
        rows.add("View Events");
        rows.add("Map Me");

        Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        int icons[]=new int[6];
        DrawerAdapter drawerAdapter = new DrawerAdapter(rows,icons,font);
        drawerRecyclerView.setAdapter(drawerAdapter);
        drawerRecyclerView.setHasFixedSize(true);
        drawerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.LayoutManager mLayoutManager;

        final GestureDetector mGestureDetector = new GestureDetector(Live_loc.this, new GestureDetector.SimpleOnGestureListener() {

            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });

        drawerRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(),motionEvent.getY());



                if(child!=null && mGestureDetector.onTouchEvent(motionEvent)){
                    Drawer.closeDrawers();
                    Toast.makeText(Live_loc.this, "The Item Clicked is: " + recyclerView.getChildPosition(child), Toast.LENGTH_SHORT).show();
                    switch (recyclerView.getChildPosition(child)){
                        case 1:
                            startActivity(new Intent(Live_loc.this,friend_list.class));
                            break;
                        case 2:
                            startActivity(new Intent(Live_loc.this,EventPage.class));
                            break;
                        case 3:
                            startActivity(new Intent(Live_loc.this,EventView.class));
                            break;
                        case 4:
                            startActivity(new Intent(Live_loc.this,MapMe.class));
                            break;
                    }

                    return true;

                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }
        });

        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager

        drawerRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager


        Drawer = (DrawerLayout) findViewById(R.id.drawer_layout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this,Drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }



        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_live_loc, menu);
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
