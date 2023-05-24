package dev.palmes.farapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;

import dev.palmes.farapp.models.Room;


public class FindActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = findViewById(R.id.toolbar);

        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button refreshButton = findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(v -> {
            updateRoom();
        });

        updateRoom();

    }

    public void updateRoom() {
        TextView errorText = findViewById(R.id.errorText);
        FragmentContainerView container = findViewById(R.id.fragment_container_view);

        RequestsManager requestsManager = new RequestsManager(this);
        try {
            requestsManager.getOne("find", response -> {
                try {
                    Room room = new Room(response);

                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    Fragment fragment = RoomFragment.newInstance(room);
                    ft.replace(R.id.fragment_container_view, fragment);
                    ft.commit();

                    errorText.setVisibility(TextView.GONE);
                    container.setVisibility(TextView.VISIBLE);
                } catch (JSONException | ParseException e) {
                    throw new RuntimeException(e);
                }
            }, error -> {
                errorText.setText("Could not find a room.");
                errorText.setVisibility(TextView.VISIBLE);
                container.setVisibility(TextView.GONE);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}