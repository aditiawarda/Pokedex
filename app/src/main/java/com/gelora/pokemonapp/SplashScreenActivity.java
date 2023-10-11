package com.gelora.pokemonapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gelora.pokemonapp.adapter.AdapterListItem;
import com.gelora.pokemonapp.helper.DatabaseHelper;
import com.gelora.pokemonapp.model.DataModels;
import com.gelora.pokemonapp.support.StatusBarColorManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

    Button startButton;
    DatabaseHelper db = new DatabaseHelper(this);
    ProgressBar loadingData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        StatusBarColorManager mStatusBarColorManager = new StatusBarColorManager(this);
        mStatusBarColorManager.setStatusBarColor(Color.BLACK, true, false);
        startButton = findViewById(R.id.start_button);
        loadingData = findViewById(R.id.loading_data);
        db = new DatabaseHelper(getApplicationContext());
        db.truncateTable("items");

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashScreenActivity.this, HomePageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        getData();

    }

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        final String url = "https://pokeapi.co/api/v2/pokemon/";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("PaRSE JSON", response + "");
                        try {
                            String results = response.getString("results");
                            Gson gson = new Gson();
                            JsonArray jsonArray = gson.fromJson(results, JsonArray.class);

                            int lastIndex = jsonArray.size() - 1;
                            for (int index = 0; index < jsonArray.size(); index++) {
                                JsonElement element = jsonArray.get(index);
                                JsonObject jsonObject = element.getAsJsonObject();
                                String name = jsonObject.get("name").getAsString();
                                String url = jsonObject.get("url").getAsString();
                                db.insert(name,url);

                                if (index == lastIndex) {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            loadingData.setVisibility(View.GONE);
                                            startButton.setVisibility(View.VISIBLE);
                                        }
                                    }, 3000);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);

        request.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

}