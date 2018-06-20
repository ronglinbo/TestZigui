package com.wcyc.zigui2.core;

/**
 * Created by 章豪 on 2017/6/26.
 */

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.wcyc.zigui2.R;

public class ADFilterTool {
    public static boolean hasAd(Context context, String url) {
        Resources res = context.getResources();
        String[] adUrls = res.getStringArray(R.array.adBlockUrl);
        for (String adUrl : adUrls) {
            if (url.contains(adUrl)) {
                return true;
            }
        }
        return false;
    }
}