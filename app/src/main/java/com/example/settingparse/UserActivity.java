package com.example.settingparse;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    Intent intent;
    static String className;
    LatLng locationFrom;
    double latitude_of_user=0,longitude_of_user=0;
public static final String TAG="UserActivity";
    static ArrayList<String> utilityAddress = new ArrayList<String>();

    public void chat(View view){

        try {
            Log.d(TAG, "chat: onClick starts");
            Intent intent1 = new Intent(getApplicationContext(), ServiceProviders.class);
//            intent1.putExtra("username",);
            startActivity(intent1);

            Log.d("chat", "open the chat window");
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if(lastKnownLocation!=null) Log.d(TAG, "onMapReady: lastKnownLocation is: "+lastKnownLocation.getLatitude()+"  <<<----->>>  "+lastKnownLocation.getLongitude());
                    updateMap(lastKnownLocation);
                    onMapReady(mMap);

//                    if(lastKnownLocation==null){
//                        lastKnownLocation.setLatitude(25.5787);
//                        lastKnownLocation.setLongitude(83.9892);
//                    }

//                    if (lastKnownLocation != null) {
//
//                        updateMap(lastKnownLocation);
//
//                    }

                }

            }


        }

    }

    public void updateMap(Location location) {
        LatLng userLocation;
        if(location==null){
         userLocation= new LatLng(28.6139,77.2090);
         Toast.makeText(this,"The GPS is off.So, the default location is set to New Delhi, India.Please turn the location on!",Toast.LENGTH_LONG).show();
        }
        else
        {
            userLocation=new LatLng(location.getLatitude(),location.getLongitude());
        }
        latitude_of_user=userLocation.latitude;longitude_of_user=userLocation.longitude;

//        locationFrom = userLocation;
        mMap.clear();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)).position(userLocation).title("Your Location"));

    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        intent = getIntent();
        className = intent.getStringExtra("service");
        utilityAddress.clear();

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

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(className);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    Log.d(TAG, "done: sizeof ParseObjects list is: "+objects.size());
                    for(ParseObject object:objects){
//                        Log.i("locate", String.valueOf(object.getParseGeoPoint("location")));
                        ParseGeoPoint parseGeoPoint = object.getParseGeoPoint("location");
                        LatLng location = new LatLng(parseGeoPoint.getLatitude(),parseGeoPoint.getLongitude());

                        locationFrom = new LatLng(latitude_of_user,longitude_of_user);
//                        Log.d(TAG, "done: ");
                        Log.d(TAG, "done: latitude and longitude of user is: "+latitude_of_user+"  <<< ------- >>>  "+longitude_of_user);
                        double distance = SphericalUtil.computeDistanceBetween(location,locationFrom);
                        distance/=1000;
                        if(distance<=5) {
                            Log.d("distance", String.valueOf(distance));
                            utilityAddress.add(object.getString("address"));
//                            if(object!=null)Log.d("address of the distance",object.getString("address"));
                            mMap.addMarker(new MarkerOptions().position(location).title(className));
                        }


                    }
                    Log.d(TAG, "done: size of utilityAddress is: "+UserActivity.utilityAddress.size());
                }
            }
        });



        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

//                updateMap(location);

            latitude_of_user=location.getLatitude();
            longitude_of_user=location.getLongitude();


            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);


        } else {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                updateMap(lastKnownLocation);

        }



        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(UserActivity.this,UserFindService.class));
    }
}