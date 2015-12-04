package com.ravishakya.hikerswatch;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {
    LocationManager locationManager;
    String provider;

    TextView txtLat;
    TextView txtLng;
    TextView txtAlt;
    TextView txtBear;
    TextView txtAccu;
    TextView txtSpeed;
    TextView txtAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtLat = (TextView) findViewById(R.id.txtLatitude);
        txtLng = (TextView) findViewById(R.id.txtLongitude);
        txtAlt = (TextView) findViewById(R.id.txtAltitude);
        txtBear = (TextView)findViewById(R.id.txtBearing);
        txtAccu = (TextView)findViewById(R.id.txtAccuracy);
        txtSpeed = (TextView)findViewById(R.id.txtSpeed);
        txtAddress = (TextView)findViewById(R.id.txtAddress);

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        Location loc = locationManager.getLastKnownLocation(provider);
        onLocationChanged(loc);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();

        locationManager.removeUpdates(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        locationManager.requestLocationUpdates(provider, 600, 1, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Double lat = location.getLatitude();
        Double lng = location.getLongitude();

        Double alt = location.getAltitude();
        float bearing = location.getBearing();
        float speed = location.getSpeed();
        float accuracy = location.getAccuracy();

        Geocoder geoCoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geoCoder.getFromLocation(lat,lng,1);

            if(addresses != null && addresses.size() > 0){
                Log.i("PlaceInfo : " , addresses.toString());
            }

            String addressHolder = "";

            for (int i= 0; i <= addresses.get(0).getMaxAddressLineIndex(); i++){
                addressHolder += addresses.get(0).getAddressLine(i) + "\n";
            }

            txtAddress.setText("Address:\n" + addressHolder);

        } catch (IOException e) {
            e.printStackTrace();
        }

        txtLat.setText("Latitude : " + lat.toString());
        txtLng.setText("Longitude : " + lng.toString());

        txtAlt.setText("Altitude: " + alt.toString() + "m");
        txtBear.setText("Bearing: " + String.valueOf(bearing));
        txtSpeed.setText("Speed: " + String.valueOf(speed) + "m/s");
        txtAccu.setText("Accuracy: " + String.valueOf(accuracy) + "m");



        Log.i("Latitude: ", lat.toString());
        Log.i("Longitude: ", lng.toString());
        Log.i("Altitude: ", alt.toString() + "m");
        Log.i("Bearing: ", String.valueOf(bearing));
        Log.i("Speed: ", String.valueOf(speed)+ "m/s");
        Log.i("Accuracy: ", String.valueOf(accuracy)+ "m");

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
