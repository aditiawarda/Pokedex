package com.gelora.pokemonapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.pokemonapp.DetailItemActivity;
import com.gelora.pokemonapp.HomePageActivity;
import com.gelora.pokemonapp.R;
import com.gelora.pokemonapp.model.AbilitiesModels;
import com.gelora.pokemonapp.model.DataModels;

import java.util.Random;

public class AdapterListAbilities extends RecyclerView.Adapter<AdapterListAbilities.MyViewHolder> {

    private final AbilitiesModels[] data;
    private final Context mContext;

    public AdapterListAbilities(AbilitiesModels[] data, DetailItemActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_abilities,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final AbilitiesModels abilitiesModels = data[i];
        myViewHolder.abilitiesName.setText(abilitiesModels.getName().substring(0,1).toUpperCase()+abilitiesModels.getName().substring(1,abilitiesModels.getName().length()));
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout dataParent;
        TextView abilitiesName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            dataParent = itemView.findViewById(R.id.data_parent);
            abilitiesName = itemView.findViewById(R.id.abilities_name);
        }
    }
}
