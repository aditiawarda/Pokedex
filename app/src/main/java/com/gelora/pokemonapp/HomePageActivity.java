package com.gelora.pokemonapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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
    EditText keywordData;
    RadioGroup filterSorting;
    RadioButton ascFilter, descFilter;
    String sortingFilter = "ASC";
    DatabaseHelper db = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        StatusBarColorManager mStatusBarColorManager = new StatusBarColorManager(this);
        mStatusBarColorManager.setStatusBarColor(Color.BLACK, true, false);
        db = new DatabaseHelper(getApplicationContext());

        keywordData = findViewById(R.id.keyword_ed);
        filterSorting = findViewById(R.id.radio_group);
        ascFilter = findViewById(R.id.radio_asc);
        descFilter = findViewById(R.id.radio_desc);
        listItemRV = findViewById(R.id.list_item_rv);
        listItemRV.setLayoutManager(new LinearLayoutManager(this));
        listItemRV.setHasFixedSize(true);
        listItemRV.setNestedScrollingEnabled(false);
        listItemRV.setItemAnimator(new DefaultItemAnimator());

        ascFilter.setChecked(true);

        keywordData.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String keyword = keywordData.getText().toString();
                getSearch(keyword, sortingFilter);
            }

        });

        keywordData.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                return false;
            }
        });

        filterSorting.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (ascFilter.isChecked()) {
                    sortingFilter = "ASC";
                    if(!keywordData.getText().toString().equals("")){
                        getSearch(keywordData.getText().toString(), sortingFilter);
                    } else {
                        getDataAll(sortingFilter);
                    }
                } else if (descFilter.isChecked()) {
                    sortingFilter = "DESC";
                    if(!keywordData.getText().toString().equals("")){
                        getSearch(keywordData.getText().toString(), sortingFilter);
                    } else {
                        getDataAll(sortingFilter);
                    }
                }
            }
        });

        getDataAll(sortingFilter);

    }

    @SuppressLint("NotifyDataSetChanged")
    private void getDataAll(String filter){
        ArrayList<HashMap<String, String>> rows = db.getAll(filter);
        Gson gsonMain = new Gson();
        String jsonArray = gsonMain.toJson(rows);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        dataModels = gson.fromJson(jsonArray, DataModels[].class);
        adapterListItem = new AdapterListItem(dataModels,HomePageActivity.this);
        listItemRV.setAdapter(adapterListItem);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getSearch(String keyword, String filter){
        ArrayList<HashMap<String, String>> rows = db.search(keyword, filter);
        Gson gsonMain = new Gson();
        String jsonArray = gsonMain.toJson(rows);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        dataModels = gson.fromJson(jsonArray, DataModels[].class);
        adapterListItem = new AdapterListItem(dataModels,HomePageActivity.this);
        listItemRV.setAdapter(adapterListItem);
    }

}