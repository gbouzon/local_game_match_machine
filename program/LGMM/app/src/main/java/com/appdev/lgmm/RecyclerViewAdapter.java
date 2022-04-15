package com.appdev.lgmm;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    ArrayList<String> plantNames = new ArrayList<>();
    ArrayList<String> images = new ArrayList<>();
    Context context;

    public RecyclerViewAdapter(ArrayList<String> plantNames, ArrayList<String> images, Context context) {
        this.plantNames = plantNames;
        this.images = images;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //image = (ImageView) itemView.findViewById(R.id.imageView);
            //name = (TextView) itemView.findViewById(R.id.nameTextView);
        }
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nearby_friend_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        holder.name.setText(plantNames.get(position));
        int imageResource = context.getResources().getIdentifier(images.get(position), null, context.getPackageName());
        Drawable res = context.getResources().getDrawable(imageResource);
        holder.image.setImageDrawable(res);
    }

    @Override
    public int getItemCount() {
        return plantNames.size();
    }
}
