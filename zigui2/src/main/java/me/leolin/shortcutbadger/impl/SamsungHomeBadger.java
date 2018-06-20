package me.leolin.shortcutbadger.impl;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import me.leolin.shortcutbadger.ShortcutBadgeException;
import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created with IntelliJ IDEA.
 * User: leolin
 * Date: 2013/11/14
 * Time: 下午7:15
 * To change this template use File | Settings | File Templates.
 */
public class SamsungHomeBadger extends ShortcutBadger {
    private static final String CONTENT_URI = "content://com.sec.badge/apps?notify=true";

    public SamsungHomeBadger(Context context) {
        super(context);
    }

    @Override
    protected void executeBadge(int badgeCount) throws ShortcutBadgeException {
        int ret;
        Uri mUri = Uri.parse(CONTENT_URI);
        ContentResolver contentResolver = mContext.getContentResolver();
        Cursor cursor = contentResolver.query(mUri, new String[]{"_id",},
                "package=?", new String[]{getContextPackageName()}, null);
        if (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            ContentValues contentValues = new ContentValues();
            contentValues.put("badgecount", badgeCount);
            ret = contentResolver.update(mUri, contentValues, "_id=?", new String[]{String.valueOf(id)});
            System.out.println("executeBadge:"+ret);
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("package", getContextPackageName());
            contentValues.put("class", getEntryActivityName());
            contentValues.put("badgecount", badgeCount);
            Uri uri = contentResolver.insert(mUri, contentValues);
            System.out.println("executeBadge uri:"+uri);
        }

        // 获取你当前的应用
        String launcherClassName = getLauncherClassName(mContext);
        if (launcherClassName == null) {
            return;
        }
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", badgeCount);
        intent.putExtra("badge_count_package_name", mContext.getPackageName());
        intent.putExtra("badge_count_class_name", launcherClassName);
        mContext.sendBroadcast(intent);
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
