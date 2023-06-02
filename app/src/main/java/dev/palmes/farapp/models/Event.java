package dev.palmes.farapp.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Event {
    private Date start;
    private Date end;

    public Event(Date start, Date end) {
        this.start = start;
        this.end = end;
    }

    public Event(JSONObject event) throws JSONException, ParseException {
        SimpleDateFormat parser = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);

        if (event.isNull("event")) {
            this.start = parser.parse(event.getString("start"));
            this.end = parser.parse(event.getString("end"));
            return;
        }

        this.start = parser.parse(event.getJSONObject("event").getString("start"));
        this.end = parser.parse(event.getJSONObject("event").getString("end"));
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
}
