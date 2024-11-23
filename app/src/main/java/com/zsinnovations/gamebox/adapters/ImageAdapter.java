package com.zsinnovations.gamebox.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.graphics.Color;
import de.hdodenhof.circleimageview.CircleImageView;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private int[] images;
    private int selectedImageResource; // Store the currently selected image resource

    // Updated constructor to include selected image
    public ImageAdapter(Context context, int[] images, int selectedImageResource) {
        this.context = context;
        this.images = images;
        this.selectedImageResource = selectedImageResource;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return images[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CircleImageView circleImageView;

        if (convertView == null) {
            circleImageView = new CircleImageView(context);
            circleImageView.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
        } else {
            circleImageView = (CircleImageView) convertView;
        }

        circleImageView.setImageResource(images[position]);

        // Highlight the currently selected avatar
        if (images[position] == selectedImageResource) {
            circleImageView.setBorderColor(Color.BLUE);
            circleImageView.setBorderWidth(8); // 8 pixels border width
        } else {
            circleImageView.setBorderWidth(0);
        }

        return circleImageView;
    }
}