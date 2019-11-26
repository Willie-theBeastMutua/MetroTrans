package com.example.metrotrans;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import com.example.metrotrans.directionhelpers.FetchURL;
import com.example.metrotrans.directionhelpers.TaskLoadedCallback;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
// import com.thecodecity.mapsdirection.directionhelpers.TaskLoadedCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class routeMap extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMapLoadedCallback, TaskLoadedCallback {


    private GoogleMap mMap;
    GoogleApiClient mApiClient;
    Location lastLocation;
    LocationRequest mlocationrequest;
    Marker marker;
    public String destLat;
    public String destLong;
    public String destname;
    private Polyline currentPolyline;
    private MarkerOptions place1, place2;
    private TextToSpeech mTTS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent i = getIntent();
        if (i != null) {
            destLat = i.getStringExtra("latitude");
            destLong = i.getStringExtra("longitude");
            destname = i.getStringExtra("Name");
            Log.w("netlat", destLat);
            Log.w("netlong", destLong);
            // new FetchURL(routeMap.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");

        }
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

    protected synchronized void buildGoogleApiClient() {
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

        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));


        // new FetchURL(routeMap.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");
        place1 = new MarkerOptions().position(latlng).title("Location 1");
        mMap.addMarker(new MarkerOptions().position(latlng));
        Double lat1 = Double.parseDouble(destLat);
        Double long1 = Double.parseDouble(destLong);
        LatLng latlng2 = new LatLng(lat1, long1);
        place2 = new MarkerOptions().position(new LatLng(lat1, long1)).title("Location 2");
        mMap.addMarker(place2);
        Location loc1 = new Location("");
        loc1.setLatitude(location.getLatitude());
        loc1.setLongitude(location.getLongitude());
        Location loc2 = new Location("");
        loc2.setLatitude(latlng2.latitude);
        loc2.setLongitude(latlng2.longitude);
        float distance = loc1.distanceTo(loc2);

        String dist = Float.toString(distance);
        new FetchURL(routeMap.this).execute(getUrl(latlng, latlng2, "driving"), "driving");
        Log.w("distanceyake", dist);


        if (distance < 500) {
            mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        int result = mTTS.setLanguage(Locale.ENGLISH);
                        if (result == TextToSpeech.LANG_MISSING_DATA
                                || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                            Log.e("TTS", "Language not supported");
                        } else {
                            mTTS.speak("you have arrived at your destination", TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                }


            });
        }

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

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onMapLoaded() {

    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }


    //getDriversAround();


    boolean getDriversAroundstarted = false;
    List<Marker> markerList = new ArrayList<Marker>();

    private void getDriver() {
        getDriversAroundstarted = true;

        DatabaseReference driverloc = FirebaseDatabase.getInstance().getReference("Driver locations");

        GeoFire geodrive = new GeoFire(driverloc);
        GeoQuery drivlocquery = geodrive.queryAtLocation(new GeoLocation(lastLocation.getLatitude(), lastLocation.getLongitude()), 10);

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
                // float distance = loc1.distanceTo(loc2);
                float time = loc1.getTime();
                String times = Float.toString(time);
                int dist = Math.round(distance);
                Log.w("timessssss", times);
                String distances = Float.toString(distance);
                Log.w("the distance", distances);
                int eta = (dist / 5);
                driversmarkers.setTitle(distance + "m");
                markerList.add(driversmarkers);
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }

}

