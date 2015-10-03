package com.example.vipul.splash;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class friend_list extends ActionBarActivity {


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
            label.setText(c.getUsername());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(friend_list.this,c.getUsername(),Toast.LENGTH_LONG).show();
                }
            });
            return view;
        }
    }

}
