package com.example.settingparse;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {
    String role="user";
    Intent intent;

    public  void admin(View view){
        intent = new Intent(getApplicationContext(),ChooseService.class);
        startActivity(intent);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void started(View view){
        Switch toggle = (Switch) findViewById(R.id.toggle);
        Log.i("switch", String.valueOf(toggle.isChecked()));
        if(toggle.isChecked()){
            role="utility";
        }
        else{
            role = "user";
        }
        if(role.equals("user")){

            ParseAnonymousUtils.logIn(new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e==null){
//                        ParseUser.getCurrentUser().put("role",role);
                        user.put("role",role);
                        user.saveInBackground();
                        Log.i("login","success");
                    }
                    else{
                        Log.i("login","failed");
                    }
                }
            });
            intent = new Intent(getApplicationContext(),UserFindService.class);
            startActivity(intent);
        }
        else{
            Log.i("role",role);
            intent = new Intent(getApplicationContext(),ServiceSignUp.class);
            intent.putExtra("role",role);
            startActivity(intent);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        getActionBar().hide();
        if(ParseUser.getCurrentUser()!=null){
            if(ParseUser.getCurrentUser().getString("role").equals("user")) {
                intent = new Intent(getApplicationContext(), UserFindService.class);
                startActivity(intent);
            }else{
                intent = new Intent(getApplicationContext(), ServiceSignUp.class);
                startActivity(intent);
            }
        }

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }


}