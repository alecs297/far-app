package dev.palmes.farapp.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Room {
    private String name;
    private String code;
    private Optional<Integer> capacity;
    private boolean available;
    List<Event> currentEvents;
    Optional<Event> nextEvent;

    public Room(String name, String code, Optional<Integer> capacity, boolean available, List<Event> currentEvents, Optional<Event> nextEvent) {
        this.name = name;
        this.code = code;
        this.capacity = capacity;
        this.available = available;
        this.currentEvents = currentEvents;
        this.nextEvent = nextEvent;
    }

    public Room(JSONObject room) throws JSONException, ParseException {
        this.name = room.getString("name");
        this.code = room.getString("code");
        this.capacity = Optional.of(room.getInt("capacity"));
        this.available = room.getBoolean("available");
        this.currentEvents = new ArrayList<>();
        this.nextEvent = !room.getJSONObject("nextEvent").isNull("event") ? Optional.of(new Event(room.getJSONObject("nextEvent"))) : null;

        if (room.has("currentEvents")) {
            for (int i = 0; i < room.getJSONArray("currentEvents").length(); i++) {
                this.currentEvents.add(new Event(room.getJSONArray("currentEvents").getJSONObject(i)));
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Optional<Integer> getCapacity() {
        return capacity;
    }

    public void setCapacity(Optional<Integer> capacity) {
        this.capacity = capacity;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public List<Event> getCurrentEvents() {
        return currentEvents;
    }

    public void setCurrentEvents(List<Event> currentEvents) {
        this.currentEvents = currentEvents;
    }

    public Optional<Event> getNextEvent() {
        return nextEvent;
    }

    public void setNextEvent(Optional<Event> nextEvent) {
        this.nextEvent = nextEvent;
    }
}

