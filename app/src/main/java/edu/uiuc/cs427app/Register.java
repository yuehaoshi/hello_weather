package edu.uiuc.cs427app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    EditText name,email,pass,phone;

    Boolean isDarkMode = false;

    Button register;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;

    /**
     * This function takes name, email, phone number, and password of the user as input to register the user in the database.
     * The authentication is hosted by Firebase Authentication
     * The user is given an option to login if they are members already
     * Documentation can be viewed @ https://firebase.google.com/docs/auth/android/start
     * The user also needs to choose the theme they would liken to set for UI
     * Additionally the entered data is validated as well
     * @param savedInstanceState default input parameter for onCreate method
     * No return needed for onCreate method
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        pass=findViewById(R.id.password);
        phone=findViewById(R.id.phone);
        register=findViewById(R.id.register);
        Switch aSwitch = findViewById(R.id.switch_dark);
        mLoginBtn=findViewById(R.id.to_login);

        fAuth= FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progressBar);

        if(fAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        register.setOnClickListener(new View.OnClickListener() {
            /**
             * This method handles the click event for logging a  user in
             * Source: https://developer.android.com/develop/ui/views/components/button
             * @param v: This is the view that is being clicked
             * @return: No return needed for void method
             */
            @Override
            public void onClick(View v) {
                String displayedName = name.getText().toString().trim();
                String memail= email.getText().toString().trim();
                String password=pass.getText().toString().trim();

                // Validating the user information
                // Check whether the Email entered is of the form email@site.com

                if (!TextUtils.isEmpty(memail) && Patterns.EMAIL_ADDRESS.matcher(memail).matches()) {
                    System.console();
                }
                else{
                    email.setError("Please enter valid Email ID");
                    return;
                }

                // Check whether the user has entered an Email

                if(TextUtils.isEmpty(memail)){
                    email.setError("Please enter Email ID");
                    return;
                }

                // Check whether the user has entered a password

                if(TextUtils.isEmpty(password)){
                    pass.setError("Password is required");
                    return;
                }

                // Check whether the user has entered a valid password

                if(password.length()<6){
                    pass.setError("Password must be greater than 6 characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // Add User details to the firebase

                fAuth.createUserWithEmailAndPassword(memail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    /**
                     * Listener called when create user Task completes.
                     * Source: https://developer.android.com/reference/com/google/android/play/core/tasks/OnCompleteListener
                     * @param task	Task: the completed Task. Never null
                     * @return: No return needed for void method
                     */
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            // Updating user's selection of dark/light mode in firebase.
                            final FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference ref = database.getReference();
                            DatabaseReference userRef = ref.child("Users").child(fAuth.getCurrentUser().getUid()).child("darkMode");
                            userRef.setValue(isDarkMode);

                            // Updating user's displayed name in firebase so that it can be displayed on the app's main page.
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(displayedName)
                                    .build();

                            FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        /**
                                         * Listener called when register Task completes finally.
                                         * Source: https://developer.android.com/reference/com/google/android/play/core/tasks/OnCompleteListener
                                         * @param task	Task: the completed Task. Never null
                                         * @return: No return needed for void method
                                         */
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(Register.this, "User Created!", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                            }
                                        }
                                    });
                        }else{
                            Toast.makeText(Register.this, "Error is occurred : " +task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        }
                    }
                });

            }
        });

        // Add a login button in case the user has an account

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * This method handles the click event for directing user to Login page if the user has an account
             * Source: https://developer.android.com/develop/ui/views/components/button
             * @param view: This is the view that is being clicked
             * @return: No return needed for void method
             */
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            // It is used to change the light/dark mode through a switch

            /**
             * Called when the checked state of a compound button has changed.
             * Source: https://developer.android.com/reference/android/widget/CompoundButton.OnCheckedChangeListener#onCheckedChanged(android.widget.CompoundButton,%20boolean)
             * @param buttonView : CompoundButton: The compound button view whose state has changed.
             * @param isChecked	boolean: The new checked state of buttonView.
             * @return: No return needed for void method
             */
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isDarkMode = true;
                    AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    isDarkMode = false;
                    AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });
    }
}