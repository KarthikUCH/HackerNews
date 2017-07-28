package com.pg.demo.hackernews.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by karthikeyan on 27/7/17.
 */

public class FileUtil {

    public static File getFileFromPath(Object obj, String fileName) throws Exception
    {
        ClassLoader classLoader = obj.getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        return new File(resource.getPath());
    }

    public static String getFileContentFromPath(Object obj, String fileName) throws Exception {
        ClassLoader classLoader = obj.getClass().getClassLoader();
        InputStream resources = classLoader.getResourceAsStream(fileName);
        return convertStreamToString(resources);
    }


    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }
}
