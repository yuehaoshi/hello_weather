package edu.uiuc.cs427app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import edu.uiuc.cs427app.MainActivity;
import edu.uiuc.cs427app.R;
import edu.uiuc.cs427app.Register;

public class Login extends AppCompatActivity {

    // Initialization

    EditText lemail,lpass;
    Button loginBtn;
    TextView mRegisterBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;


    /**
     * This function takes email and password of the user as input to login to the app by .
     * The user is given an option to register if they are not members already
     * The authentication is hosted by Firebase Authentication
     * Documentation can be found @ https://firebase.google.com/docs/auth/android/start
     * @param savedInstanceState default input parameter for onCreate method
     * No return needed for onCreate method
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d("myTag", "In Login.java");

        // Extracting information from UI elements. Refer activity_login.xml

        lemail=findViewById(R.id.emaila);
        lpass=findViewById(R.id.passworda);
        progressBar=findViewById(R.id.progressBar2);
        fAuth=FirebaseAuth.getInstance();
        loginBtn=findViewById(R.id.login);
        mRegisterBtn=findViewById(R.id.to_signup);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            /**
             * This method handles the click event for authenticating user
             * Source: https://developer.android.com/develop/ui/views/components/button
             * @param view: This is the view that is being clicked
             * @return: No return needed for void method
             */
            @Override
            public void onClick(View view) {
                String memail= lemail.getText().toString().trim();
                String password=lpass.getText().toString().trim();

                // Validating the user information

                // Check whether the Email entered is of the form email@site.com

                if (!TextUtils.isEmpty(memail) && Patterns.EMAIL_ADDRESS.matcher(memail).matches()) {
                    System.console();
                }
                else{
                    lemail.setError("Please enter valid Email ID");
                    return;
                }

                // Check whether the user has entered an Email

                if(TextUtils.isEmpty(memail)){
                    lemail.setError("Please enter Email ID");
                    return;
                }

                // Check whether the user has entered a password

                if(TextUtils.isEmpty(password)){
                    lpass.setError("Password is required");
                    return;
                }

                // Check whether the user has entered a valid password

                if(password.length()<6){
                    lpass.setError("Password must be greater than 6 characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // Authenticate the user

                fAuth.signInWithEmailAndPassword(memail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    /**
                     * Listener called when login Task completes.
                     * Source: https://developer.android.com/reference/com/google/android/play/core/tasks/OnCompleteListener
                     * @param task	Task: the completed Task. Never null
                     * @return: No return needed for void method
                     */
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(edu.uiuc.cs427app.Login.this, "Welcome back!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                            finish();
                        }else{
                            Toast.makeText(edu.uiuc.cs427app.Login.this, "Error is occurred: " +task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        }
                    }
                });


            }
        });

        // Add a register button in case the user does not have an account
        /**
         * This method handles the click event for directing user to Register page if the user doesn't have an account
         * Source: https://developer.android.com/develop/ui/views/components/button
         * @param view: This is the view that is being clicked
         * @return: No return needed for void method
         */

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
                finish();
            }
        });



    }
}