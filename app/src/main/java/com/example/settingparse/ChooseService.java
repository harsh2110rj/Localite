package com.example.settingparse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ChooseService extends AppCompatActivity {
    Intent intent;
    public void electrician(View view){
        intent = new Intent(getApplicationContext(),MarkingService.class);
        intent.putExtra("service","electrician");
        startActivity(intent);
    }

    public void plumber(View view){
        intent = new Intent(getApplicationContext(),MarkingService.class);
        intent.putExtra("service","plumber");
        startActivity(intent);
    }

    public void carpenter(View view){
        intent = new Intent(getApplicationContext(),MarkingService.class);
        intent.putExtra("service","carpenter");
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_service);
    }
}