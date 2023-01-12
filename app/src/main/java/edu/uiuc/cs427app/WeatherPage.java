package edu.uiuc.cs427app;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WeatherPage extends AppCompatActivity {
    private TextView currentTemp;
    private TextView weatherDesc;
    private TextView humidity;
    private TextView windCondition;
    String locationLatitude;
    String locationLongitude;
    static String locationName;
    String name;
    String password;

    /**
     * This function calls API and sets the TextViews when the page is generated
     * @param savedInstanceState default input parameter for onCreate method
     * @return No return needed for onCreate method
     */
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get city name, latitude, longitude information passed from "DetailsActivity" page
        locationName = getIntent().getStringExtra("city");
        locationLatitude = getIntent().getStringExtra("latitude");
        locationLongitude = getIntent().getStringExtra("longitude");
        name = getIntent().getStringExtra("name");
        password = getIntent().getStringExtra("password");
        // Connect to UI page
        setContentView(R.layout.activity_weather);
        String city = locationName;

        // Setting database object and recyclerView.
        // RecyclerView is similar to a dynamic list that can update itself, so it is a good way to implement our location list
        FirebaseAuth fAuth = FirebaseAuth.getInstance();

        // Setting the app name to "Team #1-username"
        String app_name = getString(R.string.app_name);
        if (fAuth.getCurrentUser() != null) {
            fAuth.getCurrentUser().reload();
            this.setTitle(app_name + "-" + fAuth.getCurrentUser().getDisplayName());
        }



        // Connect to TextViews in UI page
        // TextViews on UI page
        TextView cityName = findViewById(R.id.cityName);
        TextView date = findViewById(R.id.date);
        currentTemp = findViewById(R.id.currentTemp);
        weatherDesc = findViewById(R.id.weatherDesc);
        humidity = findViewById(R.id.humidity);
        windCondition = findViewById(R.id.windCondition);

        // Display city name and current data information
        cityName.setText("City: " + city);

        // Add current system date and time into textView since weather API does not return time information
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy HH:mm", Locale.getDefault());
        String formattedDate = df.format(c);
        date.setText("Time: " + formattedDate);

        // Call API and set other TextViews
        new JSONWeatherTask().execute();
    }


    /**
     * Query by OpenWeather API and update TextView
     * This is an Asynchronous method, doInBackground will call API and generate output JSON String,
     * onPostExecute will then parse the JSON information and set corresponding TextViews
     * The logic of AsyncTask can be found in this post: https://stackoverflow.com/questions/9671546/asynctask-android-example
     *
     * No param and return needed for this AsyncTask function, all changes are done during execution
     */
    @SuppressLint("StaticFieldLeak")
    private class JSONWeatherTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String[] args) {
            // Generate API call URL
            String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + locationLatitude + "&lon=" + locationLongitude + "&exclude=hourly,daily&appid=e1dca6030c2a3a4bc4d94f398c03cb10";
            // Call API and store output into "result"
            return weatherHTTPClient.getWeatherData(url);
        }
        protected void onPostExecute(String result) {
            try {
                String temp = "";
                String desc = "";
                String hum = "";
                String windSpeed = "";
                // Parse JSON file generated from API call
                // Details of parsing JSON file in Java can be found here: https://processing.org/reference/JSONObject_getJSONArray_.html
                JSONObject obj = new JSONObject(result);
                JSONObject main = obj.getJSONObject("main");
                JSONObject weatherDetail = obj.getJSONArray("weather").getJSONObject(0);
                JSONObject windDesc = obj.getJSONObject("wind");

                // Locate information from parsed file and generate output string for each TextView
                temp = "Temperature: " + main.getString("temp") + "K";
                desc = "Weather: " + weatherDetail.getString("description");
                hum = "Humidity: " + main.getInt("humidity") + "%";
                windSpeed = "Wind Speed: " + windDesc.getDouble("speed") + "m/s";
                // Update Content for each TextView
                currentTemp.setText(temp);
                weatherDesc.setText(desc);
                humidity.setText(hum);
                windCondition.setText(windSpeed);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}