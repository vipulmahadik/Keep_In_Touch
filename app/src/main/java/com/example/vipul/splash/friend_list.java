package com.example.vipul.splash;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class friend_list extends ActionBarActivity {


    private String latituteField;
    private String longitudeField;
    private LocationManager locationManager;
    private String provider;
    private ArrayList<ParseUser> ulist;

    ParseUser user= new ParseUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        updateUserStatus(true);
    }

    public void updateUserStatus(boolean b) {
        user.put("online",b);
        user.saveEventually();
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
    @Override
    protected void onDestroy(){
        super.onDestroy();
        updateUserStatus(false);

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
                    li.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                        }
                    });
                }
                else {
                    Toast.makeText(friend_list.this,"Errorrrr",Toast.LENGTH_LONG).show();
                }
            }
        });
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
            final EditText t1=(EditText)view.findViewById(R.id.text1);
            label.setText(c.getUsername());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(friend_list.this,c.getUsername(),Toast.LENGTH_LONG).show();
                }
            });
            Button b1= (Button) view.findViewById(R.id.button1);
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                    installation.put("user",ParseUser.getCurrentUser());
                    installation.saveInBackground();


                    ParseQuery pushQuery = ParseInstallation.getQuery();

// Send push notification to query

                    ParsePush push = new ParsePush();
//                    Location l;
//                    l= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    push.setQuery(pushQuery); // Set our Installation query
                    push.setMessage(t1.getText().toString());
                    push.sendInBackground();
//                    Intent i= new Intent(friend_list.this,MainActivity.class);
//                    i.putExtra("Lati", l.getLatitude()+"");
//                    i.putExtra("Longi", l.getLongitude()+"");
//                    startActivity(i);
                }
            });
            return view;
        }
   }

}
