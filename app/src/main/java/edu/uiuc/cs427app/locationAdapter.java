package edu.uiuc.cs427app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 *  // RecycleView Adapter implementation is referred from this tutorial:
 * // https://www.geeksforgeeks.org/how-to-populate-recyclerview-with-firebase-data-using-firebaseui-in-android-studio/
 * // FirebaseRecyclerAdapter is a class provided by FirebaseUI.
 * // it provides functions to bind, adapt and show database contents in a Recycler View
 */

public class locationAdapter extends FirebaseRecyclerAdapter<
        location, locationAdapter.locationsViewholder> {


    /**
     *  Initialize the adapter
     * @param options
     * */
    public locationAdapter(@NonNull FirebaseRecyclerOptions<location> options)
    {
        super(options);
    }

    /**
     *  Called by RecyclerView to display the data at the specified position.
     *  This method should update the contents of the itemView to reflect the item at the given position.
     *  Source: https://developer.android.com/reference/androidx/recyclerview/widget/RecyclerView.Adapter#onCreateViewHolder(android.view.ViewGroup,int)
     *  @param @NonNull VH holder: The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param position: The position of the item within the adapter's data set.
     * @param @NonNull List<Object> payloads
     * A non-null list of merged payloads. Can be empty list if requires full update.
     *  */

    // Function to bind the view in Card view("location.xml") with data in model class("location.class")
    @Override
    protected void
    onBindViewHolder(@NonNull locationsViewholder holder,
                     int position, @NonNull location model)
    {
        // Add locationName from model class ("location.class")to appropriate view in Card view ("location.xml")
        holder.locationName.setText(model.getLocationName());
    }
    /**
     *  Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *  Source: https://developer.android.com/reference/androidx/recyclerview/widget/RecyclerView.Adapter#onCreateViewHolder(android.view.ViewGroup,int)
     *  @param : @NonNull ViewGroup parent : The ViewGroup into which the new View will be added after it is bound to an adapter position.
     *  @param : int viewType: The view type of the new View.
     *  A non-null list of merged payloads. Can be empty list if requires full update.
     *  */


    // Function to tell the class about the Card view ("location.xml")in which the data will be shown
    @NonNull
    @Override
    public locationsViewholder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType)
    {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location, parent, false);
        return new locationsViewholder(view);
    }

    // Sub Class to create references of the views in Card view (here "location.xml")
    static class locationsViewholder extends RecyclerView.ViewHolder {
        TextView locationName;
        public locationsViewholder(@NonNull View itemView)
        {
            super(itemView);

            locationName = itemView.findViewById(R.id.locationName);

            // Set a listener for "SHOW DETAILS" button in each RecycleView item
            itemView.findViewById(R.id.buttonLocationDetail).setOnClickListener(new View.OnClickListener() {
                /**
                 * This method handles the click events for show details for a location from the favourites list
                 * Source: https://developer.android.com/develop/ui/views/components/button
                 * @param view: This is the view that is being clicked
                 * @return: No return needed for void method
                 */
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), DetailsActivity.class);
                    intent.putExtra("city", locationName.getText());
                    itemView.getContext().startActivity(intent);
                }
            });

            /**
             * This method handles the click events for removing a location from the favourites list
             * Source: https://developer.android.com/develop/ui/views/components/button
             * @param view: This is the view that is being clicked
             * @return: No return needed for void method
             */

            itemView.findViewById(R.id.buttonDeletion).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //pass deleted city name to the main activity
                    Intent intent = new Intent(itemView.getContext(), MainActivity.class);
                    intent.putExtra("city", locationName.getText());
                    itemView.getContext().startActivity(intent);

                    FirebaseAuth fAuth = FirebaseAuth.getInstance();
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference();
                    DatabaseReference userRef = ref.child("Users").child(fAuth.getCurrentUser().getUid()).child("locations").child(locationName.getText() + "Info");
                    userRef.removeValue();

                }
            });
        }
    }
}
