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
import com.zsinnovations.gamebox.utils.FavoritesManager;

public class GameAdapter extends BaseAdapter {
    private static final String TAG = "GameAdapter";
    private final Context context;
    private final String[] gameNames;
    private final int[] gameImages;
    private final FavoritesManager favoritesManager;

    public GameAdapter(Context context, String[] gameNames, int[] gameImages) {
        this.context = context;
        this.gameNames = gameNames;
        this.gameImages = gameImages;
        // Pass the context to FavoritesManager
        this.favoritesManager = FavoritesManager.getInstance(context);
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
            holder.favouriteStartIcon = convertView.findViewById(R.id.favourite_star);
            holder.favouriteStartIconFilled = convertView.findViewById(R.id.favourite_star_filled);
            convertView.setTag(holder);
            Log.d(TAG, "Created new view for position " + position);
        } else {
            holder = (ViewHolder) convertView.getTag();
            Log.d(TAG, "Reusing view for position " + position);
        }

        String currentGameName = gameNames[position];

        // Update star visibility based on favorite status
        updateFavoriteIcons(holder, currentGameName);

        holder.favouriteStartIcon.setOnClickListener(v -> {
            favoritesManager.addFavorite(currentGameName);
            updateFavoriteIcons(holder, currentGameName);
            notifyDataSetChanged();
        });

        holder.favouriteStartIconFilled.setOnClickListener(v -> {
            favoritesManager.removeFavorite(currentGameName);
            updateFavoriteIcons(holder, currentGameName);
            notifyDataSetChanged();
        });

        try {
            holder.gameName.setText(currentGameName);
            holder.gameImage.setImageResource(gameImages[position]);
        } catch (Exception e) {
            Log.e(TAG, "Error setting data for position " + position, e);
        }

        return convertView;
    }

    private void updateFavoriteIcons(ViewHolder holder, String gameName) {
        boolean isFavorite = favoritesManager.getFavoriteGames().contains(gameName);
        holder.favouriteStartIcon.setVisibility(isFavorite ? View.GONE : View.VISIBLE);
        holder.favouriteStartIconFilled.setVisibility(isFavorite ? View.VISIBLE : View.GONE);
    }

    private static class ViewHolder {
        TextView gameName;
        ImageView gameImage;
        ImageView favouriteStartIcon;
        ImageView favouriteStartIconFilled;
    }
}