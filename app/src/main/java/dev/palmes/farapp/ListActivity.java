package dev.palmes.farapp;

import static java.util.Map.entry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import dev.palmes.farapp.models.Room;
import dev.palmes.farapp.models.RoomFilter;

public class ListActivity extends AppCompatActivity {

    private LinkedHashMap<String, Integer> deltas = new LinkedHashMap<>();
    private LinkedHashMap<String, Integer> floors = new LinkedHashMap<>();
    private LinkedHashMap<String, Integer> availabilities = new LinkedHashMap<>();

    private StorageHelper storageHelper = new StorageHelper(this);

    private Date date = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        initDeltas();
        initFloors();
        initAvailabilities();

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Spinner deltaSpinner = findViewById(R.id.deltaPicker);
        deltaSpinner.setAdapter(getDeltaSpinnerAdapter());

        Spinner floorSpinner = findViewById(R.id.floorPicker);
        floorSpinner.setAdapter(getFloorSpinnerAdapter());

        Spinner availabilitySpinner = findViewById(R.id.availabilityPicker);
        availabilitySpinner.setAdapter(getAvailabilitySpinnerAdapter());

        EditText capacityPicker = findViewById(R.id.capacityPicker);
        capacityPicker.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                updateRooms();
                handled = true;
            }
            return handled;
        });

        ScrollView scrollView = findViewById(R.id.scrollView);
        FloatingActionButton fabJump = findViewById(R.id.fabJump);

        buildUI(storageHelper.getRoomFilter());

        findViewById(R.id.searchButton).setOnClickListener(v -> {
            updateRooms();
        });

        findViewById(R.id.resetButton).setOnClickListener(v -> {
            buildUI(new RoomFilter());
        });

        fabJump.setOnClickListener(v -> {
            scrollView.fullScroll(ScrollView.FOCUS_UP);
            scrollView.setSmoothScrollingEnabled(true);
        });

        scrollView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > 0) {
                fabJump.show();
            } else {
                fabJump.hide();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initDeltas() {
        deltas.put("None", 0);
        deltas.put("5 minutes", 5*60);
        deltas.put("15 minutes", 15*60);
        deltas.put("30 minutes", 30*60);
        deltas.put("1 hour", 60*60);
        deltas.put("2 hours", 2*60*60);
        deltas.put("3 hours", 3*60*60);
        deltas.put("6 hours", 6*60*60);
        deltas.put("12 hours", 12*60*60);
        deltas.put("1 day", 24*60*60);
    }

    private void initFloors() {
        floors.put("None", -1);
        floors.put("0", 0);
        floors.put("1", 1);
        floors.put("2", 2);
    }

    private void initAvailabilities() {
        availabilities.put("Any", -1);
        availabilities.put("Available", 1);
        availabilities.put("Occupied", 0);
    }

    private ArrayAdapter<String> getDeltaSpinnerAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.addAll(deltas.keySet());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private ArrayAdapter<String> getFloorSpinnerAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.addAll(floors.keySet());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private ArrayAdapter<String> getAvailabilitySpinnerAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.addAll(availabilities.keySet());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private Map.Entry<String, Integer> getSelectedDelta() {
        Spinner deltaSpinner = findViewById(R.id.deltaPicker);
        String selectedDelta = deltaSpinner.getSelectedItem().toString();
        return entry(selectedDelta, deltas.get(selectedDelta));
    }

    private Map.Entry<String, Integer> getSelectedFloor() {
        Spinner floorSpinner = findViewById(R.id.floorPicker);
        String selectedFloor = floorSpinner.getSelectedItem().toString();
        return entry(selectedFloor, floors.get(selectedFloor));
    }

    private Map.Entry<String, Integer> getSelectedAvailability() {
        Spinner availabilitySpinner = findViewById(R.id.availabilityPicker);
        String selectedAvailability = availabilitySpinner.getSelectedItem().toString();
        return entry(selectedAvailability, availabilities.get(selectedAvailability));
    }

    private RoomFilter buildFilter() throws NumberFormatException {

        RoomFilter roomFilter = new RoomFilter();

        try {
            roomFilter.setDelta(getSelectedDelta().getValue());
            if (getSelectedFloor().getValue() != -1) roomFilter.setFloor(getSelectedFloor().getValue());
            if (getSelectedAvailability().getValue() != -1) roomFilter.setAvailable(getSelectedAvailability().getValue() == 1);
            else roomFilter.setAvailable(Optional.empty());
            roomFilter.setCapacity(Integer.parseInt(((EditText) findViewById(R.id.capacityPicker)).getText().toString()));
        } catch (NumberFormatException e) {
            roomFilter.setCapacity(0);
        }
        return roomFilter;
    }



    private void buildUI(RoomFilter roomFilter) {

        EditText capacityPicker = findViewById(R.id.capacityPicker);
        Spinner deltaPicker = findViewById(R.id.deltaPicker);
        Spinner availabilityPicker = findViewById(R.id.availabilityPicker);
        Spinner floorPicker = findViewById(R.id.floorPicker);


        if (roomFilter.getAvailable().isPresent()) {
            int availabilityIndex = 0;
            for (Map.Entry<String, Integer> entry : availabilities.entrySet()) {
                if (entry.getValue().equals(roomFilter.getAvailable().get() ? 1:0)) {
                    availabilityPicker.setSelection(availabilityIndex);
                    break;
                }
                availabilityIndex++;
            }
        } else {
            availabilityPicker.setSelection(0);
        }

        if (roomFilter.getFloor().isPresent()) {
            int floorIndex = 0;
            for (Map.Entry<String, Integer> entry : floors.entrySet()) {
                if (entry.getValue().equals(roomFilter.getFloor().get())) {
                    floorPicker.setSelection(floorIndex);
                    break;
                }
                floorIndex++;
            }
        } else {
            floorPicker.setSelection(0);
        }

        int deltaIndex = 0;
        for (Map.Entry<String, Integer> entry : deltas.entrySet()) {
            if (entry.getValue() >= roomFilter.getDelta()) {
                deltaPicker.setSelection(deltaIndex);
                break;
            }
            deltaIndex++;
        }

        capacityPicker.setText(String.valueOf(roomFilter.getCapacity()));
    }

    public void updateRooms() { //throws JSONException {
        TextView errorText = findViewById(R.id.errorText);
        LinearLayout container = findViewById(R.id.frame_holder);
        RoomFilter roomFilter;

        roomFilter = buildFilter();
        storageHelper.setRoomFilter(roomFilter);

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        RequestsManager requestsManager = new RequestsManager(this);
        try {
            requestsManager.getMany("list", response -> {
                try {

                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    getSupportFragmentManager().getFragments().forEach(ft::remove);

                    for (int i = 0; i < response.length(); i++) {
                        Room room = new Room(response.getJSONObject(i));
                        Fragment fragment = RoomFragment.newInstance(room);
                        ft.add(R.id.frame_holder, fragment);
                    }

                    ft.commit();

                    errorText.setVisibility(TextView.GONE);
                    container.setVisibility(TextView.VISIBLE);
                } catch (JSONException | ParseException e) {
                    throw new RuntimeException(e);
                }
            }, error -> {
                errorText.setText("Could not find any matching rooms.");
                errorText.setVisibility(TextView.VISIBLE);
                container.setVisibility(TextView.GONE);
            }, roomFilter.getAsMap());
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }
}