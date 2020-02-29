package com.example.aultra.greenwaveproject;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    public static final String MY_SHARED_PREF = "greenwave";
    public static final String NAME_FLAG = "username";
    public static final String PASS_FLAG = "password";
    EditText user;
    EditText pass;
    TextView login;
    TextView signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences readPref = this.getSharedPreferences(MY_SHARED_PREF, MODE_PRIVATE);
        String usr = readPref.getString(LoginActivity.NAME_FLAG, null);
        String pas = readPref.getString(LoginActivity.PASS_FLAG, null);

        if(usr != null || pas != null)startActivity(new Intent(LoginActivity.this,AlbumActivity.class).setAction(Intent.ACTION_VIEW));
        else{
            user = (EditText) findViewById(R.id.et_username);
            pass = (EditText) findViewById(R.id.et_password);
            login = (TextView) findViewById(R.id.btn_login);
            signup = (TextView) findViewById(R.id.tv_signup);

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String username = user.getText().toString();
                    String password = pass.getText().toString();

                    if (!(username == null || username.isEmpty() || password == null || password.isEmpty())) {
                        //save the values in shared preferences
                        SharedPreferences myPref = getBaseContext().getSharedPreferences(MY_SHARED_PREF, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = myPref.edit();
                        editor.putString(NAME_FLAG, username);
                        editor.putString(PASS_FLAG, password);
                        editor.commit();

                        //if //check the email and password
                        startActivity(new Intent(LoginActivity.this, AlbumActivity.class));
                    }
                }
            });

            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                }
            });

        }
    }

}
