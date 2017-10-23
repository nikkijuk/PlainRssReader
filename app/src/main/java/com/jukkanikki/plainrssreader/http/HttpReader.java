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

    public static OkHttpClient client = new OkHttpClient();

    public static String getData(String urlString) {


        // Commented out plain java implementation
        // This code will be later removed, now it shows how much cognitive load it requires

        /*
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection)url.openConnection();

            if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String line;
                while((line = r.readLine()) != null) {
                    sb.append(line);
                }
                return sb.toString();
            }
        } catch (IOException e) {
            // in case of emergency please allow logging
            // e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return ""; // RETURN EMPTY
        */

        // implementation whith okHttp library
        // clean and simple api makes it easy to understand what here happens

        try {
            Request request = new Request.Builder()
                    .url(urlString)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            }
        } catch (IOException e) {
            // in case of emergency please allow logging
            // e.printStackTrace();
        }
        return ""; // RETURN EMPTY

    }

}
