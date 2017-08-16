

package com.tc.client;

import android.util.Base64;

import java.io.*;

/**
 * Created by zhangwen@reconova.com on 2017/5/23.
 */
public class FileUtils {

    /**
     * 
     *
     * @param filePath
     * @return
     */
    public static byte[] readFileAsByteArray(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }

            buffer = bos.toByteArray();
            fis.close();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    public static String base64Encode(byte[] bytes) {
        return Base64.encodeToString(bytes,Base64.NO_WRAP);//base64Encode().getEncoder().encodeToString(bytes);
    }

    public static String readFileAsBase64String(String filePath) {
        byte[] bytes = readFileAsByteArray(filePath);
        return base64Encode(bytes);
    }
}
