package com.youngmanster.collection_kotlin.theme.Inflater;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import com.youngmanster.collection_kotlin.theme.widget.SkinCompatCardView;
import com.youngmanster.collection_kotlin.theme.widget.SkinNetScrollView;
import com.youngmanster.collection_kotlin.utils.LogUtils;

/**
 * Created by yangy
 * 2020-04-05
 * Describe:
 */
public class SkinAndroidXViewInflater implements SkinLayoutInflater{
    @Override
    public View createView(@NonNull Context context, String name, @NonNull AttributeSet attrs) {
        View view = null;

        switch (name) {
            case "androidx.cardview.widget.CardView":
                view = new SkinCompatCardView(context, attrs);
                break;
            case "androidx.core.widget.NestedScrollView":
                view = new SkinNetScrollView(context, attrs);
                break;
        }
        return view;
    }
}
