package com.jukkanikki.plainrssreader;


import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.jukkanikki.plainrssreader.db.AppDatabase;
import com.jukkanikki.plainrssreader.model.FeedWrapper;
import com.jukkanikki.plainrssreader.util.ArticlesUtil;
import com.jukkanikki.plainrssreader.util.FileUtil;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

/**
 * Tests utility fuctions to write and read file content
 */
@RunWith(AndroidJUnit4.class)
public class FileUtilTest {

    private static final String TEST_URL = "http://www.jukkanikki.com/test.json";

    private static final String EMPTY_JSON = "{}";

    private Context context;

    @Before
    public void setUp() {
        // get context from android
        context = InstrumentationRegistry.getTargetContext();
    }

    /**
     * Write JSON to file - this test is run on android
     */
    @Test
    public void SaveJsonFileTest () {

        // create file using context, name parsed from url, and empty json as content
        File tempFile = FileUtil.createTempFile(context, TEST_URL, EMPTY_JSON);

        // get uri from file
        String uri = tempFile.toURI().toString();

        // read content
        String result = FileUtil.readContentFromUri(uri);

        // check structure
        Assert.assertNotNull(result);

        // check value
        Assert.assertEquals(EMPTY_JSON, result);
    }

}
