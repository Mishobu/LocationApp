package com.mishobu.locationapp;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements LocationListener {

    LocationManager locationManager;
    TextView latitudeValue, longitudeValue, providerValue, accuracyValue;
    TextView timeToFixValue, enabledProviderValue;
    long uptimeAtResume;
    List<String> enabledProviders;
    String lat, lon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        latitudeValue = (TextView) findViewById(R.id.latitudValue);
        longitudeValue = (TextView) findViewById(R.id.longitudeValue);
        providerValue = (TextView) findViewById(R.id.providerValue);
        accuracyValue = (TextView) findViewById(R.id.accuracyValue);
        timeToFixValue = (TextView) findViewById(R.id.timeToFixValue);
        enabledProviderValue  = (TextView) findViewById(R.id.enabledProvidersValue);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onLocationChanged(Location location) {
        lat = String.valueOf(location.getLatitude());
        lon = String.valueOf(location.getLongitude());

        latitudeValue.setText(lat);
        longitudeValue.setText(lon);
        providerValue.setText(String.valueOf(location.getProvider()));
        accuracyValue.setText(String.valueOf(location.getAccuracy()));

        long timeToFix = SystemClock.uptimeMillis() - uptimeAtResume;

        timeToFixValue.setText(String.valueOf(timeToFix / 1000));

        findViewById(R.id.timeToFixUnits).setVisibility(View.VISIBLE);
        findViewById(R.id.accuracyUnits).setVisibility(View.VISIBLE);



    }



    @Override
    protected void onResume() {
        super.onResume();
        StringBuffer stringBuffer = new StringBuffer();
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        enabledProviders = locationManager.getProviders(criteria, true);

        if(enabledProviders.isEmpty())
            enabledProviderValue.setText("");
        else
        {
            for(String enabledProvider : enabledProviders)
            {
                stringBuffer.append(enabledProvider).append(" ");
                try {
                    locationManager.requestSingleUpdate(enabledProvider, this, null);
                } catch (SecurityException e) { e.printStackTrace(); }
            }
            enabledProviderValue.setText(stringBuffer);
        }
        uptimeAtResume = SystemClock.uptimeMillis();

        latitudeValue.setText("");
        longitudeValue.setText("");
        providerValue.setText("");
        accuracyValue.setText("");
        timeToFixValue.setText("");

        findViewById(R.id.timeToFixUnits).setVisibility(View.GONE);
        findViewById(R.id.accuracyUnits).setVisibility(View.GONE);
    }

    public void onChangeLocationProvidersSettingsClick(View view)
    {
        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    public void showOnMapClick(View view)
    {
        Uri uri = Uri.parse("geo:" + lat + "," + lon);

        Toast.makeText(getApplicationContext(), "geo:" + lat +"," + lon, Toast.LENGTH_LONG).show();

        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    @Override
    protected void onPause() {

        super.onPause();
        try {
            locationManager.removeUpdates(this);
        } catch (SecurityException e) { e.printStackTrace(); }
    }

    public void onProviderDisabled(String provider) {
        // En blanco. Se escribe para cubrir los requisitos de la implementación

    }

    public void onProviderEnabled(String provider) {
        // En blanco. Se escribe para cubrir los requisitos de la implementación

    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
        // En blanco. Se escribe para cubrir los requisitos de la implementación

    }
}
