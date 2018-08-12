package com.example.xushuailong.mygrocerystore.scan.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.nio.CharBuffer;
import java.security.MessageDigest;
import java.util.Date;
import java.util.Locale;

public class FileManager {
    private static final String CacheRootEx = Environment.getExternalStorageDirectory() + "/WccJCCache/";
    private static final String rootIn = "/data/data/com.wochacha.johnsoncounter/files/";


    private static final String imagesDirEx = CacheRootEx;


    private static final String imagesDirIn = rootIn + "Images/";



    public static boolean mkdir(String dir) {
        try {
            return new File(dir).mkdir();
        } catch (Exception e) {
            return false;
        }
    }



    public static String getExImagesPath() {
        mkdir(imagesDirEx);
        return imagesDirEx;
    }


    public static String getInnerImagesPath() {
        mkdir(imagesDirIn);
        return imagesDirIn;
    }










    /**
     * read entire file as strings
     *
     * @param filename
     * @return result or 255
     */

    public static String r(String filename) {
        try {
            FileReader reader = new FileReader(filename);
            StringBuilder sb = new StringBuilder();
            CharBuffer buf = CharBuffer.allocate(1024);
            while (reader.read(buf) >= 0) {
                buf.flip();
                sb.append(buf.toString());
            }
            reader.close();
            return sb.toString();
        } catch (Exception e) {
            return "255";
        }
    }


}
