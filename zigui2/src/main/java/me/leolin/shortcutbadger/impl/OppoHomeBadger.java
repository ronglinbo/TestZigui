package me.leolin.shortcutbadger.impl;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import me.leolin.shortcutbadger.ShortcutBadgeException;
import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * @author xiehua
 */
public class OppoHomeBadger extends ShortcutBadger {

    private static final String LOG_TAG = OppoHomeBadger.class.getSimpleName();
    boolean isSupportedBade = true;
    Context mContext;
    public OppoHomeBadger(Context context) {
        super(context);
        mContext = context;
    }
    @Override
    @TargetApi(11)
    protected void executeBadge(int badgeCount) throws ShortcutBadgeException {
        if(isSupportedBade) {
            String launcherClassName = getLauncherClassName(mContext);
            if (launcherClassName == null) {
                Log.d(LOG_TAG, "Main activity is null");
                return;
            }
            changeOPPOBadge(mContext,badgeCount);
        }
    }

    public static void changeOPPOBadge(Context paramContext,int count) {
        try{
            Bundle localBundle = new Bundle();
            localBundle.putInt("app_badge_count",count);
            paramContext.getContentResolver()
                    .call(Uri.parse("content://com.android.badge/badge"),
                            "setAppBadgeCount",null,localBundle);
            return;
        } catch(Exception localException){
            localException.printStackTrace();
            return;
        }
    }

    private static String getLauncherClassName(Context context) {
        PackageManager packageManager = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        // To limit the components this Intent will resolve to, by setting an
        // explicit package name.
        intent.setPackage(context.getPackageName());
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        // All Application must have 1 Activity at least.
        // Launcher activity must be found!
        ResolveInfo info = packageManager
                .resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);

        // get a ResolveInfo containing ACTION_MAIN, CATEGORY_LAUNCHER
        // if there is no Activity which has filtered by CATEGORY_DEFAULT
        if (info == null) {
            info = packageManager.resolveActivity(intent, 0);
        }

        return info.activityInfo.name;
    }
}