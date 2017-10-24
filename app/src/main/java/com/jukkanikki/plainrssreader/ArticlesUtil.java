package com.jukkanikki.plainrssreader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.jukkanikki.plainrssreader.adapters.FeedAdapter;
import com.jukkanikki.plainrssreader.model.FeedWrapper;

public class ArticlesUtil {

    private ArticlesUtil () {} // helper class, not possible to instantiate

    /**
     * Binds feed to view by creating needed adapter, setting it and requesting state refresh
     *
     * @param context
     * @param view
     * @param feed
     */
    public static void bindView(Context context, RecyclerView view, FeedWrapper feed) {
        // TODO: creating new adapter after each update of values might not be right thing to do ..
        FeedAdapter adapter = new FeedAdapter(feed, context); // create adapter
        view.setAdapter(adapter); // set adapter
        adapter.notifyDataSetChanged(); // inform adapter that it should update
    }

    /**
     * Convert json to pojos using gson
     * @param json json
     * @return FeedWrapper as root of object graph
     */
    public static FeedWrapper convertToObjects(String json) {
        // TODO: GSON is created during each conversion - reusing possibilities need to be analyzed
        return new Gson().fromJson(json, FeedWrapper.class); // json -> pojos
    }


}
