package com.example.sumoweighttracker;
//this is the simplest list for the functions I have incorporated so far
import static com.example.sumoweighttracker.R.id.textViewResult;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NotificationSMS extends AppCompatActivity {

    // Find the TextView in your layout - this is what we are using to update the app from the website,
    // just need to have this same function on the targeted website
    private TextView textViewResult = findViewById(R.id.textViewResult);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_sms);

        // Perform the HTTP request in an AsyncTask
        new DownloadDataFromWebsiteTask().execute();
    }

    private class DownloadDataFromWebsiteTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                // this is the reference for the website (or websites) we will be using
                URL url = new URL("https://www.sumoweighttracker.com");

                // Open a connection to the URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Set the request method (GET)
                connection.setRequestMethod("GET");

                // Get the response code
                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // If the request was successful, read the response data
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    reader.close();

                    // Return the response as a string we can use since it is textview
                    return response.toString();
                } else {
                    // Handle the case where the request was not successful (e.g., error handling)
                    // Catch-all for any errors
                    return "Error: " + responseCode;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // Check if the result contains an error message
            if (result.startsWith("Error")) {
                // Handle the error, e.g., display an error message
                textViewResult.setText("Failed to retrieve data.");
            } else {
                // Update the TextView with the retrieved data
                textViewResult.setText(result);
            }
        }
    }
}
