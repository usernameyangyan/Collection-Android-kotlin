package com.youngmanster.collection_kotlin.theme.customview;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.youngmanster.collection_kotlin.R;
import com.youngmanster.collection_kotlin.base.customview.tablayout.CommonTabLayout;
import com.youngmanster.collection_kotlin.theme.res.SkinCompatResources;
import com.youngmanster.collection_kotlin.theme.widget.SkinCompatBackgroundHelper;
import com.youngmanster.collection_kotlin.theme.widget.SkinCompatHelper;
import com.youngmanster.collection_kotlin.theme.widget.SkinCompatSupportable;
import com.youngmanster.collection_kotlin.utils.LogUtils;

import static com.youngmanster.collection_kotlin.theme.widget.SkinCompatHelper.INVALID_ID;

/**
 * Created by yangy
 * 2020-04-05
 * Describe:
 */
public class SkinTabLayoutView extends CommonTabLayout implements SkinCompatSupportable {

    private int indicatorColor;
    private int textSelectColor;
    private int textUnSelectColor;

    private SkinCompatBackgroundHelper mBackgroundTintHelper;

    public SkinTabLayoutView(Context context) {
        this(context, null);
    }

    public SkinTabLayoutView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkinTabLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mBackgroundTintHelper = new SkinCompatBackgroundHelper(this);
        mBackgroundTintHelper.loadFromAttributes(attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CommonTabLayout,
                0, 0);

        indicatorColor = a.getResourceId(R.styleable.CommonTabLayout_tab_c_tabIndicatorColor, 0);
        textSelectColor= a.getResourceId(R.styleable.CommonTabLayout_tab_c_tabSelectedTextColor,0);
        textUnSelectColor=a.getResourceId(R.styleable.CommonTabLayout_tab_c_tabTextColor,0);
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
            this.tabIndicatorColor= SkinCompatResources.getColor(getContext(), indicatorColor);
        }
        if (textSelectColor != INVALID_ID) {
            this.tabTextSelectColor= SkinCompatResources.getColor(getContext(), textSelectColor);
        }
        if (textUnSelectColor != INVALID_ID) {
            this.tabTextUnSelectColor= SkinCompatResources.getColor(getContext(), textUnSelectColor);
        }

    }

    @Override
    public void applySkin() {
        applyItemColorResource();
    }
}
