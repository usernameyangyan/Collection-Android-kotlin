package com.youngmanster.collection_kotlin.theme.material;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.youngmanster.collection_kotlin.R;
import com.youngmanster.collection_kotlin.theme.res.SkinCompatVectorResources;
import com.youngmanster.collection_kotlin.theme.widget.SkinCompatBackgroundHelper;
import com.youngmanster.collection_kotlin.theme.widget.SkinCompatHelper;
import com.youngmanster.collection_kotlin.theme.widget.SkinCompatSupportable;

import static com.youngmanster.collection_kotlin.theme.widget.SkinCompatHelper.INVALID_ID;


public class SkinMaterialCollapsingToolbarLayout extends CollapsingToolbarLayout implements SkinCompatSupportable {
    private int mContentScrimResId = INVALID_ID;
    private int mStatusBarScrimResId = INVALID_ID;
    private SkinCompatBackgroundHelper mBackgroundTintHelper;

    public SkinMaterialCollapsingToolbarLayout(Context context) {
        this(context, null);
    }

    public SkinMaterialCollapsingToolbarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkinMaterialCollapsingToolbarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CollapsingToolbarLayout, defStyleAttr,
                R.style.Widget_Design_CollapsingToolbar);
        mContentScrimResId = a.getResourceId(R.styleable.CollapsingToolbarLayout_contentScrim, INVALID_ID);
        mStatusBarScrimResId = a.getResourceId(R.styleable.CollapsingToolbarLayout_statusBarScrim, INVALID_ID);
        a.recycle();
        applyContentScrimResource();
        applyStatusBarScrimResource();
        mBackgroundTintHelper = new SkinCompatBackgroundHelper(this);
        mBackgroundTintHelper.loadFromAttributes(attrs, 0);
    }

    private void applyStatusBarScrimResource() {
        mStatusBarScrimResId = SkinCompatHelper.checkResourceId(mStatusBarScrimResId);
        if (mStatusBarScrimResId != INVALID_ID) {
            Drawable drawable = SkinCompatVectorResources.getDrawableCompat(getContext(), mStatusBarScrimResId);
            if (drawable != null) {
                setStatusBarScrim(drawable);
            }
        }
    }

    private void applyContentScrimResource() {
        mContentScrimResId = SkinCompatHelper.checkResourceId(mContentScrimResId);
        if (mContentScrimResId != INVALID_ID) {
            Drawable drawable = SkinCompatVectorResources.getDrawableCompat(getContext(), mContentScrimResId);
            if (drawable != null) {
                setContentScrim(drawable);
            }
        }
    }

    @Override
    public void applySkin() {
        applyContentScrimResource();
        applyStatusBarScrimResource();
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.applySkin();
        }
    }

}
