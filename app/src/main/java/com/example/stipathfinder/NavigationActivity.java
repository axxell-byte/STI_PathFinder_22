package com.example.stipathfinder;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.godot.game.GodotApp;

public class NavigationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Start Godot activity
        Intent godotIntent = new Intent(this, GodotApp.class);
        startActivity(godotIntent);

        // Close this activity
        finish();
    }
}
