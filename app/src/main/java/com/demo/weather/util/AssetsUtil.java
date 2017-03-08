package com.demo.weather.util;

import org.w3c.dom.Text;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by zhongjy on 2017/3/8.
 */

public class AssetsUtil {

    public static final String TAG = "AssetsUtil";

    public static String getAssetFile(Context context, String fileName) {
        if (!TextUtils.isEmpty(fileName)) {
            AssetManager s = context.getAssets();
            InputStream is = null;
            try {
                is = s.open(fileName);
                byte[] buffer = new byte[is.available()];
                is.read(buffer);
                String json = new String(buffer, "utf-8");
                is.close();
                return json;
            } catch (IOException e) {
                Log.e(TAG, "getAssetFile occur Exception:" + e.getMessage());
            }
        }
        return null;
    }

}