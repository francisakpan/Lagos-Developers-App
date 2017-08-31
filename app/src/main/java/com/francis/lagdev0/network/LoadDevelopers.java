package com.francis.lagdev0.network;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Created by Francis on 09/08/2017.
 * A custom loader class for AsyncTask Loader
 */

public class LoadDevelopers extends AsyncTaskLoader {
    private String mUrl;
    private String mDir;
    private String mSortBy;

    /**
     *
     * @param context application context
     * @param Url JSON_RESPONSE_URL
     * @param dir gives direction on whether to load using the developer json url or repository json url
     * @param sortBy set the arraylist to sort in ascending or descending order
     */
    public LoadDevelopers(Context context, String Url, String dir, String sortBy){
        super(context);
        mUrl = Url;
        mDir = dir;
        mSortBy = sortBy;
    }

    @Override
    public Object loadInBackground() {
        if (mUrl == null){
            return null;
        }

        QueryDevs queryDevs = new QueryDevs(getContext());
        return queryDevs.fetchJSONData(mUrl, mDir, mSortBy);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
