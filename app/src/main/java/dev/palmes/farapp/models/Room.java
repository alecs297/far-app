package dev.palmes.farapp.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;

public class Room implements Serializable {
    private String name;
    private String code;

    private int floor;
    private Optional<Integer> capacity;
    private boolean available;
    List<Event> currentEvents;
    Optional<Event> nextEvent;

    public Room(String name, String code, int floor, Optional<Integer> capacity, boolean available, List<Event> currentEvents, Optional<Event> nextEvent) {
        this.name = name;
        this.code = code;
        this.floor = floor;
        this.capacity = capacity;
        this.available = available;
        this.currentEvents = currentEvents;
        this.nextEvent = nextEvent;
    }

    public Room(JSONObject room) throws JSONException, ParseException {
        this.name = room.getString("name");
        this.code = room.getString("code");
        this.floor = room.getInt("floor");
        this.capacity = room.isNull("capacity") ? Optional.empty() : Optional.of(room.getInt("capacity"));
        this.available = room.getBoolean("available");
        this.currentEvents = new ArrayList<>();
        this.nextEvent = !room.getJSONObject("nextEvent").isNull("event") ? Optional.of(new Event(room.getJSONObject("nextEvent"))) : Optional.empty();

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

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
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

    public String getUntilString() {
        if (this.isAvailable()) {
            if (getNextEvent().isPresent()) {
                return calendar(getNextEvent().get().getStart());
            } else {
                return "No events scheduled";
            }
        } else {
            Date last = getCurrentEvents().get(0).getEnd();
            for (Event event : getCurrentEvents()) {
                if (event.getEnd().after(last)) {
                    last = event.getEnd();
                }
            }
            return calendar(last);
        }
    }

    // https://stackoverflow.com/a/46526159/13200376
    static DateTimeFormatter HOUR_FORMAT = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);

    static DateTimeFormatter MDY_FORMAT = DateTimeFormatter.ofPattern("M/d/yyyy");

    private static String calendar(Date date) {
        ZonedDateTime dt = ZonedDateTime.ofInstant(date.toInstant(), TimeZone.getTimeZone("GMT+4").toZoneId());
        StringBuilder sb = new StringBuilder();

        // check difference in days from today, considering just the date (ignoring the hours)
        long days = ChronoUnit.DAYS.between(LocalDate.now(), dt.toLocalDate());
        if (days == 0) { // today
            sb.append("Today ");
        } else if (days == 1) { // tomorrow
            sb.append("Tomorrow ");
        } else if (days == -1) { // yesterday
            sb.append("Yesterday ");
        } else if (days > 0 && days < 7) { // next week
            sb.append(dt.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH)).append(" ");
        } else if (days < 0 && days > -7) { // last week
            sb.append("Last ").append(dt.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH)).append(" ");
        }

        if (Math.abs(days) < 7) {  // difference is less than a week, append current time
            sb.append("at ").append(dt.format(HOUR_FORMAT));
        } else { // more than a week of difference
            sb.append(dt.format(MDY_FORMAT));
        }

        return sb.toString();
    }
}

