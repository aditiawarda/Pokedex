package com.gelora.pokemonapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gelora.pokemonapp.adapter.AdapterListAbilities;
import com.gelora.pokemonapp.adapter.AdapterListItem;
import com.gelora.pokemonapp.model.AbilitiesModels;
import com.gelora.pokemonapp.model.DataModels;
import com.gelora.pokemonapp.support.StatusBarColorManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailItemActivity extends AppCompatActivity {

    TextView pokemonName;
    ImageView pokemonImage;
    String name, url;
    JsonArray jsonArrayFinal;

    RecyclerView listAbilitiesRV;
    AbilitiesModels[] abilitiesModels;
    AdapterListAbilities adapterListAbilities;

    List<String> abilitiesList = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_item);
        StatusBarColorManager mStatusBarColorManager = new StatusBarColorManager(this);
        mStatusBarColorManager.setStatusBarColor(Color.BLACK, true, false);
        pokemonName = findViewById(R.id.pokemon_name);
        pokemonImage = findViewById(R.id.pokemon_image);

        name = getIntent().getExtras().getString("name");
        url = getIntent().getExtras().getString("url");

        listAbilitiesRV = findViewById(R.id.list_abilities_rv);
        listAbilitiesRV.setLayoutManager(new LinearLayoutManager(this));
        listAbilitiesRV.setHasFixedSize(true);
        listAbilitiesRV.setNestedScrollingEnabled(false);
        listAbilitiesRV.setItemAnimator(new DefaultItemAnimator());

        pokemonName.setText(name.substring(0,1).toUpperCase()+name.substring(1,name.length()));
        getData(url);

    }

    private void getData(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("PaRSE JSON", response + "");
                        try {
                            String sprites = response.getString("sprites");
                            String abilities = response.getString("abilities");
                            Gson gson = new Gson();
                            JsonArray jsonArray = gson.fromJson(abilities, JsonArray.class);
                            jsonArrayFinal = new JsonArray();

                            for (int index = 0; index < jsonArray.size(); index++) {
                                JsonElement element = jsonArray.get(index);
                                JsonObject jsonObject = element.getAsJsonObject();
                                JsonObject ability = jsonObject.getAsJsonObject("ability");
                                String abilityName = ability.get("name").getAsString();
                                String abilityUrl = ability.get("url").getAsString();

                                JsonObject obj = new JsonObject();
                                obj.addProperty("name", abilityName);
                                obj.addProperty("url", abilityUrl);
                                jsonArrayFinal.add(obj);
                            }

                            JsonObject jsonObject = JsonParser.parseString(sprites).getAsJsonObject();
                            String front_default = jsonObject.get("front_default").getAsString();

                            Picasso.get().load(front_default).networkPolicy(NetworkPolicy.NO_CACHE)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                                    .into(pokemonImage);

                            Gson gsonMain = new Gson();
                            String jsonString = gsonMain.toJson(jsonArrayFinal);

                            GsonBuilder builder = new GsonBuilder();
                            Gson gsonAbilities = builder.create();
                            abilitiesModels = gsonAbilities.fromJson(jsonString, AbilitiesModels[].class);
                            adapterListAbilities = new AdapterListAbilities(abilitiesModels,DetailItemActivity.this);
                            listAbilitiesRV.setAdapter(adapterListAbilities);

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