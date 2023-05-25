package dev.palmes.farapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button aboutButton = findViewById(R.id.button_about);
        aboutButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
        });

        Button findButton = findViewById(R.id.button_find);
        findButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, FindActivity.class));
        });

        Button listButton = findViewById(R.id.button_list);
        listButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ListActivity.class));
        });
    }
}