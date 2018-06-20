package com.wcyc.zigui2.newapp.widget;

import android.text.InputFilter;
import android.text.Spanned;

import com.wcyc.zigui2.utils.DataUtil;

/**
 * @author zzc
 * @time 2018/3/30
 */
public class EditTextLengthFilter implements InputFilter {

    public EditTextLengthFilter(int max) {
        mMax = max;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        int keep = mMax - (dest.length() - (dend - dstart));

        if (keep <= 0) {
            DataUtil.getToastShort("评论/回复文字不能超过100个字");
            return "";
        } else if (keep >= end - start) {
            return null; // keep original
        } else {
            keep += start;

            DataUtil.getToastShort("评论/回复文字不能超过100个字");
            if (Character.isHighSurrogate(source.charAt(keep - 1))) {
                --keep;
                if (keep == start) {
                    return "";
                }
            }
            return source.subSequence(start, keep);
        }
    }

    private int mMax;
}
