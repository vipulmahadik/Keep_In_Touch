package com.example.vipul.splash;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


public class Login extends ActionBarActivity {


    private Button login;
    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username= (EditText) findViewById(R.id.editText);
        password= (EditText) findViewById(R.id.editText2);

        login=(Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean validationerror = false;
                StringBuilder validationmsg = new StringBuilder("Please ");

                if(username.getText().toString().trim().length() < 1){
                    validationmsg.append("enter username ");
                    validationerror=true;
                }
                if (password.getText().toString().trim().length() < 1){
                    validationmsg.append("enter password");
                    validationerror=true;
                }

                if (validationerror){
                    Toast.makeText(Login.this, validationmsg.toString(), Toast.LENGTH_LONG).show();
                    return;
                }
                final ProgressDialog dlg=new ProgressDialog(Login.this);
                dlg.setTitle("Please Wait !");
                dlg.setMessage("Signing up. Please Wait !");
                dlg.show();

                ParseUser.logInInBackground(username.getText().toString(),password.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if (e!=null){
                            Toast.makeText(Login.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }else {
                            Intent i = new Intent(Login.this,friend_list.class);
                            i.addFlags(i.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        }
                    }
                });

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
