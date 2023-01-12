package edu.uiuc.cs427app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

public class MapPage extends AppCompatActivity implements OnMapReadyCallback {

    String locationLatitude;
    String locationLongitude;
    static String locationName;

    /**
     * This function gets the user's chosen city, latitude, longitude from the MainActivity for setting the map
     * A TextView displays the aforementioned values
     * The database is hosted on Firebase Realtime Database.
     * @param savedInstanceState default input parameter for onCreate method
     * @return No return needed for onCreate method
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationName = getIntent().getStringExtra("city");
        locationLatitude = getIntent().getStringExtra("latitude");
        locationLongitude = getIntent().getStringExtra("longitude");
        // Set the layout file as the content view.
        setContentView(R.layout.activity_map);

        // Setting database object and recyclerView.
        // RecyclerView is similar to a dynamic list that can update itself, so it is a good way to implement our location list
        FirebaseAuth fAuth = FirebaseAuth.getInstance();

        // Setting the app name to "Team #1-username"
        String app_name = getString(R.string.app_name);
        if (fAuth.getCurrentUser() != null) {
            fAuth.getCurrentUser().reload();
            this.setTitle(app_name + "-" + fAuth.getCurrentUser().getDisplayName());
        }

        TextView textView = (TextView) findViewById(R.id.city_name_lat_lon);
        textView.setText("  " + locationName + "(" + locationLatitude + "°N" + "," + locationLongitude +"°E" +  ")"); //set text for text view
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * After map is ready to be used, we add a marker to the city that the user has chosen.
     * The documentation can be viewed in https://developers.google.com/maps/documentation/android-sdk/map
     * @param googleMap object is the entry point for managing the underlying map features and data.
     * @return No return needed for onCreate method.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // TODO: get latitude/longitude of the city,
        double loc_lat = Double.parseDouble(locationLatitude);
        double loc_long = Double.parseDouble(locationLongitude);
        LatLng city = new LatLng(loc_lat, loc_long);
        googleMap.addMarker(new MarkerOptions()
                .position(city)
                .title(locationName));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(city));
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(false);
        googleMap.getUiSettings().setScrollGesturesEnabled(false);
        googleMap.getUiSettings().setTiltGesturesEnabled(false);
    }
}