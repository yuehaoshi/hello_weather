package edu.uiuc.cs427app;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AddLocationActivity extends AppCompatActivity implements View.OnClickListener{

    private Geocoder geocoder;
    EditText enterCityName;
    TextView cityDisplay, cityLatLong;
    Double latitude;
    Double longitude;
    String city;

    /**
     * This function handles the input from the user for the city name to add to favourites
     * @param savedInstanceState default input parameter for onCreate method
     * @return No return needed for onCreate method
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addlocation);

        // set a listener for addLocationButton and back button
        Button addLocationButton = findViewById(R.id.addLocationButton);
        addLocationButton.setOnClickListener(this);
        Button backToList = findViewById(R.id.backToList);
        backToList.setOnClickListener(this);

        // Setting database object and recyclerView.
        // RecyclerView is similar to a dynamic list that can update itself, so it is a good way to implement our location list
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        fAuth.getCurrentUser().reload();

        // Setting the app name to "Team #1-username"
        String app_name = getString(R.string.app_name);
        this.setTitle(app_name + "-" + fAuth.getCurrentUser().getDisplayName());

        //Assign variable
        cityDisplay = findViewById(R.id.city_display);
        cityLatLong = findViewById(R.id.latlong_display);

        //Initialize geocoder
        geocoder = new Geocoder(this);
    }

    /**
     * This method handles the click event for confirming the location to the database
     * Source: https://developer.android.com/develop/ui/views/components/button
     * @param view: This is the view that is being clicked
     * @return: No return needed for void method
     */

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.addLocationButton) {
            // Getting the user input from Textview
            TextView input = findViewById(R.id.enter_city_name);
            String str = input.getText().toString();
            TextView addLocationConfirmation = findViewById(R.id.addLocationConfirmation);

            try {
                List<Address> addresses = geocoder.getFromLocationName(str, 1);
                if (addresses.size() > 0) {
                    Address address = addresses.get(0);
                    // Save latitude and longitude information into global variable
                    latitude = address.getLatitude();
                    longitude = address.getLongitude();
                    city = address.getLocality();

                    //Display city name and Latitude, Longitude
                    cityDisplay.setText(address.getLocality());
                    cityLatLong.setText(String.valueOf(latitude) + ", " + String.valueOf(longitude));

                    // Get reference of current user/city from Firebase
                    FirebaseAuth fAuth = FirebaseAuth.getInstance();
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference();
                    DatabaseReference userRef = ref.child("Users").child(fAuth.getCurrentUser().getUid()).child("locations").child(city + "Info");

                    // Get a location reference for current location to store latitude and longitude for current location
                    location location = new location();
                    location.setLocationName(city);
                    location.setLocationLatitude(latitude);
                    location.setLocationLongitude(longitude);
                    userRef.setValue(location);

                    // Displaying a confirmation message after adding a new location
                    String confirmation = "Successfully added " + city + " to your list!";
                    addLocationConfirmation.setText(confirmation);

                } else {
                    // Displaying error message
                    String errorMessage = "Please enter a valid city name.";
                    addLocationConfirmation.setText(errorMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (view.getId() == R.id.backToList){
            // Going back to main page after adding  new location
            Intent intent;
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}
