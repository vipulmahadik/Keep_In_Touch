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
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class Events extends ActionBarActivity {

    Button cr,ve;
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
        setContentView(R.layout.activity_events);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        ActionNew();
        
        cr=(Button)findViewById(R.id.cr_event);
        ve=(Button)findViewById(R.id.ve_events);
        cr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Events.this,EventPage.class));
            }
        });
        ve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Events.this,EventView.class));
            }
        });
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

        final GestureDetector mGestureDetector = new GestureDetector(Events.this, new GestureDetector.SimpleOnGestureListener() {

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
                    Toast.makeText(Events.this, "The Item Clicked is: " + recyclerView.getChildPosition(child), Toast.LENGTH_SHORT).show();
                    switch (recyclerView.getChildPosition(child)){
                        case 1:
                            startActivity(new Intent(Events.this,friend_list.class));
                            break;
                        case 2:
                            break;
                        case 3:
                            startActivity(new Intent(Events.this,MapMe.class));
                            break;
                        case 4:
                            startActivity(new Intent(Events.this,invites.class));
                            break;
                        case 5:
                            startActivity(new Intent(Events.this,facebook.class));
                            break;
                        case 6:
                            startActivity(new Intent(Events.this,group.class));
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
        getMenuInflater().inflate(R.menu.menu_events, menu);
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
