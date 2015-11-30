package com.weatherapp;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import org.json.JSONObject;
import android.support.v4.app.Fragment;

abstract class AbstractVolleyRequest extends Fragment implements Response.Listener, Response.ErrorListener {
    public static final String REQUEST_TAG = "MainVolleyActivity";

    public void start(String url, RequestQueue mQueue) {
        final JSONObjectRequest jsonRequest = new JSONObjectRequest(Request.Method
                .GET, url,
                new JSONObject(), this, this);
        jsonRequest.setTag(REQUEST_TAG);

        mQueue.add(jsonRequest);
    }

    public void stop(RequestQueue mQueue) {
        if (mQueue != null) {
            mQueue.cancelAll(REQUEST_TAG);
        }
    }
}
