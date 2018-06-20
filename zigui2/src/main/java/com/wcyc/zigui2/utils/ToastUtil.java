package com.wcyc.zigui2.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wcyc.zigui2.R;


/**
 * Toast统一管理类
 *
 * Created by ronglinbo on 2016/6/30.
 */
public class ToastUtil {

    // Toast
    private static Toast toast;

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message)
    {
        if (null == toast)
        {
            toast = Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT);
            // toast.setGravity(Gravity.CENTER, 0, 0);
        }
        else
        {
            toast.setText(message);
        }
        toast.show();
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, int message)
    {
        if (null == toast)
        {
            toast = Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT);
            // toast.setGravity(Gravity.CENTER, 0, 0);
        }
        else
        {
            toast.setText(message);
        }
        toast.show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message)
    {

        if (null == toast)
        {
            toast = Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_LONG);
            // toast.setGravity(Gravity.CENTER, 0, 0);
        }
        else
        {
            toast.setText(message);
        }
        toast.show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, int message)
    {
        if (null == toast)
        {
            toast = Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_LONG);
            // toast.setGravity(Gravity.CENTER, 0, 0);
        }
        else
        {
            toast.setText(message);
        }
        toast.show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, CharSequence message, int duration)
    {
        if (null == toast)
        {
            toast = Toast.makeText(context.getApplicationContext(), message, duration);
            // toast.setGravity(Gravity.CENTER, 0, 0);
        }
        else
        {
            toast.setText(message);
        }
        toast.show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, int message, int duration)
    {
        if (null == toast)
        {
            toast = Toast.makeText(context.getApplicationContext(), message, duration);
            // toast.setGravity(Gravity.CENTER, 0, 0);
        }
        else
        {
            toast.setText(message);
        }
        toast.show();
    }

//    /**
//     * 自定义显示带图片的toast
//     *
//     * @param context
//     * @param message
//     * @param duration
//     */
//    public static void showWithPic(Context context, String message, int duration )
//    {
//        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
//        View layout = inflater.inflate(R.layout.toast_with_pic,null);
//        ImageView image = (ImageView) layout
//                .findViewById(R.id.tvImageToast);
//        image.setImageResource(R.drawable.warning);
//        TextView text = (TextView) layout.findViewById(R.id.tvTextToast);
//        text.setText(message);
//        Toast toast = new Toast(context.getApplicationContext());
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        toast.setDuration(duration);
//        toast.setView(layout);
//        toast.show();
//    }

    /** Hide the toast, if any. */
    public static void hideToast()
    {
        if (null != toast)
        {
            toast.cancel();
        }
    }
}
