package com.example.vipul.splash;

import android.app.ActionBar;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class friend_list extends ActionBarActivity {


    private String latituteField;
    private String longitudeField;
    private LocationManager locationManager;
    private String provider;
    private ArrayList<ParseUser> ulist;
    @InjectView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @InjectView(R.id.toolbar) Toolbar toolbar;
    @InjectView(R.id.drawer_recyclerView) RecyclerView drawerRecyclerView;
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle
    ParseUser user= new ParseUser();
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "ContactDetails" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        ButterKnife.inject(this);

        setSupportActionBar(toolbar);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        LocationManager locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location1 = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        sharedpreferences.edit().putString("latitude",location1.getLatitude()+"").apply();
        sharedpreferences.edit().putString("longitude",location1.getLongitude()+"").apply();
        Log.d(location1.getLatitude()+"",location1.getLongitude()+"");

        ActionNew();
    }

    private void ActionNew() {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");


        int icons[]=new int[7];
        icons[0]=R.string.friends;
        icons[1]=R.string.icon_heart;
        icons[2]=R.string.icon_heart;
        icons[3]=R.string.map;
        icons[4]=R.string.invi;
        icons[5]=R.string.faceb;
        icons[6]=R.string.icon_heart;

        List<String> rows = new ArrayList<>();

        DrawerAdapter drawerAdapter = new DrawerAdapter(rows,icons,font);
        drawerRecyclerView.setAdapter(drawerAdapter);
        drawerRecyclerView.setHasFixedSize(true);
        drawerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.LayoutManager mLayoutManager;

        final GestureDetector mGestureDetector = new GestureDetector(friend_list.this, new GestureDetector.SimpleOnGestureListener() {

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
                    Toast.makeText(friend_list.this,"The Item Clicked is: "+recyclerView.getChildPosition(child),Toast.LENGTH_SHORT).show();
                    switch (recyclerView.getChildPosition(child)){
                        case 1:
                            break;
                        case 2:
                            startActivity(new Intent(friend_list.this,Events.class));
                            break;
                        case 3:
                            startActivity(new Intent(friend_list.this,MapMe.class));
                            break;
                        case 4:
                            startActivity(new Intent(friend_list.this,invites.class));
                            break;
                        case 5:
                            startActivity(new Intent(friend_list.this,facebook.class));
                            break;
                        case 6:
                            startActivity(new Intent(friend_list.this,group.class));
                            break;
                        case 7:
                            startActivity(new Intent(friend_list.this,EmergencyContact.class));
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
    protected void onDestroy(){
        super.onDestroy();

    }

    @Override
    protected void onResume(){
        super.onResume();
        loadList();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    private void loadList() {
        final ProgressDialog dlg=new ProgressDialog(friend_list.this);
        dlg.setTitle("Please wait.. loading users");
        ParseUser.getQuery().whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername()).findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                dlg.dismiss();
                if (parseUsers != null){
                    if (parseUsers.size()==0)
                        Toast.makeText(friend_list.this,"No User Found",Toast.LENGTH_LONG).show();
                    ulist=new ArrayList<ParseUser>(parseUsers);
                    ListView li=(ListView)findViewById(R.id.user_list);
                    li.setAdapter(new UserAdapter());
                }
                else {
                    Toast.makeText(friend_list.this,"Errorrrr",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friend_list, menu);
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



    private class UserAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return ulist.size();
        }

        @Override
        public ParseUser getItem(int i) {
            return ulist.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view==null){
                view=getLayoutInflater().inflate(R.layout.activity_chat_item, null);
            }
            final ParseUser c= getItem(i);

            TextView label= (TextView)view.findViewById(R.id.label);
            label.setText(c.getUsername());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                    installation.put("user",ParseUser.getCurrentUser());
                    installation.saveInBackground();


                    ParseQuery pushQuery = ParseInstallation.getQuery();

// Send push notification to query

                    ParsePush push = new ParsePush();
                    ParseGeoPoint g=(ParseGeoPoint)c.get("coords");
                    push.setQuery(pushQuery); // Set our Installation query
                    push.setMessage(g.getLatitude()+"  "+g.getLongitude());
                    Toast.makeText(friend_list.this,g.getLatitude()+"  "+g.getLongitude(),Toast.LENGTH_LONG).show();
                    push.sendInBackground();
                    Intent i= new Intent(friend_list.this,MainActivity.class);
                    i.putExtra("name",c.getUsername());
                    Bundle b = new Bundle();
                    b.putDouble("latitude", g.getLatitude());
                    b.putDouble("longitude", g.getLongitude());
                    i.putExtras(b);
                    startActivity(i);
                }
            });
            return view;
        }
    }

}