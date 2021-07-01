package com.example.trivia.controler;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class AppControler extends Application {
    //we have to extent applicationn and in manifest file   we have to use android name

    private static AppControler instance;
    private RequestQueue requestQueue;
   // private ImageLoader imageLoader;
   // private static Context ctx;

   /*
    these things are not used in this app

    private AppControler(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();

        imageLoader = new ImageLoader(requestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    } */

    public static synchronized AppControler getInstance() {
        /*if (instance == null) {
            instance = new AppControler(context);
        } */
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

     /*public ImageLoader getImageLoader() {
        return imageLoader;
    } */

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
