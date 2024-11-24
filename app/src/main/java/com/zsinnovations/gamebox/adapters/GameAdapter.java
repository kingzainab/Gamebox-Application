package com.zsinnovations.gamebox.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.zsinnovations.gamebox.R;

public class GameAdapter extends BaseAdapter {
    private static final String TAG = "GameAdapter";
    private final Context context;
    private final String[] gameNames;
    private final int[] gameImages;

    public GameAdapter(Context context, String[] gameNames, int[] gameImages) {
        this.context = context;
        this.gameNames = gameNames;
        this.gameImages = gameImages;
        Log.d(TAG, "Adapter created with " + gameNames.length + " items");
    }

    @Override
    public int getCount() {
        return gameNames.length;
    }

    @Override
    public Object getItem(int position) {
        return gameNames[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
            holder = new ViewHolder();
            holder.gameName = convertView.findViewById(R.id.gameName);
            holder.gameImage = convertView.findViewById(R.id.gameImage);
            convertView.setTag(holder);
            Log.d(TAG, "Created new view for position " + position);
        } else {
            holder = (ViewHolder) convertView.getTag();
            Log.d(TAG, "Reusing view for position " + position);
        }

        try {
            holder.gameName.setText(gameNames[position]);
            holder.gameImage.setImageResource(gameImages[position]);
        } catch (Exception e) {
            Log.e(TAG, "Error setting data for position " + position, e);
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView gameName;
        ImageView gameImage;
    }
}