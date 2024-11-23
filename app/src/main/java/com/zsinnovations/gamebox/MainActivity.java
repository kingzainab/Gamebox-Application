package com.zsinnovations.gamebox;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.zsinnovations.gamebox.adapters.ImageAdapter;
import com.zsinnovations.gamebox.ui.mainscreen.game;

public class MainActivity extends AppCompatActivity {
    private ImageView avatarImageView;
    private int[] predefinedImages = {
            R.drawable.a,
            R.drawable.b,
            R.drawable.c,
            R.drawable.d,
            R.drawable.e
    };
    private int currentAvatarResource = R.drawable.a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//avatar
        avatarImageView = findViewById(R.id.avatarIcon);
        avatarImageView.setImageResource(currentAvatarResource);

        avatarImageView.setOnClickListener(v -> showPredefinedImageDialog());
        if (savedInstanceState == null) {
            // Create an instance of the fragment
            game gameFragment = new game();

            // Add the fragment dynamically into the fragmentContainer
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer, gameFragment);
            transaction.commit();
        }

    }

    private void showPredefinedImageDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_predefined_images, null);

        GridView gridView = dialogView.findViewById(R.id.gridView);
        gridView.setAdapter(new ImageAdapter(this, predefinedImages, currentAvatarResource));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setTitle("Choose an Avatar")
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        gridView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            currentAvatarResource = predefinedImages[position];
            avatarImageView.setImageResource(currentAvatarResource);
            Toast.makeText(this, "Avatar updated!", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
    }


}
