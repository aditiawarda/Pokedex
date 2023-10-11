package com.gelora.pokemonapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ListView;

import com.gelora.pokemonapp.adapter.AdapterListItem;
import com.gelora.pokemonapp.helper.DatabaseHelper;
import com.gelora.pokemonapp.model.DataModels;
import com.gelora.pokemonapp.support.StatusBarColorManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {

    RecyclerView listItemRV;
    DataModels[] dataModels;
    AdapterListItem adapterListItem;
    DatabaseHelper db = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        StatusBarColorManager mStatusBarColorManager = new StatusBarColorManager(this);
        mStatusBarColorManager.setStatusBarColor(Color.BLACK, true, false);
        db = new DatabaseHelper(getApplicationContext());

        listItemRV = findViewById(R.id.list_item_rv);
        listItemRV.setLayoutManager(new LinearLayoutManager(this));
        listItemRV.setHasFixedSize(true);
        listItemRV.setNestedScrollingEnabled(false);
        listItemRV.setItemAnimator(new DefaultItemAnimator());

        getDataAll();

    }

    @SuppressLint("NotifyDataSetChanged")
    private void getDataAll(){
        ArrayList<HashMap<String, String>> rows = db.getAll();
        Gson gsonMain = new Gson();
        String jsonArray = gsonMain.toJson(rows);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        dataModels = gson.fromJson(jsonArray, DataModels[].class);
        adapterListItem = new AdapterListItem(dataModels,HomePageActivity.this);
        listItemRV.setAdapter(adapterListItem);
    }

}