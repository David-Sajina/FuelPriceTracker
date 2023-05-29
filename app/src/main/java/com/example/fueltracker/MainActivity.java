package com.example.fueltracker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerViewFuel;
    private FuelAdapter fuelAdapter;

    public class Fuel {
        private String fuelName;
        private List<GasStation> gasStations;

        public Fuel(String fuelName, List<GasStation> gasStations) {
            this.fuelName = fuelName;
            this.gasStations = gasStations;
        }

        public String getFuelName() {
            return fuelName;
        }

        public List<GasStation> getGasStations() {
            return gasStations;
        }
    }
    public class GasStation {
        private String name;
        private String price;

        public GasStation(String name, String price) {
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public String getPrice() {
            return price;
        }
    }
    private class FuelDataFetchTask extends AsyncTask<Void, Void, List<Fuel>> {
        @Override
        protected List<Fuel> doInBackground(Void... voids) {
            return getFuelDataFromApi();
        }

        @Override
        protected void onPostExecute(List<Fuel> fuelList) {
            Log.d("MainActivity", "Fuel List: " + fuelList);
            fuelAdapter.setFuelList(fuelList);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewFuel = findViewById(R.id.recycler_view_fuel);
        recyclerViewFuel.setLayoutManager(new LinearLayoutManager(this));

        fuelAdapter = new FuelAdapter(new ArrayList<>());
        recyclerViewFuel.setAdapter(fuelAdapter);

        new FuelDataFetchTask().execute();
    }


    // Your method to fetch the fuel data from the API
    private List<Fuel> getFuelDataFromApi() {
        List<Fuel> fuelList = new ArrayList<>();

        try {
            // Create a URL object with the API endpoint
            URL url = new URL("https://fuel-service.onrender.com/");

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            // Check if the response code is successful
            int responseCode = connection.getResponseCode();
            Log.d("MainActivity", "API Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response from the API
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }
                bufferedReader.close();

                // Parse the JSON response
                Log.d("MainActivity", "API Response: " + response.toString());

                JSONArray fuelDataArray = new JSONArray(response.toString());
                for (int i = 0; i < fuelDataArray.length(); i++) {
                    JSONObject fuelObject = fuelDataArray.getJSONObject(i);
                    String fuelName = fuelObject.getString("Fuel");
                    JSONArray gasStationsArray = fuelObject.getJSONArray("data");

                    List<GasStation> gasStations = new ArrayList<>();
                    for (int j = 0; j < gasStationsArray.length(); j++) {
                        JSONObject gasStationObject = gasStationsArray.getJSONObject(j);
                        String gasStationName = gasStationObject.getString("name");
                        String gasStationPrice = gasStationObject.getString("price");
                        GasStation gasStation = new GasStation(gasStationName, gasStationPrice);
                        gasStations.add(gasStation);
                    }

                    Fuel fuel = new Fuel(fuelName, gasStations);
                    fuelList.add(fuel);
                    Log.d("MainActivity", "Parsed Fuel List: " + fuelList);
                }
            } else {
                // Handle the error response
                Log.e("MainActivity", "API Error Response: " + responseCode);
            }

            // Disconnect the connection
            connection.disconnect();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return fuelList;
    }






}
