package com.example.vipul.splash;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class Signup extends ActionBarActivity {

    private EditText username;
    private EditText password;
    private EditText passwordAgainView;
    private Button register;
    ImageView loginlogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        username= (EditText) findViewById(R.id.username);
        password= (EditText) findViewById(R.id.password);
        passwordAgainView= (EditText) findViewById(R.id.passwordAgain);
        register= (Button) findViewById(R.id.register);
        loginlogo=(ImageView) findViewById(R.id.login_logo);

        Animation animation= AnimationUtils.loadAnimation(this, R.anim.rotate_login);
        loginlogo.startAnimation(animation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loginlogo.setVisibility(View.VISIBLE);
            }
        }, 2900);

        register.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean validationerror = false;
                StringBuilder validationmsg = new StringBuilder(" ");

                if(username.getText().toString().trim().length() < 1){
                    validationmsg.append("Please enter username ");
                    validationerror=true;
                }
                if(!(password.getText().toString().equals(passwordAgainView.getText().toString()))){
                    validationmsg.append("Entered password don't match");
                    validationerror=true;
                }
                if (password.getText().toString().trim().length() < 1){
                    validationmsg.append("Please enter password");
                    validationerror=true;
                }
                if (passwordAgainView.getText().toString().trim().length() < 1){
                    validationmsg.append("Please re-enter password");
                    validationerror=true;
                }

                if (validationerror){
                    Toast.makeText(Signup.this,validationmsg.toString(),Toast.LENGTH_LONG).show();
                    return;
                }
                final ProgressDialog dlg=new ProgressDialog(Signup.this);
                dlg.setTitle("Please Wait !");
                dlg.setMessage("Signing up. Please Wait !");
                dlg.show();


                ParseUser user= new ParseUser();
                user.setUsername(username.getText().toString());
                user.setPassword(password.getText().toString());

                user.signUpInBackground( new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e!=null){
                            Toast.makeText(Signup.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        } else {
                            Intent i = new Intent(Signup.this,MainActivity.class);
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
        getMenuInflater().inflate(R.menu.menu_signup, menu);
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
