package com.example.vipul.splash;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookActivity;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.model.ShareLinkContent;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class EventDetails extends ActionBarActivity {
    TextView ed;
    private CallbackManager callbackManager;
    String event="";
    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.drawer_recyclerView)
    RecyclerView drawerRecyclerView;
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    ActionBarDrawerToggle mDrawerToggle;
    MenuItem del;
    ImageView fbshare;
    String objectid;
    Button direction;
    TextView name,description,createdby,from,to,venue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        ButterKnife.inject(this);

        setSupportActionBar(toolbar);
        fbshare=(ImageView)findViewById(R.id.fbshare);
        name=(TextView)findViewById(R.id.eventname);
        description=(TextView)findViewById(R.id.description);
        createdby=(TextView)findViewById(R.id.createdby);
        from=(TextView)findViewById(R.id.fromvariable);
        to=(TextView)findViewById(R.id.tovariable);
        direction=(Button)findViewById(R.id.direction);
        venue=(TextView)findViewById(R.id.venue);

        objectid=getIntent().getExtras().getString("objectId");



        fbshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, ParseUser.getCurrentUser().getUsername()+" is goin to event "+name.getText()+" at "+venue.getText());
                startActivity(Intent.createChooser(shareIntent, "Share your thoughts"));
            }
        });
        direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(EventDetails.this,GetDirections.class);
                i.putExtra("objectId",objectid);
                i.putExtra("place",venue.getText());
                i.putExtra("ename",name.getText());
                startActivity(i);
                finish();
            }
        });

        ActionNew();

        Log.d(getClass().getSimpleName(), "Objectid: " + objectid);


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
        query.getInBackground(objectid, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    name.setText(object.getString("name"));
                    description.setText(object.getString("description"));
                    createdby.setText(object.getString("createdby"));
                    from.setText(object.getString("eventdate")+ " "+ object.getString("eventtime"));
                    to.setText(object.getString("enddate")+ " "+ object.getString("endtime"));
                    venue.setText(object.getString("place"));
                    Log.d(getClass().getSimpleName(), "event: " + event);
                } else {
                    // something went wrong
                }
            }
        });
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

        final GestureDetector mGestureDetector = new GestureDetector(EventDetails.this, new GestureDetector.SimpleOnGestureListener() {

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
                    Toast.makeText(EventDetails.this, "The Item Clicked is: " + recyclerView.getChildPosition(child), Toast.LENGTH_SHORT).show();
                    switch (recyclerView.getChildPosition(child)){
                        case 1:
                            startActivity(new Intent(EventDetails.this,friend_list.class));
                            break;
                        case 2:
                            startActivity(new Intent(EventDetails.this,EventPage.class));
                            break;
                        case 3:
                            startActivity(new Intent(EventDetails.this,EventView.class));
                            break;
                        case 4:
                            startActivity(new Intent(EventDetails.this,MapMe.class));
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
        getMenuInflater().inflate(R.menu.menu_event_details, menu);
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
        if (id==R.id.OK){
            ParseQuery<ParseObject> q=ParseQuery.getQuery("Events");
            q.whereEqualTo("objectId",objectid);
            q.getInBackground(objectid, new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    if (e == null) {
                        try {
                            object.delete();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                        object.saveInBackground();
                        startActivity(new Intent(EventDetails.this,EventView.class));
                        finish();
                    } else {
                        // something went wrong
                    }
                }
            });
        }
        if (id==R.id.edit){
            Intent i =new Intent(EventDetails.this,EventPage.class);
            i.putExtra("objectId",objectid);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
