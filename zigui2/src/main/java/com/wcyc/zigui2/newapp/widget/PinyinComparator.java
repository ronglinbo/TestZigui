package com.wcyc.zigui2.newapp.widget;

import com.wcyc.zigui2.newapp.bean.AllTeacherList;

import java.util.Comparator;

/**
 * Created by hbj on 2017/5/18.
 */

public class PinyinComparator implements Comparator<AllTeacherList.TeacherMap> {

    public int compare(AllTeacherList.TeacherMap o1, AllTeacherList.TeacherMap o2) {
        if (o1.getSortLetters().equals("@")
                || o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#")
                || o2.getSortLetters().equals("@")) {
            return 1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }


}

