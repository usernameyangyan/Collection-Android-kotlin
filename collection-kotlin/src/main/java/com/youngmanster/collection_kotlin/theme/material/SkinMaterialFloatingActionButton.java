package com.youngmanster.collection_kotlin.theme.material;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.youngmanster.collection_kotlin.R;
import com.youngmanster.collection_kotlin.theme.res.SkinCompatResources;
import com.youngmanster.collection_kotlin.theme.widget.SkinCompatHelper;
import com.youngmanster.collection_kotlin.theme.widget.SkinCompatImageHelper;
import com.youngmanster.collection_kotlin.theme.widget.SkinCompatSupportable;

import static com.youngmanster.collection_kotlin.theme.widget.SkinCompatHelper.INVALID_ID;

public class SkinMaterialFloatingActionButton extends FloatingActionButton implements SkinCompatSupportable {
    private int mRippleColorResId = INVALID_ID;
    private int mBackgroundTintResId = INVALID_ID;

    private SkinCompatImageHelper mImageHelper;

    public SkinMaterialFloatingActionButton(Context context) {
        this(context, null);
    }

    public SkinMaterialFloatingActionButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkinMaterialFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.FloatingActionButton, defStyleAttr,
                R.style.Widget_Design_FloatingActionButton);
        mBackgroundTintResId = a.getResourceId(R.styleable.FloatingActionButton_backgroundTint, INVALID_ID);
        mRippleColorResId = a.getResourceId(R.styleable.FloatingActionButton_rippleColor, INVALID_ID);
        a.recycle();
        applyBackgroundTintResource();
        applyRippleColorResource();

        mImageHelper = new SkinCompatImageHelper(this);
        mImageHelper.loadFromAttributes(attrs, defStyleAttr);
    }

    private void applyBackgroundTintResource() {
        mBackgroundTintResId = SkinCompatHelper.checkResourceId(mBackgroundTintResId);
        if (mBackgroundTintResId != INVALID_ID) {
            setBackgroundTintList(SkinCompatResources.getColorStateList(getContext(), mBackgroundTintResId));
        }
    }

    private void applyRippleColorResource() {
        mRippleColorResId = SkinCompatHelper.checkResourceId(mRippleColorResId);
        if (mRippleColorResId != INVALID_ID) {
            setRippleColor(SkinCompatResources.getColor(getContext(), mRippleColorResId));
        }
    }

    @Override
    public void applySkin() {
        applyBackgroundTintResource();
        applyRippleColorResource();
        if (mImageHelper != null) {
            mImageHelper.applySkin();
        }
    }

}
