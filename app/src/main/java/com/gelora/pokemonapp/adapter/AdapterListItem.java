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
import com.gelora.pokemonapp.model.DataModels;

import java.util.Random;

public class AdapterListItem extends RecyclerView.Adapter<AdapterListItem.MyViewHolder> {

    private final DataModels[] data;
    private final Context mContext;
    private int[] backgroundResources = {
            R.drawable.shape_item_1,
            R.drawable.shape_item_2,
            R.drawable.shape_item_3,
            R.drawable.shape_item_4,
            R.drawable.shape_item_5
    };

    public AdapterListItem(DataModels[] data, HomePageActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_data,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final DataModels dataTest = data[i];
        int randomIndex = new Random().nextInt(backgroundResources.length);
        myViewHolder.dataParent.setBackgroundResource(backgroundResources[randomIndex]);

        myViewHolder.itemName.setText(dataTest.getName().substring(0,1).toUpperCase()+dataTest.getName().substring(1,dataTest.getName().length()));
        myViewHolder.dataParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailItemActivity.class);
                intent.putExtra("name",dataTest.getName());
                intent.putExtra("url",dataTest.getUrl());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout dataParent;
        TextView itemName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            dataParent = itemView.findViewById(R.id.data_parent);
            itemName = itemView.findViewById(R.id.item_name);
        }
    }
}
