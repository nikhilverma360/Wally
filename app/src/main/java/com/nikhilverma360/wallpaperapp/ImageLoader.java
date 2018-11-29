package com.nikhilverma360.wallpaperapp;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.List;

public class ImageLoader extends AsyncTaskLoader<List<Image>> {

    private String mUrl;
    private List<Image> cacheVariable;
    private static final String LOG_TAG = ImageLoader.class.getSimpleName();

    ImageLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }




    /**
     * Subclasses must implement this to take care of loading their data,
     * as per {@link #startLoading()}.  This is not called by clients directly,
     * but as a result of a call to {@link #startLoading()}.
     */
    @Override
    protected void onStartLoading() {

        if (cacheVariable != null) {
            deliverResult(cacheVariable);
        } else {
            forceLoad();
        }

    }


    /**
     * Called on a worker thread to perform the actual load and to return
     * the result of the load operation.
     * <p>
     * @return The result of the load operation.

     */
    @Override
    public List<Image> loadInBackground() {
        if (mUrl == null) {
           // Log.v(LOG_TAG, "uri is incorrect");
          //  Toast.makeText(getContext(), "No result found", Toast.LENGTH_LONG).show();
        Log.e(LOG_TAG, "Data is null");
            return null;
        }

       return NetworkUtils.fetchImagesData(mUrl);

    }

    /**
     * Sends the result of the load to the registered listener. Should only be called by subclasses.
     * <p>
     * Must be called from the process's main thread.
     *
     * @param data the result of the load
     */
    @Override
    public void deliverResult(List<Image> data) {
        cacheVariable = data;
        super.deliverResult(data);
    }
}
