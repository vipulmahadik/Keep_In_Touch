package com.example.vipul.splash;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import static android.provider.CalendarContract.ACTION_EVENT_REMINDER;
import static android.provider.CalendarContract.Events;
import static android.provider.CalendarContract.Events.*;


public class invites extends ActionBarActivity {

    List<ParseObject> ilist,elist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invites);

        loadlist();
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
            Button accept=(Button)view.findViewById(R.id.accept);
            Button decline=(Button)view.findViewById(R.id.decline);
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
                    }
                    else if (i2.get("status")==false){
                        status.setText("Declined");
                    }
                    else
                        status.setText("Not defined");
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
