package dev.kai.mgdg008;

import android.content.Context;

import com.android.volley.RequestQueue;

public class Volley  {
    private static RequestQueue MRQ;
    private Volley() {}
    public static void init(Context context) {
        MRQ = com.android.volley.toolbox.Volley.newRequestQueue(context);
    }
    public static RequestQueue getRequestQueue() {
        if (MRQ != null) {
            return MRQ;
        } else {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }
}
