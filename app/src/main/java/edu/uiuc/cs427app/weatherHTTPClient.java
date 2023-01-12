package edu.uiuc.cs427app;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class weatherHTTPClient {
    /**
     * Returns a String generated from Weather API call output JSON file
     * Call OpenWeather API with customized URL
     * This method is referred from https://github.com/survivingwithandroid/Swa-app/tree/master/WeatherApp
     * and: https://www.codespeedy.com/weather-forecasting-android-app-using-openweathermap-api-in-android-studio/
     * @param targetURL an OpenWeather API call URL using query info and API key
     * @return a String transformed from OpenWeather API call JSON output file
     */
    public static String getWeatherData(String targetURL) {
        URL url;
        HttpURLConnection con = null;
        try {
            // Set HTTP connection
            url = new URL(targetURL);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            // Check HTTP status
            InputStream is;
            int status = con.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                is = con.getErrorStream();
            }
            else {
                is = con.getInputStream();
            }

            // Read HTTP response and store in output string
            BufferedReader br = new BufferedReader((new InputStreamReader(is)));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = br.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            br.close();

            // Return generated string from HTTP response
            return response.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        finally {
            // Close connection
            if (con != null) con.disconnect();
        }
    }
}