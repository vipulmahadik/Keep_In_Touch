package com.example.vipul.splash;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.CalendarContract;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.provider.CalendarContract.ACTION_EVENT_REMINDER;
import static android.provider.CalendarContract.Events;
import static android.provider.CalendarContract.Events.*;


public class invites extends ActionBarActivity {

    List<ParseObject> ilist,elist;
    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.drawer_recyclerView)
    RecyclerView drawerRecyclerView;
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invites);
        ButterKnife.inject(this);

        setSupportActionBar(toolbar);

        ActionNew();

        loadlist();
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
        rows.add("Invites");

        Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        int icons[]=new int[6];
        DrawerAdapter drawerAdapter = new DrawerAdapter(rows,icons,font);
        drawerRecyclerView.setAdapter(drawerAdapter);
        drawerRecyclerView.setHasFixedSize(true);
        drawerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.LayoutManager mLayoutManager;

        final GestureDetector mGestureDetector = new GestureDetector(invites.this, new GestureDetector.SimpleOnGestureListener() {

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
                    Toast.makeText(invites.this, "The Item Clicked is: " + recyclerView.getChildPosition(child), Toast.LENGTH_SHORT).show();
                    switch (recyclerView.getChildPosition(child)){
                        case 1:
                            startActivity(new Intent(invites.this,friend_list.class));
                            break;
                        case 2:
                            startActivity(new Intent(invites.this,EventPage.class));
                            break;
                        case 3:
                            startActivity(new Intent(invites.this,EventView.class));
                            break;
                        case 4:
                            startActivity(new Intent(invites.this,MapMe.class));
                            break;
                        case 5:
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

    private void loadlist() {

        ParseQuery query=ParseQuery.getQuery("eventmember");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                ilist = new ArrayList<ParseObject>();
                for (ParseObject obj :parseObjects){
                    Log.d("Exception 0",obj.get("event_members")+" "+ParseUser.getCurrentUser().getObjectId() );
                    if (obj.get("event_members").toString().equals(ParseUser.getCurrentUser().getObjectId())){
                        ilist.add(obj);
                        Log.d("Exception 1",ParseUser.getCurrentUser().getObjectId() );
                    }
                }
                elist=new ArrayList<ParseObject>();
                ParseQuery query1=ParseQuery.getQuery("Events");
                query1.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {
                        for (ParseObject obj : parseObjects) {
                            Log.d("Exception 2", obj.getObjectId());
                            for (ParseObject e1 : ilist) {
                                Log.d("Exception 3", obj.getObjectId() + " " + e1.get("e_name"));
                                if (obj.getObjectId().toString().equals(e1.get("e_name").toString())) {
                                    elist.add(obj);
                                    Log.d("Exception 4", e1.getObjectId());
                                }
                            }
                        }
                        ListView li = (ListView) findViewById(R.id.invite_list1);
                        li.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                        li.setAdapter(new InviteAdapter());
                    }
                });
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_invites, menu);
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

    private class InviteAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return elist.size();
        }

        @Override
        public ParseObject getItem(int i) {
            return elist.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view==null){
                view=getLayoutInflater().inflate(R.layout.invite_row_new, null);
            }
            final ParseObject c= getItem(i);
            final Button accept=(Button)view.findViewById(R.id.accept);
            final Button decline=(Button)view.findViewById(R.id.decline);
            final Button addtocal=(Button)view.findViewById(R.id.addtocal);
            String[] projection = new String[] { "_id", "name" };
            final Uri calendars = Uri.parse("content://calendar/calendars");
            final Cursor managedCursor = getContentResolver().query(calendars, projection, "selected=1", null, null);
            addtocal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Date startdate,enddate;
                    String start=new String(c.getString("eventdate")+"T"+c.getString("eventtime").replace(" ","")+"Z");
                    String end=new String(c.getString("enddate")+"T"+c.getString("endtime").replace(" ","")+"Z");
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
                    startdate=new Date();
                    enddate=new Date();
                    try {
                        startdate = format.parse(start);
                        enddate=format.parse(end);
                        System.out.println(startdate);
                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(startdate);
                    Intent intent = new Intent(Intent.ACTION_EDIT);
                    intent.setType("vnd.android.cursor.item/event");
                    intent.putExtra("beginTime", cal.getTimeInMillis());
                    cal.setTime(enddate);
                    intent.putExtra("allDay", false);
                    intent.putExtra("rrule", "FREQ=YEARLY");
                    intent.putExtra("endTime", cal.getTimeInMillis());
                    intent.putExtra("title", c.getString("name"));
                    intent.putExtra("hasAlarm", 1);
                    startActivity(intent);
                }
            });
            final TextView status=(TextView)view.findViewById(R.id.status);
            for (ParseObject i2:ilist){
                if (i2.get("e_name").toString().equals(c.getObjectId())){
                    if (i2.get("status")==true){
                        status.setText("Accepted");
                        accept.setEnabled(false);
                    }
                    else if (i2.get("status")==false){
                        status.setText("Declined");
                        addtocal.setEnabled(false);
                        decline.setEnabled(false);
                    }
                    else{
                        status.setText("Not defined");
                        addtocal.setEnabled(false);
                        accept.setEnabled(true);
                        decline.setEnabled(true);
                    }

                }
            }

            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (ParseObject i:ilist){
                        if (i.get("e_name").toString().equals(c.getObjectId())){
                            i.put("status",true);
                            status.setText("Accepted");
                            i.saveEventually();
                        }
                        addtocal.setEnabled(true);
                        accept.setEnabled(false);
                        decline.setEnabled(true);
                    }
                }
            });
            decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (ParseObject i:ilist){
                        if (i.get("e_name").toString().equals(c.getObjectId())){
                            i.put("status",false);
                            status.setText("Declined");
                            i.saveEventually();
                        }
                        addtocal.setEnabled(false);
                        accept.setEnabled(true);
                        decline.setEnabled(false);
                    }
                }
            });


            TextView label= (TextView)view.findViewById(R.id.eventname);
            TextView createdby= (TextView)view.findViewById(R.id.by);

            createdby.setText("invited by: "+c.getString("createdby"));
            label.setText(""+c.getString("name"));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(invites.this,EventDetails.class);
                    i.putExtra("objectId",c.getObjectId());
                    startActivity(i);
                    finish();
                }
            });
            return view;
        }
    }
}
