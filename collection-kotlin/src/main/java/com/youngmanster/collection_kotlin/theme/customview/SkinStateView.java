package com.youngmanster.collection_kotlin.theme.customview;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.youngmanster.collection_kotlin.R;
import com.youngmanster.collection_kotlin.base.customview.tablayout.CommonTabLayout;
import com.youngmanster.collection_kotlin.base.stateview.StateView;
import com.youngmanster.collection_kotlin.theme.res.SkinCompatResources;
import com.youngmanster.collection_kotlin.theme.widget.SkinCompatBackgroundHelper;
import com.youngmanster.collection_kotlin.theme.widget.SkinCompatSupportable;
import com.youngmanster.collection_kotlin.utils.LogUtils;

import org.w3c.dom.Text;

import static com.youngmanster.collection_kotlin.theme.widget.SkinCompatHelper.INVALID_ID;

/**
 * Created by yangy
 * 2020-04-05
 * Describe:
 */
public class SkinStateView extends StateView implements SkinCompatSupportable {

    private int color;
    private int txtColor;

    public SkinStateView(Context context) {
        this(context, null);
    }

    public SkinStateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkinStateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StateView,
                0, 0);

        color = a.getResourceId(R.styleable.StateView_bgColor, 0);
        txtColor= a.getResourceId(R.styleable.StateView_tipTextColor, 0);
        a.recycle();

        applyItemColorResource();
    }



    private void applyItemColorResource() {
        if (color != INVALID_ID) {
            setBgColor(SkinCompatResources.getColor(getContext(), color));

            this.getMDisConnectView().setBackgroundColor(SkinCompatResources.getColor(getContext(), color));

            this.getMEmptyView().setBackgroundColor(SkinCompatResources.getColor(getContext(), color));

            this.getMLoadingView().setBackgroundColor(SkinCompatResources.getColor(getContext(), color));

        }

        if(txtColor!=INVALID_ID){
            if(getEmptyTv()!=null){
                getEmptyTv().setTextColor(SkinCompatResources.getColor(getContext(), txtColor));
            }

            if(getLoadingTv()!=null){
                getLoadingTv().setTextColor(SkinCompatResources.getColor(getContext(), txtColor));
            }

            if(getDisConnectTv()!=null){
                getDisConnectTv().setTextColor(SkinCompatResources.getColor(getContext(), txtColor));
            }
        }

    }

    @Override
    public void applySkin() {
        applyItemColorResource();
    }
}
