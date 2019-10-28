package com.example.metrotrans;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import androidx.annotation.Nullable;


import android.Manifest;

import android.content.pm.PackageManager;

import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.LocationListener;



import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class schedulemap extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener  {

    private GoogleMap mMap;
    public String id;
    public  String seatsA;
       GoogleApiClient mApiClient;
    Location lastLocation;
    LocationRequest mlocationrequest;
    Button distancebut;
    Button seats;
    Button book;
    public String Regno;
        Marker marker;
    Location Driverloc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedulemap);
        Intent intent = getIntent();
        if(intent != null){
           id = intent.getStringExtra("uid");
           seatsA = intent.getStringExtra("seats");
           Regno = intent.getStringExtra("Reg");




        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        distancebut = findViewById(R.id.button4);
        seats = findViewById(R.id.button5);
        seats.setText("Seats Available are : " + seatsA);
        book = findViewById(R.id.button6);
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(schedulemap.this, BookSeats.class));
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
    }
    protected synchronized void   buildGoogleApiClient(){
        mApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mApiClient.connect();


    }
    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.clear();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));

        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));

      getDriver();

    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mlocationrequest = new LocationRequest();
        mlocationrequest.setInterval(10000);
        mlocationrequest.setFastestInterval(10000);
        mlocationrequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mApiClient, mlocationrequest, (com.google.android.gms.location.LocationListener) this);



    }
    boolean getDriversAroundstarted= false;
    List<Marker> markerList= new ArrayList<Marker>();
    private  void getDriver(){
        getDriversAroundstarted = true;

        DatabaseReference driverloc = FirebaseDatabase.getInstance().getReference("Driver locations");

        GeoFire geodrive= new GeoFire(driverloc);
        GeoQuery drivlocquery = geodrive.queryAtLocation(new GeoLocation(lastLocation.getLatitude(),lastLocation.getLongitude()), 10);

        drivlocquery.addGeoQueryEventListener(new GeoQueryEventListener() {

            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                LatLng driverlocation = new LatLng(location.latitude, location.longitude);
                Marker driversmarkers = mMap.addMarker(new MarkerOptions().position(driverlocation).
                        icon(BitmapDescriptorFactory.fromResource(R.mipmap.drivermarker)));
                Location loc1 = new Location("");
                loc1.setLatitude(lastLocation.getLatitude());
                loc1.setLongitude(lastLocation.getLongitude());
                Location loc2 = new Location("");
                loc2.setLatitude(location.latitude);
                loc2.setLongitude(location.longitude);
                float distance = loc1.distanceTo(loc2);
                float time = loc1.getTime();
                String times = Float.toString(time);
                int dist = Math.round(distance);
                Log.w("timessssss", times);
                String distances = Float.toString(distance);
                Log.w("the distance",distances);
                int  eta = (dist/5);
                distancebut.setText("Bus " + Regno + " is " +dist + " M Away : ETA "+eta + "s" );
                driversmarkers.setTitle(distance+"m");
                markerList.add(driversmarkers);
            }

            @Override
            public void onKeyExited(String key) {
                for (Marker markerit : markerList){
                    if(Objects.equals(markerit.getTag(), key));
                    markerit.remove();
                    markerList.remove(markerit);
                    return;
                }
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                for (Marker markerit : markerList){
                    if(Objects.equals(markerit.getTag(), key));
                    markerit.setPosition(new LatLng(location.latitude,location.longitude));

                }
            }

        @Override
        public void onGeoQueryReady() {

        }

        @Override
        public void onGeoQueryError(DatabaseError error) {

        }
    });

}
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
