package com.jukkanikki.plainrssreader.util;

import com.google.gson.Gson;
import com.jukkanikki.plainrssreader.model.FeedWrapper;

/**
 * Operations for json manipulation
 */
public class JsonUtil {

    private JsonUtil () {} // helper class, not possible to instantiate

    /**
     * Convert json to pojos using gson
     * @param json json
     * @return FeedWrapper as root of object graph
     */
    public static FeedWrapper convertToObjects(String json) {

        // GSON is created during each conversion - reusing possibilities need to be analyzed
        return new Gson().fromJson(json, FeedWrapper.class); // json -> pojos
    }

}
