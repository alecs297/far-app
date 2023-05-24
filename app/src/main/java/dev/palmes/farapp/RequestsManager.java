package dev.palmes.farapp;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RequestsManager {
    private final RequestQueue queue;
    private static final String url = "https://find-a-room.palmes.dev/api/v1/";

    private static String buildQuery(Map<String, String> query) {
        String queryString = "";
        if (!query.isEmpty()) {
            queryString += "?";
            for (String key : query.keySet()) {
                queryString += key + "=" + query.get(key) + "&";
            }
            queryString = queryString.substring(0, queryString.length() - 1);
        }
        return queryString;
    }

    public RequestsManager(Context context) {
        this.queue = Volley.newRequestQueue(context);
    }

    public void getOne(String endpoint, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, Map<String, String> query) throws IOException {
        URL url = new URL(RequestsManager.url + endpoint + buildQuery(query));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url.toString(), null, listener, errorListener);
        queue.add(request);
    }

    public void getMany(String endpoint, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener, Map<String, String> query) throws IOException {
        URL url = new URL(RequestsManager.url + endpoint + buildQuery(query));
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url.toString(), null, listener, errorListener);
        queue.add(request);
    }

    public void getOne(String endpoint, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) throws IOException {
        getOne(endpoint, listener, errorListener, new HashMap<>());
    }

    public void getMany(String endpoint, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) throws IOException {
        getMany(endpoint, listener, errorListener, new HashMap<>());
    }

}
