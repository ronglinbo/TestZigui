package com.dh.playback.controlbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import com.wcyc.zigui2.R;


public class RemotePortaitControlBar extends RemoteControlBar {

    public RemotePortaitControlBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void inflateLayout() {
//        LayoutInflater.from(getContext()).inflate(R.layout.play_back_protait_ctrl, this);
    }

    @Override
    protected int getExtarWidth() {
        return 0;
    }
}
