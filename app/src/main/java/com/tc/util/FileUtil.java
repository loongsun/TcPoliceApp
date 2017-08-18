package com.tc.util;

import android.content.Context;

import java.io.File;

/**
 * Created by zhao on 17-8-17.
 */

public class FileUtil {

    public static File getSaveFile(Context context){
        File file = new File(context.getFilesDir(),"pic.jpg");
        return file;
    }

}
