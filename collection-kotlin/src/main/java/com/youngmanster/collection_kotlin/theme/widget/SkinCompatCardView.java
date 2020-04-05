package com.youngmanster.collection_kotlin.theme.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.cardview.widget.CardView;

import com.youngmanster.collection_kotlin.R;
import com.youngmanster.collection_kotlin.theme.res.SkinCompatResources;

import static com.youngmanster.collection_kotlin.theme.widget.SkinCompatHelper.INVALID_ID;

public class SkinCompatCardView extends CardView implements SkinCompatSupportable {
    private static final int[] COLOR_BACKGROUND_ATTR = {android.R.attr.colorBackground};
    private int mThemeColorBackgroundResId = INVALID_ID;
    private int mBackgroundColorResId = INVALID_ID;

    public SkinCompatCardView(Context context) {
        this(context, null);
    }

    public SkinCompatCardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkinCompatCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CardView, defStyleAttr,
                R.style.CardView);
        if (a.hasValue(R.styleable.CardView_cardBackgroundColor)) {
            mBackgroundColorResId = a.getResourceId(R.styleable.CardView_cardBackgroundColor, INVALID_ID);
        } else {
            final TypedArray aa = getContext().obtainStyledAttributes(COLOR_BACKGROUND_ATTR);
            mThemeColorBackgroundResId = aa.getResourceId(0, INVALID_ID);
            aa.recycle();
        }
        a.recycle();
        applyBackgroundColorResource();
    }

    private void applyBackgroundColorResource() {
        mBackgroundColorResId = SkinCompatHelper.checkResourceId(mBackgroundColorResId);
        mThemeColorBackgroundResId = SkinCompatHelper.checkResourceId(mThemeColorBackgroundResId);
        ColorStateList backgroundColor;
        if (mBackgroundColorResId != INVALID_ID) {
            backgroundColor = SkinCompatResources.getColorStateList(getContext(), mBackgroundColorResId);
            setCardBackgroundColor(backgroundColor);
        } else if (mThemeColorBackgroundResId != INVALID_ID) {
            int themeColorBackground = SkinCompatResources.getColor(getContext(), mThemeColorBackgroundResId);
            final float[] hsv = new float[3];
            Color.colorToHSV(themeColorBackground, hsv);
            backgroundColor = ColorStateList.valueOf(hsv[2] > 0.5f
                    ? getResources().getColor(R.color.cardview_light_background)
                    : getResources().getColor(R.color.cardview_dark_background));
            setCardBackgroundColor(backgroundColor);
        }
    }

    @Override
    public void applySkin() {
        applyBackgroundColorResource();
    }

}
