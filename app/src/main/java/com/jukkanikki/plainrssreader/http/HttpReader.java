package com.jukkanikki.plainrssreader.http;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
* Simple utility class to help on reading http stream.
*/
public class HttpReader {

    public static final String EMPTY_JSON = "{}";

    public static OkHttpClient client = new OkHttpClient(); // static, initialized once

    /**
     * read http from url
     * implementation whith okHttp library
     * clean and simple api makes it easy to understand what here happens
     *
     * @param urlString
     * @return
     */
    public static String getData(String urlString) {

        try {
            // build request object
            Request request = new Request.Builder()
                    .url(urlString)
                    .build();

            // call with try-with-resources, cleanup is then automatic
            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            }
        } catch (IOException e) {
            // in case of emergency please allow logging
            // e.printStackTrace();
        }
        return EMPTY_JSON; // empty object can be parsed without problems, it just won't contain data

    }

}
