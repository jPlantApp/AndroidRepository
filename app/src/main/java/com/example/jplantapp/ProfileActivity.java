package com.example.jplantapp;

import static com.example.jplantapp.ThemeKeeper.*;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends BasicActivity {

    private SharedPreferences userPreferences;
    private BottomNavigationView bottomNavigationView;

    // User info
    private boolean isLoggedIn;
    private TextView givenNameTextView;
    private TextView emailTextView;
    private ImageView userPhotoImageView;
    private Button logoutButton;
    private Button themeButton;

    // ------------------------------------- //
    // ------------------------------------- //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        givenNameTextView = findViewById(R.id.givenName);
        emailTextView = findViewById(R.id.email);
        userPhotoImageView = findViewById(R.id.userPhoto);

        userPreferences = getSharedPreferences("UserCreds", MODE_PRIVATE);
        isLoggedIn = userPreferences.getBoolean("isLoggedIn", false);
        givenNameTextView.setText(userPreferences.getString("userName", ""));
        emailTextView.setText(userPreferences.getString("userEmail", ""));
        Glide.with(this).load(userPreferences.getString("userPhotoUrl", "")).into(userPhotoImageView);


        if (!isLoggedIn) {
            Intent loginIntent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_user);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_user) {
                return true;
            } else if (itemId == R.id.bottom_classification) {
                startActivity(new Intent(getApplicationContext(), ClassificationActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
                return true;
            } else if (itemId == R.id.bottom_list) {
                startActivity(new Intent(getApplicationContext(), ListActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
                return true;
            }
            return false;
        });


        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        themeButton = findViewById(R.id.changeThemeButton);
        themeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTheme();
            }
        });
    }

    private void changeTheme() {
        ThemeKeeper.rollover(this);
        recreate();
    }
    private void logoutUser() {
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.remove("isLoggedIn");
        editor.remove("userEmail");
        editor.remove("userToken");
        editor.remove("userName");
        editor.remove("userPhotoUrl");
        editor.apply();

        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
        finish();
    }
}
