package com.lulu.surfaceviewdemo;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author zhanglulu on 2020/6/15.
 * for
 */
class FileUtils {
    public static String readAssetFile(Context context, String fileName) {
        InputStream is = null;

        try {
            is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            String var4 = new String(buffer, "UTF-8");
            return var4;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return "";
    }
}
