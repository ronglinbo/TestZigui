package me.leolin.shortcutbadger.impl;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

import me.leolin.shortcutbadger.ShortcutBadgeException;
import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * @author xiehua
 */
public class HuaweiHomeBadger extends ShortcutBadger {

    private static final String LOG_TAG = HuaweiHomeBadger.class.getSimpleName();
    boolean isSupportedBade = true;
    public HuaweiHomeBadger(Context context) {
        super(context);
    }
    @Override
    @TargetApi(11)
    protected void executeBadge(int badgeCount) throws ShortcutBadgeException {
//        checkIsSupportedByVersion(mContext);
        if(isSupportedBade) {
            String launcherClassName = getLauncherClassName(mContext);
            if (launcherClassName == null) {
                Log.d(LOG_TAG, "Main activity is null");
                return;
            }
            try {
                Bundle localBundle = new Bundle();
                localBundle.putString("package", mContext.getPackageName());
                localBundle.putString("class", launcherClassName);
                localBundle.putInt("badgenumber", badgeCount);
                System.out.println("huawei setShortcutBadge：" + badgeCount);
                mContext.getContentResolver().call(
                        Uri.parse("content://com.huawei.android.launcher.settings/badge/"),
                        "change_badge", null, localBundle);
            }catch (Exception e){
                e.printStackTrace();
                isSupportedBade = false;
            }
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

    public boolean checkIsSupportedByVersion(Context context){
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo("com.huawei.android.launcher", 0);
            if(info.versionCode >= 63029){//emui 4.1
                isSupportedBade = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSupportedBade;
    }
}
