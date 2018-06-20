package com.wcyc.zigui2.newapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author zzc
 * @time 2017/11/29 0029
 */
public class FragmentAdapter extends FragmentPagerAdapter {
    private FragmentManager fm;
    private List<Fragment> listFragment;

    public FragmentAdapter(FragmentManager fm, List<Fragment> listFragment) {
        super(fm);
        this.fm = fm;
        this.listFragment = listFragment;
    }

    @Override
    public Fragment getItem(int position) {
        return listFragment.get(position);
    }

    @Override
    public int getCount() {
        return listFragment == null ? 0 : listFragment.size();
    }
}
