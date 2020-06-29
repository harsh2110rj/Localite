package com.example.settingparse;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MarkingService extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    Intent intent;
    private static final String TAG = "MarkingService";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marking_service);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        intent = getIntent();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
        // Add a marker in Sydney and move the camera
//        LocationManager locationManager=new LocationManager(LocationManager.GPS_PROVIDER,0,0,LocationManager.)
        LatLng sydney = new LatLng(25.5787, 83.9892);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Admin Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,14));
    }


    @Override
    public void onMapLongClick(final LatLng latLng) {
        Log.i("clicked","true");
        final ParseObject request = new ParseObject(intent.getStringExtra("service"));

//        request.put("username", ParseUser.getCurrentUser().getUsername());

        ParseGeoPoint parseGeoPoint = new ParseGeoPoint(latLng.latitude, latLng.longitude);

        request.put("location", parseGeoPoint);

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        ArrayList<Address> address = null;
        String result="";
        try {
            address = (ArrayList<Address>) geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
            Log.i("address", String.valueOf(address.get(0)));
            if(address != null && address.size()>0){
                if(address.get(0).getAddressLine(0)!=null){

                    result+=address.get(0).getAddressLine(0);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        if(result.equals("")){
            result+=address.get(0).getSubAdminArea();
            if(address.get(0).getLocality()!=null){
                result+=" "+address.get(0).getLocality();
            }
        }



        final EditText editText = new EditText(this);
        final String finalResult = result;
        final String finalResult1 = result;
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_btn_speak_now)
                .setTitle("Username")
                .setMessage("Please enter the username of the Service Provider")
                .setView(editText)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(MarkingService.this, "Hindi", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onClick: editText.getText().length() is: "+editText.getText().toString().length());
                        if(editText.getText().toString().length()>0) {
                            request.put("username", editText.getText().toString());

                            request.put("address", finalResult1);
                            request.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(e==null){
                                        Log.i("address","added");
                                    }
                                }
                            });
                            request.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {

                                    if (e == null) {
                                        Log.d("location"," location has been addded");
                                    }
                                    else{
                                        Log.d("location","-------------failed---------------");
                                    }

                                }
                            });

                            mMap.addMarker(new MarkerOptions().position(latLng).title(finalResult));
                            Log.d(TAG, "onClick: saveinBackground called");
                        }
                    }
                })
                .show().setCanceledOnTouchOutside(false);
        

    }
}