package dev.palmes.farapp;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import dev.palmes.farapp.models.RoomFilter;

public class StorageHelper {

    private Context context;
    public static final String PREFS_NAME = "dev.palmes.farapp.prefs";

    public static final String PREFS_ROOMS = "dev.palmes.farapp.prefs.rooms";
    public static final String PREFS_SEARCH = "dev.palmes.farapp.prefs.search";

    public StorageHelper(Context context) {
        this.context = context;
    }

    public RoomFilter getRoomFilter() {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if (!preferences.contains(PREFS_SEARCH)) {
            RoomFilter filter = new RoomFilter();
            setRoomFilter(filter);
            return filter;
        } else {
            JSONObject json;
            RoomFilter filter = new RoomFilter();

            try {
                json = new JSONObject(preferences.getString(PREFS_SEARCH, ""));
                filter.setDelta(json.has("delta") ? json.getInt("delta") : 0);
                if (json.has("capacity")) filter.setCapacity(json.getInt("capacity"));
                if (json.has("floor")) filter.setFloor(json.getInt("floor"));
                if (json.has("available")) filter.setAvailable(json.getBoolean("available"));

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            return filter;
        }

    }

    public void setRoomFilter(RoomFilter filter) {
        try {
            SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(PREFS_SEARCH, filter.getAsJSON().toString());
            editor.apply();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


}
