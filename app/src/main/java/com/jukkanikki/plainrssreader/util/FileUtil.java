package com.jukkanikki.plainrssreader.util;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;

public class FileUtil {

    private static final String TAG = "FileUtil";

    private FileUtil () {}

    /**
     * Read content from file
     *
     * @param uri location of file
     * @return content of file
     */
    public static String readContentFromUri(String uri) {

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

    /**
     * Writes content to temp file
     *
     * file is written to temp files of android, it's not exposed to outsiders
     *
     * @param url url of content to extract name of file from
     * @param content payload
     * @return file handle
     */
    public static File createTempFile(Context context, String url, String content) {
        File file = null;

        // TODO: use try-with-resources
        try {
            String fileName = Uri.parse(url).getLastPathSegment();

            // create temp file used to keep file hidden from other apps
            file = File.createTempFile(fileName, null, context.getCacheDir());

            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(content);

            myOutWriter.close();

            fOut.flush();
            fOut.close();

        } catch (IOException e) {
            // Error while creating file
            Log.e(TAG,"error saving result :"+e.getMessage());
            e.printStackTrace();
        }

        return file;
    }

    // for testing purposes here would need to be also delete of all existing temp files
    // so that it would be possibly to start with clean state

    // it could be also good to have methods to list temp files and read last created temp file
    // but as this would be all for GREY BOX tests it's not really having priority

}
