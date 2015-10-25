package com.example.vipul.splash;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;


public class addmem extends ActionBarActivity {

    Button addmembers;
    ListView li;
    ArrayList<ParseUser> ulist;
    ArrayList<String> users,finaluser;
    UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmem);
        li = (ListView) findViewById(R.id.li);
        users= new ArrayList<String>();
        finaluser= new ArrayList<String>();
        addmembers=(Button)findViewById(R.id.buttonadd);
        addmembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b=new Bundle();
                b.putStringArrayList("user",finaluser);
                Intent i = new Intent(addmem.this,EventPage.class);
                i.putExtras(b);
                startActivity(i);
            }
        });
        ParseUser.getQuery().whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername()).findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                if (parseUsers != null) {
                    if (parseUsers.size() == 0)
                        Toast.makeText(addmem.this, "No User Found", Toast.LENGTH_LONG).show();
                    ulist = new ArrayList<>(parseUsers);
                    li.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
                    adapter=new UserAdapter();
                    li.setAdapter(adapter);
                    li.setMultiChoiceModeListener(new ModeCallback());

                } else {
                    Toast.makeText(addmem.this, "Errorrrr", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_addmem, menu);
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

    public class UserAdapter extends BaseAdapter {
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

            TextView label = (TextView) view.findViewById(R.id.label);
            label.setText(c.getUsername());
            users.add(c.getUsername());
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

            return true;
        }

        public void onDestroyActionMode(ActionMode mode) {
        }

        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            final int checkedCount = li.getCheckedItemCount();
            switch (checkedCount) {
                case 0:
                    addmembers.setText(null);
                    break;
                case 1:
                    addmembers.setText("One item selected");
                    break;
                default:
                    addmembers.setText("" + checkedCount + " items selected");
                    break;
            }
            finaluser.add(users.get(position));
        }

    }
}
