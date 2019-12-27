package com.m.openthedoorapp.interfaces;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Ma7MouD on 6/24/2018.
 */

public interface RecyclerTouchHelperListner {

    void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
}
