package com.youngmanster.collection_kotlin.theme.customview;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.youngmanster.collection_kotlin.R;
import com.youngmanster.collection_kotlin.base.customview.tablayout.OutSideFrameTabLayout;
import com.youngmanster.collection_kotlin.theme.res.SkinCompatResources;
import com.youngmanster.collection_kotlin.theme.widget.SkinCompatBackgroundHelper;
import com.youngmanster.collection_kotlin.theme.widget.SkinCompatSupportable;

import static com.youngmanster.collection_kotlin.theme.widget.SkinCompatHelper.INVALID_ID;

/**
 * Created by yangy
 * 2020-04-05
 * Describe:
 */
public class SkinOutSideTabLayoutView extends OutSideFrameTabLayout implements SkinCompatSupportable {

    private int indicatorColor;
    private int textSelectColor;
    private int textUnSelectColor;
    private int barStrokeColor;
    private int barColor;

    private SkinCompatBackgroundHelper mBackgroundTintHelper;

    public SkinOutSideTabLayoutView(Context context) {
        this(context, null);
    }

    public SkinOutSideTabLayoutView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkinOutSideTabLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mBackgroundTintHelper = new SkinCompatBackgroundHelper(this);
        mBackgroundTintHelper.loadFromAttributes(attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.OutSideFrameTabLayout,
                0, 0);

        indicatorColor = a.getResourceId(R.styleable.OutSideFrameTabLayout_tab_tabIndicatorColor, 0);
        textSelectColor= a.getResourceId(R.styleable.OutSideFrameTabLayout_tab_tabSelectedTextColor,0);
        textUnSelectColor=a.getResourceId(R.styleable.OutSideFrameTabLayout_tab_tabTextColor,0);
        barStrokeColor=a.getResourceId(R.styleable.OutSideFrameTabLayout_tab_bar_stroke_color,0);
        barColor=a.getResourceId(R.styleable.OutSideFrameTabLayout_tab_bar_color,0);
        a.recycle();

        applyItemColorResource();
    }

    @Override
    public void setBackgroundResource(int resId) {
        super.setBackgroundResource(resId);

        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.onSetBackgroundResource(resId);
        }
    }


    private void applyItemColorResource() {
        if (indicatorColor != INVALID_ID) {
            this.mIndicatorColor= SkinCompatResources.getColor(getContext(), indicatorColor);
        }
        if (textSelectColor != INVALID_ID) {
            this.mTextSelectColor= SkinCompatResources.getColor(getContext(), textSelectColor);
        }
        if (textUnSelectColor != INVALID_ID) {
            this.mTextUnSelectColor= SkinCompatResources.getColor(getContext(), textUnSelectColor);
        }

        if (barStrokeColor != INVALID_ID) {
            this.mBarStrokeColor= SkinCompatResources.getColor(getContext(), barStrokeColor);
        }

        if (barColor != INVALID_ID) {
            this.mBarColor= SkinCompatResources.getColor(getContext(), barColor);
        }

    }

    @Override
    public void applySkin() {
        applyItemColorResource();
    }
}
