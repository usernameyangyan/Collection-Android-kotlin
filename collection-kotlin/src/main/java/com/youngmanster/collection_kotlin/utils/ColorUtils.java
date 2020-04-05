package com.youngmanster.collection_kotlin.utils;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.ColorStateList;

import androidx.core.content.ContextCompat;

/**
 * Created by yangy
 * 2020-02-25
 * Describe:
 */
public class ColorUtils {

    public static ColorStateList createColorStateList(Context context,int selected, int pressed, int normal) {
        int[] colors = new int[]{ContextCompat.getColor(context,selected), ContextCompat.getColor(context,pressed), ContextCompat.getColor(context,normal)};
        int[][] states = new int[3][];
        states[0] = new int[]{android.R.attr.state_selected};
        states[1] = new int[]{android.R.attr.state_pressed};
        states[2] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }
}
