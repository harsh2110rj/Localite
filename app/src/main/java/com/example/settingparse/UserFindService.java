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
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class UserFindService extends AppCompatActivity {

    Intent intent;
    boolean exit=false;

    public void electrician(View view){
        intent = new Intent(getApplicationContext(),UserActivity.class);
        intent.putExtra("service","electrician");
        startActivity(intent);
    }

    public void plumber(View view){
        intent = new Intent(getApplicationContext(),UserActivity.class);
        intent.putExtra("service","plumber");
        startActivity(intent);
    }

    public void carpenter(View view){
        intent = new Intent(getApplicationContext(),UserActivity.class);
        intent.putExtra("service","carpenter");
        startActivity(intent);
    }

    public void deletingMessages(String role){
        ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("Messages");
        query1.whereEqualTo(role,ParseUser.getCurrentUser().getUsername());
        query1.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    for (ParseObject object:objects){
                        object.deleteInBackground();
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.user_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.logout){
            ParseUser user = ParseUser.getCurrentUser();
            try {
                user.delete();
                startActivity(new Intent(UserFindService.this,MainActivity.class));
            } catch (ParseException e) {
                e.printStackTrace();
            }

//            user.deleteInBackground(new DeleteCallback() {
//                @Override
//                public void done(ParseException e) {
//                    if(e==null)
//                        Log.i("delete","success");
//                    else {
//                        Log.i("delete", "failed");
//                        System.out.println(e);
//                    }
//                }
//            });

            deletingMessages("sender");
            deletingMessages("recipient");
            ParseUser.logOut();
            finish();
        }
        return true;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_find_service);
        setTitle("Find Service");
    }
}