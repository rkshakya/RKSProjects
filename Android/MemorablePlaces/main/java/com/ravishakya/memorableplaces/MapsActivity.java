package com.ravishakya.memorableplaces;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, LocationListener {
    LocationManager locationmanager;
    String provider;
    private GoogleMap mMap;
    int location = -1;

    @Override
    public void onMapLongClick(LatLng point) {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        String label = new Date().toString();
        try {
            List<Address> lstAddresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
            label = lstAddresses.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        MainActivity.places.add(label);
        MainActivity.arrayAdapter.notifyDataSetChanged();
        MainActivity.points.add(point);

        mMap.addMarker(new MarkerOptions()
                .position(point)
                .title(label)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationmanager.getBestProvider(new Criteria(), false);
//getSupportActionBar().setDisplayHomeAsUpEnabled(true)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent i = getIntent();
        location = i.getIntExtra("locationInfo", -1);
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
        //LatLng sydney = new LatLng(-34, 151);
        if (location != -1 && location !=0 ) {
            locationmanager.removeUpdates(this);
            mMap.addMarker(new MarkerOptions().position(MainActivity.points.get(location)).title(MainActivity.places.get(location)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MainActivity.points.get(location), 10));
        } else{
            locationmanager.requestLocationUpdates(provider, 400, 1, this);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                locationmanager.removeUpdates(this);
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (location == -1 || location ==0 ) {
            locationmanager.requestLocationUpdates(provider, 400, 1, this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationmanager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location userlocation) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(userlocation.getLatitude(),userlocation.getLongitude()), 10));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
