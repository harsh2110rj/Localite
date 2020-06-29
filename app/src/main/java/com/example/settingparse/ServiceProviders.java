package com.example.settingparse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class ServiceProviders extends AppCompatActivity {
//    ArrayList<String> utilityAddress = new ArrayList<String>();
    ArrayAdapter arrayAdapter;
    public static final String TAG="ServiceProviders";
    Button btnStartPayment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_providers);
        Log.d(TAG, "onCreate: starts ");
        ListView listView = (ListView) findViewById(R.id.listView);
        Log.d(TAG, "onCreate: yaha tak chala");
        btnStartPayment=(Button)findViewById(R.id.btnPayMoney);
        Log.d(TAG, "onCreate: UserActivity.utilityAddress.size() : "+UserActivity.utilityAddress.size());
        ArrayList<String> allUtilsNearby=new ArrayList<>() ;
        for(int i=0;i<UserActivity.utilityAddress.size();i++)
        {
            if(UserActivity.utilityAddress.get(i)!=null){allUtilsNearby.add(UserActivity.utilityAddress.get(i));}
        }
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,allUtilsNearby);

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//             Log.i(TAG, "onCreate: yaha tak chala")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                final String[] usernameToPass = {""};
                final Intent intent = new Intent(getApplicationContext(),ChatWindow.class);
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(UserActivity.className);
                Log.d(TAG, "onItemClick: query.size() is:  "+query);
                query.whereEqualTo("address",UserActivity.utilityAddress.get(i));
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if(e==null){
                            for(ParseObject object:objects){
//                                Log.i("users ",object.getString("username"));

//                                Log.i("username to pass",usernameToPass[0]);
                                intent.putExtra("username",object.getString("username"));
                                Log.d(TAG, "username of the utilityprovider is "+object.getString("username"));

                                startActivity(intent);
//                                usernameToPass[0] = object.getString("username");
                            }
                        }
                    }
                });


            }
        });

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        startActivity(new Intent(ServiceProviders.this,UserFindService.class));
    }
    public void startPaymentActivity(View v)
    {
        Log.d(TAG, "startPaymentActivity: called");
        startActivity(new Intent(ServiceProviders.this,UpiPaymentActivity.class));
    }
}