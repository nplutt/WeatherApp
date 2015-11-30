package com.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.location.*;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements OnButtonPressListener, LocationListener{

    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1, this);
        if(getLocation() != null){
            sendGpsCoordinates(getLocation().getLatitude(),getLocation().getLongitude());
        }
        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.location,new LocationInputFragment());
            ft.add(R.id.conditions,new CurrentConditionsFragment());
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
            getSupportFragmentManager().executePendingTransactions();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
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
    //Receives the message from locationInputFragment and send it to CurrentConditionsFragment
    public void onButtonPressed(String msg){
        //Gets the current conditions fragment
        CurrentConditionsFragment Obj = (CurrentConditionsFragment) getSupportFragmentManager().findFragmentById(R.id.conditions);
        //Calls the setMessage method from the currentConditionsFragment
        Obj.setMessage(msg);
    }

    //Sends the GPS coordinates to the CurrentConditionsFragment
    public void sendGpsCoordinates(Double lat, Double lon){
        CurrentConditionsFragment Obj = (CurrentConditionsFragment) getSupportFragmentManager().findFragmentById(R.id.conditions);
        Obj.setGpsLocation(lat.toString(), lon.toString());
    }

    @Override
    public void onLocationChanged(Location location) {
//        Double lat = location.getLatitude();
//        Double lon = location.getLongitude();
//        CurrentConditionsFragment Obj = (CurrentConditionsFragment) getSupportFragmentManager().findFragmentById(R.id.conditions);
//        Obj.setGpsLocation(lat.toString(), lon.toString());
    }

    @Override
    public void onProviderDisabled(String provider) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        Toast.makeText(getBaseContext(), "Gps is turned off!! ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(getBaseContext(), "Gps is turned on!! ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    public Location getLocation(){
        Location location = null;
        try{
            //Sets the criteria
            Criteria criteria = new Criteria();
            //Gets the best provider
            String provider = locationManager.getBestProvider(criteria, true);
            //Gets the lest known location
            location = locationManager.getLastKnownLocation(provider);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            //Returns the location
            return location;
        }
    }

}
