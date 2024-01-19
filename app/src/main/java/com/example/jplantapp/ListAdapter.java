package com.example.jplantapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.jplantapp.Response.FlowerResponse;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.FlowerViewHolder> {
    private List<FlowerResponse> flowerList;
    public ListAdapter(List<FlowerResponse> flowerList) {
        this.flowerList = flowerList;
    }

    @NonNull
    @Override
    public FlowerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new FlowerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlowerViewHolder holder, @SuppressLint("RecyclerView") int position) {
        FlowerResponse flowerItem = flowerList.get(position);
        holder.bindData(flowerItem);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FlowerResponse selectedFlower = flowerList.get(position);
                Intent intent = new Intent(view.getContext(), FlowerActivity.class);
                intent.putExtra("flower", selectedFlower);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return flowerList.size();
    }

    public void setFlowerList(List<FlowerResponse> flowerList) {
        this.flowerList = flowerList;
    }

    public static class FlowerViewHolder extends RecyclerView.ViewHolder {
        private TextView flowerName;
        private TextView flowerAcc;
        private ImageView flowerImage;

        public FlowerViewHolder(@NonNull View itemView) {
            super(itemView);
            flowerName = itemView.findViewById(R.id.listFlowerName);
            flowerAcc = itemView.findViewById(R.id.listAccuracy);
            flowerImage = itemView.findViewById(R.id.listFlowerImage);
        }

        public void bindData(FlowerResponse flowerResponse) {
            flowerName.setText(flowerResponse.getName());
            flowerAcc.setText(String.valueOf(flowerResponse.getPrediction()) + "%");

            String imagePath = flowerResponse.getImagePath();
            Glide.with(itemView).load(imagePath).into(flowerImage);
        }
    }
}