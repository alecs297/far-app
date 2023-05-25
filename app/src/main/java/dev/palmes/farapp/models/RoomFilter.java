package dev.palmes.farapp.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RoomFilter {
    private Optional<Integer> floor;
    private int capacity = 0;
    private int delta = 0;
    private Optional<Boolean> available;

    public RoomFilter(Optional<Integer> floor, int capacity, int delta, Optional<Boolean> available) {
        this.floor = floor;
        this.capacity = capacity;
        this.delta = delta;
        this.available = available;
    }

    public RoomFilter() {
        this(Optional.empty(), 0, 0, Optional.of(true));
    }

    public JSONObject getAsJSON() throws JSONException {
        JSONObject json = new JSONObject();
        if (floor.isPresent()) json.put("floor", floor.get());
        if (available.isPresent()) json.put("available", available.get());
        json.put("capacity", capacity);
        json.put("delta", delta);
        return json;
    }

    public Map<String, String> getAsMap() throws JSONException {
        Map<String, String> map = new HashMap<>();
        getAsJSON().keys().forEachRemaining(key -> {
            try {
                map.put(key, getAsJSON().getString(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        return map;
    }

    public Optional<Integer> getFloor() {
        return floor;
    }

    public void setFloor(Optional<Integer> floor) {
        this.floor = floor;
    }

    public void setFloor(int floor) {
        this.floor = Optional.of(floor);
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getDelta() {
        return delta;
    }

    public void setDelta(int delta) {
        this.delta = delta;
    }

    public Optional<Boolean> getAvailable() {
        return available;
    }

    public void setAvailable(Optional<Boolean> available) {
        this.available = available;
    }

    public void setAvailable(boolean available) {
        this.available = Optional.of(available);
    }
}
