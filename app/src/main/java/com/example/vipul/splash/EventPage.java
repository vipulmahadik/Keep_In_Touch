package com.example.vipul.splash;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class EventPage extends ActionBarActivity {

    int year_x, month_x,day_x,setHour,setMin;
    TextView submit;
    TextView fromdate,fromtime,todate,totime,name;
    EditText title,description;
    static final int DIALOG_ID=0;
    ParseQuery<ParseObject> query;
    String objectid;

    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle

    @InjectView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @InjectView(R.id.toolbar) Toolbar toolbar;
    @InjectView(R.id.drawer_recyclerView) RecyclerView drawerRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);
        ButterKnife.inject(this);
        Calendar cal= Calendar.getInstance();
        year_x=cal.get(Calendar.YEAR);
        month_x=cal.get(Calendar.MONTH);
        day_x=cal.get(Calendar.DAY_OF_MONTH);
        setSupportActionBar(toolbar);
        fromdate=(TextView)findViewById(R.id.date_text);
        fromtime=(TextView)findViewById(R.id.time_text);
        todate=(TextView)findViewById(R.id.date_text_to);
        totime=(TextView)findViewById(R.id.time_text_to);
        name=(TextView)findViewById(R.id.name);
        name.setText(ParseUser.getCurrentUser().getUsername().toString());
        title=(EditText)findViewById(R.id.ename);
        description=(EditText)findViewById(R.id.description);
        submit=(Button)findViewById(R.id.submit);


        ActionNew();

        date_time_setter();
        check_edit();


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Toast.makeText(getApplicationContext(), "Kuch to problem", Toast.LENGTH_SHORT).show();

                    if(getIntent().getExtras().getString("objectId")!=null){
                        query.getInBackground(objectid, new GetCallback<ParseObject>() {
                            public void done(ParseObject object, ParseException e) {
                                if (e == null) {
                                    object.put("name", String.valueOf(title.getText()));
                                    object.put("eventdate", String.valueOf(fromdate.getText()));
                                    object.put("eventtime", String.valueOf(fromtime.getText()));
                                    object.put("enddate", String.valueOf(todate.getText()));
                                    object.put("endtime", String.valueOf(totime.getText()));
                                    object.put("description", String.valueOf(description.getText()));
                                    object.put("createdby", String.valueOf(ParseUser.getCurrentUser().getUsername()));
                                    object.saveInBackground(new SaveCallback() {
                                                                @Override
                                                                public void done(ParseException e) {
                                                                    if (e == null) {
                                                                        Intent intent = new Intent(EventPage.this, EventView.class);
                                                                        startActivity(intent);
                                                                        finish();
                                                                    }
                                                                    else
                                                                    {
                                                                        Toast.makeText(getApplicationContext(), "Event not Posted.Please Re-enter the details and post", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            }

                                    );
                                } else {
                                    // something went wrong
                                }
                            }
                        });
                    }
                    else{


                    }

                }catch (NullPointerException e){
                    ParseObject post = new ParseObject("Events");
                    post.put("name", String.valueOf(title.getText()));
                    post.put("eventdate", String.valueOf(fromdate.getText()));
                    post.put("eventtime", String.valueOf(fromtime.getText()));
                    post.put("enddate", String.valueOf(todate.getText()));
                    post.put("endtime", String.valueOf(totime.getText()));
                    post.put("description", String.valueOf(description.getText()));
                    post.put("createdby", String.valueOf(ParseUser.getCurrentUser().getUsername()));
                    post.saveInBackground(new SaveCallback() {
                                              @Override
                                              public void done(ParseException e) {
                                                  if (e == null) {
                                                      Intent intent = new Intent(EventPage.this, EventView.class);
                                                      startActivity(intent);
                                                      finish();
                                                  } else {
                                                      Toast.makeText(getApplicationContext(), "Event not Posted.Please Re-enter the details and post", Toast.LENGTH_SHORT).show();
                                                  }
                                              }
                                          }

                    );
                }

        }
    });
    }

    private void check_edit() {
        try {
            if(getIntent().getExtras().getString("objectId")!=null){
                objectid=getIntent().getExtras().getString("objectId");
                query = ParseQuery.getQuery("Events");
                query.getInBackground(objectid, new GetCallback<ParseObject>() {
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            title.setText(object.getString("name"));
                            description.setText(object.getString("description"));
                            fromdate.setText(object.getString("eventdate"));
                            fromtime.setText(object.getString("eventtime"));
                            todate.setText(object.getString("enddate"));
                            totime.setText(object.getString("endtime"));
                            submit.setText("Update Event");
                            
                        } else {
                            // something went wrong
                        }
                    }
                });
            }
        }catch (NullPointerException e){

        }

    }

    public void date_time_setter(){
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener dlistener= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                year_x=i;
                month_x=i2+1;
                day_x=i3;
                fromdate.setText(year_x+"-"+month_x+"-"+day_x);
            }
        };

        final TimePickerDialog.OnTimeSetListener timelistnr = new TimePickerDialog.OnTimeSetListener(){

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                setHour=hourOfDay;
                setMin=minute;
                fromtime.setText(setHour+":"+setMin);
            }
        };
        final DatePickerDialog.OnDateSetListener dlistener1= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                year_x=i;
                month_x=i2+1;
                day_x=i3;
                todate.setText(year_x+"/"+month_x+"/"+day_x);
            }
        };

        final TimePickerDialog.OnTimeSetListener timelistnr1 = new TimePickerDialog.OnTimeSetListener(){

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                setHour=hourOfDay;
                setMin=minute;
                totime.setText(setHour+":"+setMin);
            }
        };

        fromdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(EventPage.this, dlistener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        fromtime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new TimePickerDialog(EventPage.this, timelistnr, myCalendar
                        .get(Calendar.HOUR), myCalendar.get(Calendar.MINUTE),
                        true).show();
            }
        });
        todate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(EventPage.this, dlistener1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        totime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new TimePickerDialog(EventPage.this, timelistnr1, myCalendar
                        .get(Calendar.HOUR), myCalendar.get(Calendar.MINUTE),
                        true).show();
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

        DrawerAdapter drawerAdapter = new DrawerAdapter(rows);
        drawerRecyclerView.setAdapter(drawerAdapter);
        drawerRecyclerView.setHasFixedSize(true);
        drawerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.LayoutManager mLayoutManager;

        final GestureDetector mGestureDetector = new GestureDetector(EventPage.this, new GestureDetector.SimpleOnGestureListener() {

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
                    Toast.makeText(EventPage.this, "The Item Clicked is: " + recyclerView.getChildPosition(child), Toast.LENGTH_SHORT).show();
                    switch (recyclerView.getChildPosition(child)){
                        case 1:
                            startActivity(new Intent(EventPage.this,friend_list.class));
                            break;
                        case 2:
                            break;
                        case 3:
                            startActivity(new Intent(EventPage.this,EventView.class));
                            break;
                        case 4:
                            startActivity(new Intent(EventPage.this,MapMe.class));
                            break;
                    }

                    return true;

                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

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
        getMenuInflater().inflate(R.menu.menu_event_page, menu);
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
