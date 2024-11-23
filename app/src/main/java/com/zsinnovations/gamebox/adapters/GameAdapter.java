package com.zsinnovations.gamebox.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.zsinnovations.gamebox.R;
import com.zsinnovations.gamebox.models.Game;
import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {
    private List<Game> games;
    private OnGameClickListener listener;

    public interface OnGameClickListener {
        void onGameClick(Game game);
        void onFavoriteClick(Game game);
    }

    public GameAdapter(List<Game> games, OnGameClickListener listener) {
        this.games = games;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        Game game = games.get(position);
        holder.title.setText(game.getTitle());
        holder.icon.setImageResource(game.getIconResId());
        holder.favoriteIcon.setImageResource(
                game.isFavorite() ? R.drawable.favorite_24px : R.drawable.favorite_24px
        );

        // Handle game click
        holder.itemView.setOnClickListener(v -> listener.onGameClick(game));

        // Handle favorite click
        holder.favoriteIcon.setOnClickListener(v -> listener.onFavoriteClick(game));
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public static class GameViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon, favoriteIcon;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.game_title);
            icon = itemView.findViewById(R.id.game_icon);
            favoriteIcon = itemView.findViewById(R.id.favorite_icon);
        }
    }
}
