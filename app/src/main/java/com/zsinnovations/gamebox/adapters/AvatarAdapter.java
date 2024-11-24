package com.zsinnovations.gamebox.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import androidx.cardview.widget.CardView;
import com.zsinnovations.gamebox.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class AvatarAdapter extends BaseAdapter {
    private final Context context;
    private final int[] avatars;
    private final int selectedAvatar;
    private final LayoutInflater inflater;

    public AvatarAdapter(Context context, int[] avatars, int selectedAvatar) {
        this.context = context;
        this.avatars = avatars;
        this.selectedAvatar = selectedAvatar;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return avatars.length;
    }

    @Override
    public Object getItem(int position) {
        return avatars[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_avatar, parent, false);
            holder = new ViewHolder();
            holder.cardView = convertView.findViewById(R.id.avatarCardView);
            holder.imageView = convertView.findViewById(R.id.avatarImage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.imageView.setImageResource(avatars[position]);

        // Highlight selected avatar
        if (avatars[position] == selectedAvatar) {
            holder.cardView.setCardBackgroundColor(context.getColor(R.color.avatar_selected_background));
            holder.cardView.getWidth();
            holder.cardView.getSolidColor();
        } else {
            holder.cardView.setCardBackgroundColor(context.getColor(R.color.avatar_background));
            holder.cardView.getWidth();
        }

        return convertView;
    }

    private static class ViewHolder {
        CardView cardView;
        CircleImageView imageView;
    }
}