package com.pg.demo.hackernews.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by karthikeyan on 27/7/17.
 */

public class FileUtil {

    public static String getFileFromPath(Object obj, String fileName) throws Exception {
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
