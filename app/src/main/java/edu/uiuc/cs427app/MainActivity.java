package edu.uiuc.cs427app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView recyclerView;
    locationAdapter adapter; // Create Object of the Adapter class
    DatabaseReference mbase; // Create object of the Firebase Realtime Database
    DatabaseReference mbase1;

    /**
     * This function displays the Favourites list of the user with buttons to remove the favourites and check the map and weather of the favourites.
     * The database is hosted on Firebase Realtime Database.
     * * Firebase Realtime Database does not provide a simple "get()" method to read data;
     * Instead, we need to set onDataChange() method to read a static snapshot of the content.
     * Tutorial for Read Data from Firebase: https://firebase.google.com/docs/database/android/read-and-write
     * @param savedInstanceState default input parameter for onCreate method
     * @return No return needed for onCreate method
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseAuth fAuth = FirebaseAuth.getInstance();


        // Setting database object and recyclerView.
        // RecyclerView is similar to a dynamic list that can update itself, so it is a good way to implement our location list
       if( fAuth.getCurrentUser()!=null) {
           fAuth.getCurrentUser().reload();
       }
        // Setting the app name to "Team #1-username"
        String app_name = getString(R.string.app_name);
        if( fAuth.getCurrentUser()!=null) {
            this.setTitle(app_name + "-" + fAuth.getCurrentUser().getDisplayName());

            mbase = FirebaseDatabase.getInstance().getReference().child("Users").child(fAuth.getCurrentUser().getUid()).child("locations");
            recyclerView = findViewById(R.id.recycler1);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setItemAnimator(null);
            FirebaseRecyclerOptions<location> options
                    = new FirebaseRecyclerOptions.Builder<location>()
                    .setQuery(mbase, location.class)
                    .build();
            adapter = new locationAdapter(options);
            recyclerView.setAdapter(adapter);

            // Setting a listener for add location
            Button buttonNew = findViewById(R.id.buttonAddLocation);
            buttonNew.setOnClickListener(this);

            //Getting the UI mode from Database

            mbase1 = FirebaseDatabase.getInstance().getReference().child("Users").child(fAuth.getCurrentUser().getUid()).child("darkMode");


            mbase1.addValueEventListener(new ValueEventListener() {
                @Override

                /**
                 * This method will be called with a snapshot of the data at this location.
                 * It will also be called each time that data changes.
                 * Tutorial for Read Data from Firebase: https://firebase.google.com/docs/database/android/read-and-write
                 * Source : https://firebase.google.com/docs/reference/kotlin/com/google/firebase/database/ValueEventListener
                 * @param dataSnapshot: The current data at the location
                 * @return: No return needed for void method
                 */

                //Function to get a snapshot of the Firebase Data
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Boolean ui = dataSnapshot.getValue(Boolean.class);
                    if (ui == true) {
                        AppCompatDelegate.setDefaultNightMode(
                                AppCompatDelegate.MODE_NIGHT_YES);
                    }
                }

                /**
                 * This method will be triggered in the event that this listener either failed at the server, or is removed as a result of the security and Firebase Database rules.
                 * For more information on securing your data, see: Security Quickstart https://firebase.google.com/docs/database/security/get-started
                 * Source: https://firebase.google.com/docs/reference/kotlin/com/google/firebase/database/ValueEventListener
                 *
                 * @param databaseError: A description of the error that occurred
                 * @return: No return needed for void method
                 */
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
        //Display the confirmation that a city has been removed.
        if (getIntent().hasExtra(("city"))) {
            String cityName = getIntent().getStringExtra("city").toString();
            String delConfirmStr = cityName + " has been removed from the list.";
            Toast.makeText(MainActivity.this, delConfirmStr, Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * This method handles the click event for adding location and directs the user to addLocation page
     * Source: https://developer.android.com/develop/ui/views/components/button
     * @param view: This is the view that is being clicked
     * @return: No return needed for void method
     */

    @Override
    public void onClick(View view) {
        // User will be directed to addLocation page when clicking on ButtonAddLocation
        Intent intent;
        if (view.getId() == R.id.buttonAddLocation) {
            intent = new Intent(this, AddLocationActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * The onClick method is configured to call logout to sign the user out
     * Source: https://developers.google.com/identity/sign-in/android/disconnect
     * @param view: This is the view that is being clicked
     * @return: No return needed for void method
     */

    public void logout(View view) {
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO);

        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));

        //finish();
    }

    /**
     * This method initiates listening to Firebase
     * Source: https://firebase.google.com/docs/auth/android/start
     * @return: No return needed for void method
     */

    // Function to tell the app to start getting
    // data from database on starting of the activity
    @Override protected void onStart()
    {
        super.onStart();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            adapter.startListening();
        }

    }

    /**
     * This method halts listening to Firebase
     * Source: https://firebase.google.com/docs/auth/android/start
     * @return: No return needed for void method
     */

    // Function to tell the app to stop getting
    // data from database on stopping of the activity
    @Override protected void onStop()
    {
        super.onStop();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            adapter.stopListening();
        }
    }
}