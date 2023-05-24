package dev.palmes.farapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

        TextView debug = findViewById(R.id.debug);

        RequestsManager requestsManager = new RequestsManager(this);
        try {
            requestsManager.getOne("find", response -> {
                debug.setText(response.toString());
                try {
                    System.out.println(new Room(response));
                } catch (JSONException | ParseException e) {
                    throw new RuntimeException(e);
                }
            }, error -> {
                debug.setText(error.toString());
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}