package dev.palmes.farapp.models;

import java.util.Date;
import java.util.Optional;

public class RoomFilter {
    private Optional<Integer> date;
    private Optional<Integer> floor;
    private int capacity = 0;
    private int delta = 0;
    private Optional<Boolean> available;

    public RoomFilter(Optional<Integer> date, Optional<Integer> floor, int capacity, int delta, Optional<Boolean> available) {
        this.date = date;
        this.floor = floor;
        this.capacity = capacity;
        this.delta = delta;
        this.available = available;
    }

    public Optional<Integer> getDate() {
        return date;
    }

    public void setDate(Optional<Integer> date) {
        this.date = date;
    }

    public void setDate(int date) {
        this.date = Optional.of(date);
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
