package edu.uiuc.cs427app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener{

    String locationLatitude;
    String locationLongitude;
    static String locationName;

    /**
     * This function provides the options available to the user for the chosen city, that is Map and Weather
     * @param savedInstanceState default input parameter for onCreate method
     * @return No return needed for onCreate method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Setting database object and recyclerView.
        // RecyclerView is similar to a dynamic list that can update itself, so it is a good way to implement our location list
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        fAuth.getCurrentUser().reload();

        // Setting the app name to "Team #1-username"
        String app_name = getString(R.string.app_name);
        this.setTitle(app_name + "-" + fAuth.getCurrentUser().getDisplayName());


        // Process the Intent payload that has opened this Activity and show the information accordingly
        String cityName = getIntent().getStringExtra("city").toString();
        String welcome = "Welcome to " + cityName + "!";
        String cityWeatherInfo = "Find detailed information about " + cityName + " below.";
        locationName = cityName;

        // Initializing the GUI elements
        TextView welcomeMessage = findViewById(R.id.welcomeText);
        TextView cityInfoMessage = findViewById(R.id.cityInfo);

        welcomeMessage.setText(welcome);
        cityInfoMessage.setText(cityWeatherInfo);

        // A map button which on click shows the map of the city.
        Button buttonMap = findViewById(R.id.mapButton);
        buttonMap.setOnClickListener(this);

        // A weather button which on click shows the weather details of current city
        Button buttonWeather = findViewById(R.id.weatherButton);
        buttonWeather.setOnClickListener(this);

        // Get location latitude and longitude information from Firebase and store in global variable
        readData(new MyCallback() {
            /**
             * This function sets the latitude to a global variable
             * @param value latitude of String Type
             * @return No return needed for onCreate method
             */
            @Override
            public void onLatCallback(String value) { locationLatitude = value;}
            /**
             * This function sets the longitude to a global variable
             * @param value longitude of String Type
             * @return No return needed for onCreate method
             */
            @Override
            public void onLngCallback(String value) {locationLongitude = value;}
        });
    }

    /**
     * This method handles the click events for viewing the Map and viewing the Weather information
     * Source: https://developer.android.com/develop/ui/views/components/button
     * @param view: This is the view that is being clicked
     * @return: No return needed for void method
     */

    @Override
    public void onClick(View view) {
        Intent intent;
        Intent map_intent;
        switch (view.getId()) {
            case R.id.mapButton:
                //Implement this (create an Intent that goes to a new Activity, which shows the map)
                String city = getIntent().getStringExtra("city");
                map_intent = new Intent(getApplicationContext(), MapPage.class);
                // Pass cityName, latitude, longitude to weather page for API query
                map_intent.putExtra("city", locationName);
                map_intent.putExtra("latitude", locationLatitude);
                map_intent.putExtra("longitude", locationLongitude);
                startActivity(map_intent);
                break;
            case R.id.weatherButton:
                // Redirect to weather details page
                String locationName = getIntent().getStringExtra("city");
                intent = new Intent(this, WeatherPage.class);

                // Pass cityName, latitude, longitude to weather page for API query
                intent.putExtra("city", locationName);
                intent.putExtra("latitude", locationLatitude);
                intent.putExtra("longitude", locationLongitude);
                startActivity(intent);
                break;
        }
    }

    /**
     * "MyCallback" interface and "readData" class are implemented to retrieve latitude and longitude data from Firebase
     * No params and return needed for interface
     */
    public interface MyCallback {
        void onLatCallback(String value);
        void onLngCallback(String value);
    }

    /**
     * Method to get data from database
     * "MyCallback" interface and "readData" class are implemented to retrieve latitude and longitude data from Firebase
     * Firebase Realtime Database does not provide a simple "get()" method to read data;
     * Instead, we need to set onDataChange() method to read a static snapshot of the content.
     * Tutorial for Read Data from Firebase: https://firebase.google.com/docs/database/android/read-and-write
     * Since reading data is an asynchronous process, we need to use a self-defined callback interface to store database data to global variable
     * Reference to store database data as global variable: https://stackoverflow.com/questions/47847694/how-to-return-datasnapshot-value-as-a-result-of-a-method/47853774
     *
     * @param myCallback
     * No return needed for void method
     */
    public static void readData(MyCallback myCallback) {
        // Connect and get a reference of Firebase Realtime database
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        DatabaseReference curLat = ref.child("Users").child(fAuth.getCurrentUser().getUid()).child("locations").child(locationName + "Info").child("locationLatitude");
        DatabaseReference curLng = ref.child("Users").child(fAuth.getCurrentUser().getUid()).child("locations").child(locationName + "Info").child("locationLongitude");

        // Set Listeners for latitude data
        curLat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Get data in Double type, round to 2-decimal, and convert to String type
                Double val = (Double) snapshot.getValue();
                Double roundedLat = (Math.round(val * 100.0) / 100.0);
                String latStr = roundedLat.toString();
                myCallback.onLatCallback(latStr);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        // Set Listeners for longitude data
        curLng.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Get data in Double type, round to 2-decimal, and convert to String type
                Double val = (Double) snapshot.getValue();
                Double roundedLng = (Math.round(val * 100.0) / 100.0);
                String lngStr = roundedLng.toString();
                myCallback.onLngCallback(lngStr);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
}

