package com.youngmanster.collection_kotlin.theme.Inflater;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import com.youngmanster.collection_kotlin.theme.customview.SkinOutSideTabLayoutView;
import com.youngmanster.collection_kotlin.theme.customview.SkinStateView;
import com.youngmanster.collection_kotlin.theme.customview.SkinTabLayoutView;
import com.youngmanster.collection_kotlin.theme.widget.SkinCompatCardView;
import com.youngmanster.collection_kotlin.theme.widget.SkinNetScrollView;
import com.youngmanster.collection_kotlin.utils.LogUtils;

/**
 * Created by yangy
 * 2020-04-05
 * Describe:
 */
public class SkinCustomViewInflater implements SkinLayoutInflater{
    @Override
    public View createView(@NonNull Context context, String name, @NonNull AttributeSet attrs) {
        View view = null;

        switch (name) {
            case "com.youngmanster.collection_kotlin.base.customview.tablayout.CommonTabLayout":
                view = new SkinTabLayoutView(context, attrs);
                break;
            case "com.youngmanster.collection_kotlin.base.customview.tablayout.OutSideFrameTabLayout":
                view =new SkinOutSideTabLayoutView(context, attrs);
                break;
            case "com.youngmanster.collection_kotlin.base.stateview.StateView":
                view =new SkinStateView(context, attrs);
                break;
        }
        return view;
    }
}
