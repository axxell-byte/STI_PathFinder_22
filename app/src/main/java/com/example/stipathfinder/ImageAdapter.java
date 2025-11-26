package com.example.stipathfinder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.Holder> {

    private final int[] images;
    private final Context context;

    public ImageAdapter(Context context, int[] images) {
        this.context = context;
        this.images = images;
    }

    static class Holder extends RecyclerView.ViewHolder {
        ImageView img;
        Holder(ImageView v) {
            super(v);
            img = v;
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ImageView v = new ImageView(context);
        v.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        v.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.img.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return images.length;
    }
}
