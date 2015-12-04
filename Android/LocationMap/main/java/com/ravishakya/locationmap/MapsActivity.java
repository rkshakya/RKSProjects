package com.ravishakya.locationmap;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;

    LocationManager locationManager;
    String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

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

        Location location = locationManager.getLastKnownLocation(provider);
        if(location != null){
            // Add a marker in Sydney and move the camera
            Double lat = location.getLatitude();
            Double lng = location.getLongitude();

            Log.i("Latitude: ", lat.toString());

            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Initial location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lng)));
        }


    }

    @Override
    public void onLocationChanged(Location location) {
        Double lat = location.getLatitude();
        Double lng = location.getLongitude();

        Log.i("Latitude: ", lat.toString());

        mMap.clear();

        mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Current Location"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 18));

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        List<Address> listAddresses = null;
        try {
            listAddresses = geocoder.getFromLocation(lat, lng, 1);

            if(listAddresses != null && listAddresses.size() > 0){
                Log.i("PlaceInfo", listAddresses.get(0).toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    @Override
    protected void onPause() {
        super.onPause();

        locationManager.removeUpdates(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }
}
