package com.voteandeat.voteandeat.GoogleAPI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.SearchEvent;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.voteandeat.voteandeat.GoogleAPI.Model.MyPlaces;
import com.voteandeat.voteandeat.GoogleAPI.Model.Results;
import com.voteandeat.voteandeat.GoogleAPI.Remote.IGoogleAPIService;
import com.voteandeat.voteandeat.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private static final int MY_PERMISSION_CODE = 1000;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;

    private double latitude, longitude;
    private Location mLastLocation;
    private Marker mMarker;
    private LocationRequest mLocationRequest;

    IGoogleAPIService mService;

    MyPlaces currentPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Init Service
        mService = Common.getGoogleAPIService();

        //Request Runtime Permission
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_restaurant_maps:
                        nearByPlace("restaurant");
                        break;
                    case R.id.action_takeaway_maps:
                        nearByPlace("meal_takeaway");
                        break;
                    case R.id.action_cafe_maps:
                        nearByPlace("cafe");
                        break;
                    case R.id.action_bar_maps:
                        nearByPlace("bar");
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void nearByPlace(final String placeType) {
        if(mMap != null) {
            mMap.clear();
        }
        String url = getUrl(latitude, longitude, placeType);

        mService.getNearByPlaces(url)
                .enqueue(new Callback<MyPlaces>() {
                    @Override
                    public void onResponse(Call<MyPlaces> call, Response<MyPlaces> response) {

                        currentPlace = response.body(); //Remember assign value for currentPlace

                            if(response.isSuccessful()){
                                for(int i=0;i<response.body().getResults().length;i++){
                                    MarkerOptions markerOptions = new MarkerOptions();
                                    Results googlePlace = response.body().getResults()[i];
                                    double lat = Double.parseDouble(googlePlace.getGeometry().getLocation().getLat());
                                    double lng = Double.parseDouble(googlePlace.getGeometry().getLocation().getLng());
                                    String placeName = googlePlace.getName();
                                    String vicinity = googlePlace.getVicinity();
                                    LatLng latLng = new LatLng(lat, lng);
                                    markerOptions.position(latLng);
                                    markerOptions.title(placeName);
                                    if(placeType.equals("restaurant")){
                                        //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_local_hospital_black_24dp));
                                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_restaurant));
                                    }else if(placeType.equals("meal_takeaway")){
                                       //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_shopping_cart_black_24dp));
                                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_takeaway));
                                    }else if(placeType.equals("cafe")){
                                        //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_restaurant_black_24dp));
                                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_cafe));
                                    }else if(placeType.equals("bar")){
                                        //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_school_black_24dp));
                                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_bar));
                                    }else{
                                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                                    }

                                    markerOptions.snippet(String.valueOf(i)); //Assign index for marker

                                    //Add to map
                                    mMap.addMarker(markerOptions);

                                    //Move camera
                                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                                }
                            }
                    }

                    @Override
                    public void onFailure(Call<MyPlaces> call, Throwable t) {

                    }
                });


    }

    private String getUrl(double latitude, double longitude, String placeType) {
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location="+latitude+","+longitude);
        googlePlacesUrl.append("&radius="+1000);
        googlePlacesUrl.append("&type="+placeType);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key="+getResources().getString(R.string.browser_key));
        Log.d("getUrl",googlePlacesUrl.toString());
        return googlePlacesUrl.toString();
    }

    //TODO SI FALLA REVISAR AQUEST METODE, URL: https://youtu.be/wKrYU97Wwg4?t=1908
    private boolean checkLocationPermission() {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, MY_PERMISSION_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, MY_PERMISSION_CODE);
                return false;
            }
        }else{
            return true;
        }
        return false;
    }

    //Ctrl + O

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_PERMISSION_CODE:
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    if(mGoogleApiClient == null){
                        buildGoogleApiClient();
                        mMap.setMyLocationEnabled(true);
                    }
                }
            }else {
                Toast.makeText(this,"Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onSearchRequested(@Nullable SearchEvent searchEvent) {
        return super.onSearchRequested(searchEvent);
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

//        //Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        //Init Google Play Services
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
        //Make event click on Marker
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //When user select marker, just get Result of Place and assign to static variable
                Common.currentResult = currentPlace.getResults()[Integer.parseInt(marker.getSnippet())];
                //Start a new activity
                startActivity(new Intent(MapsActivity.this,ViewPlace.class));
                return true;
            }
        });
    }

    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if(mMarker != null){
            mMarker.remove();
        }

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        LatLng latLng = new LatLng(latitude,longitude);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title("Your position")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mMarker = mMap.addMarker(markerOptions);

        //Move Camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        if (mGoogleApiClient != null){
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
        //DEFAULT
        nearByPlace("restaurant");
    }
}
