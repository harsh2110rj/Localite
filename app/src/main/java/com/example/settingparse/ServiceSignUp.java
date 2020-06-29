package com.example.settingparse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class ServiceSignUp extends AppCompatActivity implements View.OnClickListener {
    boolean isActive=true;
    boolean exit=false;
    EditText name;
    EditText address;
    EditText contact;
    EditText username;
    EditText password;
    EditText loginUsername;
    EditText loginPassword;
    LinearLayout signupLinearLayout;
    LinearLayout loginLinearLayout;
    RelativeLayout relativeLayout;
    Intent intent;
    String role;



    public void login(View view){
        if(isActive){
            ParseUser utility = new ParseUser();
            ParseUser.logInInBackground(loginUsername.getText().toString(), loginPassword.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if(e==null){
                        Toast.makeText(ServiceSignUp.this,"logged in successfully",Toast.LENGTH_SHORT).show();
                        loginPassword.setText(null);
                        loginUsername.setText(null);
                        intent = new Intent(getApplicationContext(),ServiceChatWindow.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(ServiceSignUp.this,"enter valid credentials",Toast.LENGTH_SHORT).show();
                        loginUsername.setText(null);
                        loginPassword.setText(null);
                    }
                }
            });
        }
        else{
            ParseUser utility = new ParseUser();

//            utility.put("username",username.getText().toString());
//            utility.put("password",password.getText().toString());
            utility.put("name",name.getText().toString());
            utility.put("address",address.getText().toString());
            utility.put("contact",contact.getText().toString());
            utility.put("role",role);
            utility.setUsername(username.getText().toString());
            utility.setPassword(password.getText().toString());
            utility.saveEventually();
            utility.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){
                        Log.i("signup ","success");
                        ParseUser.getCurrentUser().put("name",name.getText().toString());
                        ParseUser.getCurrentUser().put("address",address.getText().toString());
                        ParseUser.getCurrentUser().put("contact",contact.getText().toString());
                        ParseUser.getCurrentUser().put("role",role);
                    }
                }
            });

            Button b = (Button) findViewById(R.id.login);
            TextView orSignup = (TextView) findViewById(R.id.orSignup);
            isActive=true;
            b.setText("Login");
            orSignup.setText("or,SignUp");
            loginLinearLayout.setVisibility(View.VISIBLE);
            signupLinearLayout.setVisibility(View.INVISIBLE);

        }
    }
    public void toggle(View view){
        Button b = (Button) findViewById(R.id.login);
        TextView orSignup = (TextView) findViewById(R.id.orSignup);
        if(isActive){
            isActive=false;
            b.setText("Sign Up");
            orSignup.setText("or,Login");
            loginLinearLayout.setVisibility(View.INVISIBLE);
            signupLinearLayout.setVisibility(View.VISIBLE);
        }
        else{
            isActive=true;
            b.setText("Login");
            orSignup.setText("or,SignUp");
            loginLinearLayout.setVisibility(View.VISIBLE);
            signupLinearLayout.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed()
    {
        if(ParseUser.getCurrentUser()!=null) {
            if (exit) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Press Back again to Exit.",
                        Toast.LENGTH_SHORT).show();
                exit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 2 * 1000);
            }
        }
        else{
            super.onBackPressed();
        }
    }


    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.relativeLayout){
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_sign_up);
        name = (EditText) findViewById(R.id.name);
        address = (EditText) findViewById(R.id.address);
        contact = (EditText) findViewById(R.id.contact);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        loginUsername = (EditText) findViewById(R.id.loginusername);
        loginPassword = (EditText) findViewById(R.id.loginpassword);
        signupLinearLayout = (LinearLayout) findViewById(R.id.signupLinearLayout);
        loginLinearLayout = (LinearLayout) findViewById(R.id.loginLinearLayout);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        relativeLayout.setOnClickListener((View.OnClickListener) this);
        intent = getIntent();
        role = intent.getStringExtra("role");
        if(ParseUser.getCurrentUser()!=null){
            intent = new Intent(getApplicationContext(),ServiceChatWindow.class);
            startActivity(intent);
        }
    }
}