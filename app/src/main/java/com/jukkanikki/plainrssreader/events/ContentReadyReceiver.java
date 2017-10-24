package com.jukkanikki.plainrssreader.events;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.jukkanikki.plainrssreader.util.ArticlesUtil;
import com.jukkanikki.plainrssreader.model.FeedWrapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URI;

// TODO: Implement receiver
// receiver should force refresh of content:
//   reading data from persistent store,
//   filling / creating recycler view adapter and
//   notifying view recycler view that content should be repainted

public class ContentReadyReceiver extends BroadcastReceiver {

    private static final String TAG = "ContentReadyReceiver";

    private RecyclerView articleView;

    /**
     * Inject article view
     * @param articleView article view
     */
    public void setArticleView(RecyclerView articleView) {
        this.articleView = articleView;
    }

    /**
     * This method is called when the BroadcastReceiver is receiving
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        // ready to update article view
        // TODO: add updating + reference to articles view

        String uri = intent.getStringExtra(Events.CONTENT_URL); // get uri from extras
        Log.d(TAG,"Content ready :"+uri.toString());

        String data = readArticlesFile(uri); //Get the text file
        Log.d(TAG,"read data from temp :"+data.substring(0,100));

        // Fill list of articles
        FeedWrapper feed = ArticlesUtil.convertToObjects(data);
        ArticlesUtil.bindView(context ,articleView, feed);
    }

    /**
     * Read articles from file
     */
    private String readArticlesFile(String uri) {

        // todo: refactor to try with resources
        try {
            File file = new File(new URI(uri));

            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuilder text = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            br.close();

            return text.toString();

        } catch (Exception e) {
            Log.e(TAG,"error reading result :"+e.getMessage());
            e.printStackTrace();
        }

        return ""; // EMPTY if error

    }
}
