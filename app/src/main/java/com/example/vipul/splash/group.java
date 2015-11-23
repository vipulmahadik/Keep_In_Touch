package com.example.vipul.splash;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class group extends ActionBarActivity {

    private ArrayList<ParseObject> glist;
    private ArrayList<ParseUser> ulist,users;
    ArrayList<String> finaluser;
    ListView li;
    TextView t;
    String m_Text="";
    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.drawer_recyclerView)
    RecyclerView drawerRecyclerView;
    DrawerLayout Drawer;             // Declaring DrawerLayout
    ActionBarDrawerToggle mDrawerToggle;
    private static final String TAG = "DemoActivity";
    private SlidingUpPanelLayout mLayout;
    UserAdapter1 adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        ButterKnife.inject(this);

        setSupportActionBar(toolbar);
        ActionNew();

        ParseQuery<ParseObject> query=ParseQuery.getQuery("group");
        query.addDescendingOrder("createdAt").findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                glist = new ArrayList<ParseObject>(parseObjects);
                ListView li = (ListView) findViewById(R.id.group_list);
                li.setAdapter(new UserAdapter());
            }
        });
        users=new ArrayList<>();
        addslider();
    }

    private void ActionNew() {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");


        int icons[]=new int[6];
        icons[0]=R.string.friends;
        icons[1]=R.string.icon_heart;
        icons[2]=R.string.map;
        icons[3]=R.string.invi;
        icons[4]=R.string.faceb;
        icons[5]=R.string.icon_heart;

        List<String> rows = new ArrayList<>();
        rows.add("Find Friends");
        rows.add("Events");
        rows.add("Map Me");
        rows.add("Invites");
        rows.add("Facebook");
        rows.add("Group");

        DrawerAdapter drawerAdapter = new DrawerAdapter(rows,icons,font);
        drawerRecyclerView.setAdapter(drawerAdapter);
        drawerRecyclerView.setHasFixedSize(true);
        drawerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.LayoutManager mLayoutManager;

        final GestureDetector mGestureDetector = new GestureDetector(group.this, new GestureDetector.SimpleOnGestureListener() {

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
                    Toast.makeText(group.this,"The Item Clicked is: "+recyclerView.getChildPosition(child),Toast.LENGTH_SHORT).show();
                    switch (recyclerView.getChildPosition(child)){
                        case 1:
                            break;
                        case 2:
                            startActivity(new Intent(group.this,Events.class));
                            break;
                        case 3:
                            startActivity(new Intent(group.this,MapMe.class));
                            break;
                        case 4:
                            startActivity(new Intent(group.this,invites.class));
                            break;
                        case 5:
                            startActivity(new Intent(group.this,facebook.class));
                            break;
                        case 6:
                            startActivity(new Intent(group.this,group.class));
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


    public void addslider() {

        ListView lv = (ListView) findViewById(R.id.list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(group.this, "onItemClick", Toast.LENGTH_SHORT).show();
            }
        });

        getUserList();

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelExpanded(View panel) {
                Log.i(TAG, "onPanelExpanded");

            }

            @Override
            public void onPanelCollapsed(View panel) {
                Log.i(TAG, "onPanelCollapsed");

            }

            @Override
            public void onPanelAnchored(View panel) {
                Log.i(TAG, "onPanelAnchored");
            }

            @Override
            public void onPanelHidden(View panel) {
                Log.i(TAG, "onPanelHidden");
            }
        });
        Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");

        t = (TextView) findViewById(R.id.name1);
        t.setText(R.string.friends);
        t.setTypeface(font);
    }

    private void getUserList() {
        li = (ListView) findViewById(R.id.list);
        finaluser= new ArrayList<String>();
        ParseUser.getQuery().whereNotEqualTo("username", "").findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                if (parseUsers != null) {
                    if (parseUsers.size() == 0)
                        Toast.makeText(group.this, "No User Found", Toast.LENGTH_LONG).show();
                    ulist = new ArrayList<>(parseUsers);
                    li.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
                    adapter = new UserAdapter1();
                    li.setAdapter(adapter);
                    li.setMultiChoiceModeListener(new ModeCallback());

                } else {
                    Toast.makeText(group.this, "Errorrrr", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_group, menu);
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


    private class UserAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return glist.size();
        }

        @Override
        public ParseObject getItem(int i) {
            return glist.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view==null){
                view=getLayoutInflater().inflate(R.layout.group_member_row, null);
            }
            final ParseObject c= getItem(i);
            TextView gname= (TextView)view.findViewById(R.id.gname);
            TextView gcreated= (TextView)view.findViewById(R.id.gcreated);
            TextView gmember= (TextView)view.findViewById(R.id.gmembers);
            gname.setText(c.getString("groupname"));
            gcreated.setText(c.getString("admin"));
            gmember.setText(c.getString("membername"));
            return view;
        }
    }

    public class UserAdapter1 extends BaseAdapter {
        private View.OnClickListener dialog;

        @Override
        public int getCount() {
            return ulist.size();
        }

        @Override
        public ParseUser getItem(int i) {
            return ulist.get(i);
        }

        public String getUsernameThis(int i){final ParseUser p=getItem(i);return p.getUsername();}
        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.member_list, null);
            }
            final ParseUser c = getItem(i);
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(group.this);
//                    builderSingle.setIcon(R.drawable.male);
//                    builderSingle.setTitle("Select One Name:-");
//
//                    final ArrayAdapter<ParseObject> arrayAdapter = new ArrayAdapter<>(
//                            group.this,
//                            android.R.layout.select_dialog_singlechoice);
//                    ParseQuery<ParseObject> eve=ParseQuery.getQuery("events");
//                    eve.findInBackground(new FindCallback<ParseObject>() {
//                        @Override
//                        public void done(List<ParseObject> parseObjects, ParseException e) {
//                            arrayAdapter.addAll(parseObjects);
//                        }
//                    });
//
//
//                    builderSingle.setNegativeButton(
//                            "cancel",
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            });
//
//                    builderSingle.setAdapter(
//                            arrayAdapter,
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    ParseObject strName = arrayAdapter.getItem(which);
//                                    AlertDialog.Builder builderInner = new AlertDialog.Builder(
//                                            group.this);
//                                    builderInner.setMessage(strName.getString("ename"));
//                                    builderInner.setTitle("Your Selected Item is");
//                                    builderInner.setPositiveButton(
//                                            "Ok",
//                                            new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(
//                                                        DialogInterface dialog,
//                                                        int which) {
//                                                    dialog.dismiss();
//                                                }
//                                            });
//                                    builderInner.show();
//                                }
//                            });
//                    builderSingle.show();
//                }
//            });
            TextView label = (TextView) view.findViewById(R.id.label);
            label.setText(c.getUsername());
            return view;
        }

    }


    public class ModeCallback implements ListView.MultiChoiceModeListener {

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.selected_item, menu);
            mode.setTitle("Select Items");
            return true;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.print_item) {
                SparseBooleanArray a=li.getCheckedItemPositions();
                for (int i=0;i<li.getAdapter().getCount();i++){
                    if (a.get(i)){
                        users.add(ulist.get(i));
                    }
                }
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                AlertDialog.Builder builder = new AlertDialog.Builder(group.this);
                builder.setTitle("Group Name");

// Set up the input
                final EditText input = new EditText(group.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        ParseObject o=new ParseObject("group");
                        o.put("admin",ParseUser.getCurrentUser().getUsername());
                        String mems=new String();
                        for (ParseUser p :users){
                            mems+=p.getUsername()+", ";
                        }
                        o.put("membername",mems.substring(0,mems.length()-2));
                        o.put("groupname",m_Text);
                        o.saveEventually();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                mode.finish();
                builder.show();
            }
            return true;
        }

        public void onDestroyActionMode(ActionMode mode) {
        }

        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            final int checkedCount = li.getCheckedItemCount();
            switch (checkedCount) {
                case 0:
                    t.setText("Add Members");
                    break;
                case 1:
                    t.setText("One item selected");
                    break;
                default:
                    t.setText("" + checkedCount + " items selected");
                    break;
            }
        }


    }

}

