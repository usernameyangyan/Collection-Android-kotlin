package com.youngmanster.collection_kotlin.base.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * Created by yangyan
 * on 2018/3/18.
 */

public class CollectionViewPager extends ViewPager {

    private boolean scrollable = true;

    public CollectionViewPager(Context context) {
        super(context);
    }

    public CollectionViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollable(boolean enable) {
        scrollable = enable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(scrollable){
            return super.onInterceptTouchEvent(ev);
        }else{
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(scrollable){
            return super.onTouchEvent(ev);
        }else{
            return false;
        }
    }
}
