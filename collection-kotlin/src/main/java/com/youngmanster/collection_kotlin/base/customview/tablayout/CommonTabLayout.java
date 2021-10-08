package com.youngmanster.collection_kotlin.base.customview.tablayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.viewpager.widget.ViewPager;

import com.youngmanster.collection_kotlin.R;
import com.youngmanster.collection_kotlin.utils.DisplayUtils;
import com.youngmanster.collection_kotlin.utils.LogUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import static androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP;

/**
 * 自定义可修改下划线长度和增加下滑线动画
 * Created by yangyan
 * on 2018/6/26.
 */
@ViewPager.DecorView
public class CommonTabLayout extends HorizontalScrollView implements ViewPager.OnPageChangeListener {

    /**
     * TabLayout模式设置
     */
    public static final int MODE_SCROLLABLE = 0;
    public static final int MODE_FIXED = 1;

    @RestrictTo(LIBRARY_GROUP)
    @IntDef(value = {MODE_SCROLLABLE, MODE_FIXED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {
    }

    private int mMode;

    /**
     * 下划线显示的位置，只针对横线
     */
    public static final int GRAVITY_BOTTOM = 0;
    public static final int GRAVITY_TOP = 1;

    @RestrictTo(LIBRARY_GROUP)
    @IntDef(flag = true, value = {GRAVITY_BOTTOM, GRAVITY_TOP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TabGravity {
    }

    private int mIndicatorGravity;

    /**
     * 下划线形状，分为三角形、块状显示、横线
     */
    public static final int INDICATOR_LINE = 0;
    public static final int INDICATOR_TRIANGLE = 1;
    public static final int INDICATOR_BLOCK = 2;

    @RestrictTo(LIBRARY_GROUP)
    @IntDef(flag = true, value = {INDICATOR_LINE, INDICATOR_TRIANGLE, INDICATOR_BLOCK})
    @Retention(RetentionPolicy.SOURCE)
    public @interface IndicatorStyle {
    }

    private int mIndicatorStyle;

    private Context mContext;
    private LinearLayout mTabsContainer;
    private ViewPager mViewPager;
    private int mTabCount;
    private int mCurrentTab;
    private float mCurrentPositionOffset;
    private Paint mTextPaint;
    private Paint mTrianglePaint;
    private Path mTrianglePath = new Path();
    /**
     * 用于绘制显示器
     */
    private Rect mIndicatorRect = new Rect();
    /**
     * 用于实现滚动居中
     */
    private Rect mTabRect = new Rect();
    private int mLastScrollX;
    private GradientDrawable mIndicatorDrawable = new GradientDrawable();

    /**
     * indicator
     */
    public int tabIndicatorColor;
    public float tabIndicatorHeight;
    public float tabIndicatorWidth;
    public float tabWidth;
    public float tabPadding;

    public float tabIndicatorCornerRadius;
    public float tabIndicatorMarginLeft;
    public float tabIndicatorMarginTop;
    public float tabIndicatorMarginRight;
    public float tabIndicatorMarginBottom;
    public int tabTextSelectColor;
    public int tabTextUnSelectColor;
    public float tabTextSize;
    private boolean mTextAllCaps;

    private OnTabSelectListener mListener;
    private List<View> customViews = new ArrayList<>();

    public void setOnTabSelectListener(OnTabSelectListener listener) {
        this.mListener = listener;
    }


    public CommonTabLayout(Context context) {
        this(context, null);
    }

    public CommonTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        setHorizontalScrollBarEnabled(false);
        mTabsContainer = new LinearLayout(context);
        super.addView(mTabsContainer, 0, new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CommonTabLayout,
                defStyleAttr, 0);

        tabIndicatorHeight = a.getDimensionPixelSize(R.styleable.CommonTabLayout_tab_c_tabIndicatorHeight, DisplayUtils.Companion.dip2px(mIndicatorStyle == INDICATOR_TRIANGLE ? 4 : (mIndicatorStyle == INDICATOR_BLOCK ? -1 : 2)));
        tabIndicatorWidth = a.getDimensionPixelSize(R.styleable.CommonTabLayout_tab_c_tabIndicatorWidth, 0);
        tabWidth = a.getDimensionPixelSize(R.styleable.CommonTabLayout_tab_c_tabWidth, 0);

        mIndicatorStyle = a.getInt(R.styleable.CommonTabLayout_tab_c_indicator_style, INDICATOR_LINE);
        tabIndicatorColor = a.getColor(R.styleable.CommonTabLayout_tab_c_tabIndicatorColor, 0);
        tabIndicatorCornerRadius = a.getDimension(R.styleable.CommonTabLayout_tab_c_indicator_corner, DisplayUtils.Companion.dip2px(mIndicatorStyle == INDICATOR_BLOCK ? -1 : 0));
        tabIndicatorMarginLeft = a.getDimension(R.styleable.CommonTabLayout_tab_c_indicator_marginLeft, DisplayUtils.Companion.dip2px(mIndicatorStyle == INDICATOR_BLOCK ? 10 : 0));
        tabIndicatorMarginTop = a.getDimension(R.styleable.CommonTabLayout_tab_c_indicator_marginTop, DisplayUtils.Companion.dip2px(mIndicatorStyle == INDICATOR_BLOCK ? 7 : 0));
        tabIndicatorMarginRight = a.getDimension(R.styleable.CommonTabLayout_tab_c_indicator_marginRight, DisplayUtils.Companion.dip2px(mIndicatorStyle == INDICATOR_BLOCK ? 10 : 0));
        tabIndicatorMarginBottom = a.getDimension(R.styleable.CommonTabLayout_tab_c_indicator_marginBottom, DisplayUtils.Companion.dip2px(mIndicatorStyle == INDICATOR_BLOCK ? 7 : 0));
        tabPadding = a.getDimension(R.styleable.CommonTabLayout_tab_c_padding, DisplayUtils.Companion.dip2px(10));
        mIndicatorGravity = a.getInt(R.styleable.CommonTabLayout_tab_c_indicator_gravity, GRAVITY_BOTTOM);
        tabTextSelectColor = a.getColor(R.styleable.CommonTabLayout_tab_c_tabSelectedTextColor, 0);
        tabTextUnSelectColor = a.getColor(R.styleable.CommonTabLayout_tab_c_tabTextColor, 0);
        tabTextSize = a.getDimension(R.styleable.CommonTabLayout_tab_c_tabTextSize, DisplayUtils.Companion.dip2px(14));
        mTextAllCaps = a.getBoolean(R.styleable.CommonTabLayout_tab_c_textAllCaps, false);

        mMode = a.getInt(R.styleable.CommonTabLayout_tab_c_tabMode, MODE_SCROLLABLE);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTrianglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }


    public void setupWithViewPager(@Nullable ViewPager vp,int tabWidth) {
        this.tabWidth=tabWidth;
        if (vp == null || vp.getAdapter() == null) {
            throw new IllegalStateException("ViewPager or ViewPager adapter can not be null!");
        }

        this.mViewPager = vp;

        this.mViewPager.removeOnPageChangeListener(this);
        this.mViewPager.addOnPageChangeListener(this);
        notifyDataSetChanged();
    }

    public void setupWithViewPager(@Nullable ViewPager vp) {
        if (vp == null || vp.getAdapter() == null) {
            throw new IllegalStateException("ViewPager or ViewPager adapter can not be null!");
        }

        this.mViewPager = vp;

        this.mViewPager.removeOnPageChangeListener(this);
        this.mViewPager.addOnPageChangeListener(this);
        notifyDataSetChanged();
    }

    public void setCustomViews(List<View> customViews) {
        this.customViews.clear();
        this.customViews = customViews;
    }

    public void notifyDataSetChanged() {
        mTabsContainer.removeAllViews();
        if (mMode == MODE_FIXED) {
            if(tabWidth>0){
                mTabsContainer.getLayoutParams().width = (int) tabWidth;
            }else{
                mTabsContainer.getLayoutParams().width = DisplayUtils.Companion.getScreenWidthPixels();
            }
        }

        if (customViews != null && customViews.size() > 0) {
            for (int i = 0; i < customViews.size(); i++) {
                addCustomTab(i, customViews.get(i));
            }

        } else {
            this.mTabCount = mViewPager.getAdapter().getCount();
            View tabView;
            for (int i = 0; i < mTabCount; i++) {
                tabView = View.inflate(mContext, R.layout.collection_library_design_layout_tab_text, null);
                CharSequence pageTitle = mViewPager.getAdapter().getPageTitle(i);
                addTab(i, pageTitle.toString(), tabView);
            }
        }


        updateTabStates();
    }

    /**
     * 创建并添加tab
     */
    private void addTab(final int position, String title, View tabView) {
        TextView tv_tab_title = tabView.findViewById(R.id.tv_tab_title);
        if (tv_tab_title != null) {
            if (title != null) tv_tab_title.setText(title);
        }

        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mTabsContainer.indexOfChild(v);
                if (position != -1) {
                    if (mViewPager.getCurrentItem() != position) {
                        mViewPager.setCurrentItem(position, false);

                        if (mListener != null) {
                            mListener.onTabSelect(position);
                        }
                    }
                }
            }
        });

        /** 每一个Tab的布局参数 */
        LinearLayout.LayoutParams lp_tab;
        if (mMode == MODE_FIXED) {
            if(tabWidth>0){
                lp_tab = new LinearLayout.LayoutParams((int) (tabWidth / mTabCount), LayoutParams.MATCH_PARENT);
            }else{
                lp_tab = new LinearLayout.LayoutParams(DisplayUtils.Companion.getScreenWidthPixels() / mTabCount, LayoutParams.MATCH_PARENT);
            }
        } else {
            lp_tab = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        }
        mTabsContainer.addView(tabView, position, lp_tab);
    }


    /**
     * 创建并添加tab
     */
    private void addCustomTab(final int position, View tabView) {

        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mTabsContainer.indexOfChild(v);
                if (position != -1) {
                    if (mViewPager.getCurrentItem() != position) {
                        mViewPager.setCurrentItem(position, false);

                        if (mListener != null) {
                            mListener.onTabSelect(position);
                        }
                    }
                }
            }
        });

        /** 每一个Tab的布局参数 */
        LinearLayout.LayoutParams lp_tab;
        if (mMode == MODE_FIXED) {
            if(tabWidth>0){
                lp_tab = new LinearLayout.LayoutParams((int) (tabWidth / mTabCount), LayoutParams.MATCH_PARENT);
            }else{
                lp_tab = new LinearLayout.LayoutParams(DisplayUtils.Companion.getScreenWidthPixels() / customViews.size(), LayoutParams.MATCH_PARENT);
            }
        } else {
            lp_tab = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        }
        mTabsContainer.addView(tabView, position, lp_tab);
    }


    private void updateTabStates() {

        if (customViews == null || customViews.size() <= 0) {
            for (int i = 0; i < mTabCount; i++) {
                View v = mTabsContainer.getChildAt(i);
                TextView tv_tab_title = v.findViewById(R.id.tv_tab_title);
                if (tv_tab_title != null) {
                    tv_tab_title.setTextColor(i == mCurrentTab ? tabTextSelectColor : tabTextUnSelectColor);
                    tv_tab_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
                    tv_tab_title.setPadding((int) tabPadding, 0, (int) tabPadding, 0);
                    if (mTextAllCaps) {
                        tv_tab_title.setText(tv_tab_title.getText().toString().toUpperCase());
                    }
                }
            }
        } else {
            for (int i = 0; i < customViews.size(); i++) {
                View v = mTabsContainer.getChildAt(i);

                boolean isSelect = i == mCurrentTab;
                v.setSelected(isSelect);
            }
        }
    }

    public void setIndicatorGravity(@TabGravity int gravity) {
        if (gravity != mIndicatorGravity) {
            mIndicatorGravity = gravity;
            invalidate();
        }
    }

    @TabGravity
    public int getIndicatorGravity() {
        return mIndicatorGravity;
    }

    public void setTabMode(@Mode int mode) {
        if (mode != mMode) {
            mMode = mode;
            invalidate();
        }
    }

    @Mode
    public int getTabMode() {
        return mMode;
    }

    public void setIndicatorStyle(@IndicatorStyle int indicatorStyle) {
        if (indicatorStyle != mIndicatorStyle) {
            mIndicatorStyle = indicatorStyle;
            invalidate();
        }
    }

    @IndicatorStyle
    public int getIndicatorStyle() {
        return mIndicatorStyle;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        this.mCurrentTab = position;
        this.mCurrentPositionOffset = positionOffset;
        scrollToCurrentTab();
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
        updateTabSelection(position);
        if (mListener != null) {
            mListener.onTabSelect(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * HorizontalScrollView滚到当前tab,并且居中显示
     */
    private void scrollToCurrentTab() {
        if (customViews != null && customViews.size() > 0) {
            mTabCount = customViews.size();
        }

        if (mTabCount <= 0) {
            return;
        }

        int offset = (int) (mCurrentPositionOffset * mTabsContainer.getChildAt(mCurrentTab).getWidth());
        /**当前Tab的left+当前Tab的Width乘以positionOffset*/
        int newScrollX = mTabsContainer.getChildAt(mCurrentTab).getLeft() + offset;

        if (mCurrentTab > 0 || offset > 0) {
            /**HorizontalScrollView移动到当前tab,并居中*/
            newScrollX -= getWidth() / 2 - getPaddingLeft();
            calcIndicatorRect();
            newScrollX += ((mTabRect.right - mTabRect.left) / 2);
        }

        if (newScrollX != mLastScrollX) {
            mLastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }
    }

    private float margin;

    private void calcIndicatorRect() {

        if (customViews == null || customViews.size() <= 0) {
            View currentTabView = mTabsContainer.getChildAt(this.mCurrentTab);
            float left = currentTabView.getLeft();
            float right = currentTabView.getRight();

            if (mIndicatorStyle == INDICATOR_LINE && tabIndicatorWidth == 0) {
                TextView tab_title = currentTabView.findViewById(R.id.tv_tab_title);
                mTextPaint.setTextSize(tabTextSize);
                float textWidth = mTextPaint.measureText(tab_title.getText().toString());
                margin = (right - left - textWidth) / 2;
            }

            if (this.mCurrentTab < mTabCount - 1) {
                View nextTabView = mTabsContainer.getChildAt(this.mCurrentTab + 1);
                float nextTabLeft = nextTabView.getLeft();
                float nextTabRight = nextTabView.getRight();

                left = left + mCurrentPositionOffset * (nextTabLeft - left);
                right = right + mCurrentPositionOffset * (nextTabRight - right);

                if (mIndicatorStyle == INDICATOR_LINE && tabIndicatorWidth == 0) {
                    TextView next_tab_title = nextTabView.findViewById(R.id.tv_tab_title);
                    mTextPaint.setTextSize(tabTextSize);
                    float nextTextWidth = mTextPaint.measureText(next_tab_title.getText().toString());
                    float nextMargin = (nextTabRight - nextTabLeft - nextTextWidth) / 2;
                    margin = margin + mCurrentPositionOffset * (nextMargin - margin);
                }
            }

            mIndicatorRect.left = (int) left;
            mIndicatorRect.right = (int) right;
            if (mIndicatorStyle == INDICATOR_LINE && tabIndicatorWidth == 0) {
                mIndicatorRect.left = (int) (left + margin - 1);
                mIndicatorRect.right = (int) (right - margin - 1);
            }

            mTabRect.left = (int) left;
            mTabRect.right = (int) right;

            if (tabIndicatorWidth <= 0) {
            } else {//indicatorWidth大于0时,圆角矩形以及三角形
                float indicatorLeft = currentTabView.getLeft() + (currentTabView.getWidth() - tabIndicatorWidth) / 2;

                if (this.mCurrentTab < mTabCount - 1) {
                    View nextTab = mTabsContainer.getChildAt(this.mCurrentTab + 1);
                    indicatorLeft = indicatorLeft + mCurrentPositionOffset * (currentTabView.getWidth() / 2 + nextTab.getWidth() / 2);
                }

                mIndicatorRect.left = (int) indicatorLeft;
                mIndicatorRect.right = (int) (mIndicatorRect.left + tabIndicatorWidth);
            }
        }
    }


    private void updateTabSelection(int position) {

        if (customViews != null && customViews.size() > 0) {
            for (int i = 0; i < customViews.size(); ++i) {
                View tabView = mTabsContainer.getChildAt(i);
                final boolean isSelect = i == position;


                tabView.setSelected(isSelect);
            }
        } else {
            for (int i = 0; i < mTabCount; ++i) {
                View tabView = mTabsContainer.getChildAt(i);
                final boolean isSelect = i == position;


                TextView tab_title = tabView.findViewById(R.id.tv_tab_title);
                if (tab_title != null) {
                    tab_title.setTextColor(isSelect ? tabTextSelectColor : tabTextUnSelectColor);
                }
            }
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode() || mTabCount <= 0) {
            return;
        }

        int height = getHeight();
        int paddingLeft = getPaddingLeft();

        calcIndicatorRect();
        if (mIndicatorStyle == INDICATOR_TRIANGLE) {
            if (tabIndicatorHeight > 0) {
                mTrianglePaint.setColor(tabIndicatorColor);
                mTrianglePath.reset();
                mTrianglePath.moveTo(paddingLeft + mIndicatorRect.left, height);
                mTrianglePath.lineTo(paddingLeft + mIndicatorRect.left / 2 + mIndicatorRect.right / 2, height - tabIndicatorHeight);
                mTrianglePath.lineTo(paddingLeft + mIndicatorRect.right, height);
                mTrianglePath.close();
                canvas.drawPath(mTrianglePath, mTrianglePaint);
            }
        } else if (mIndicatorStyle == INDICATOR_BLOCK) {
            if (tabIndicatorHeight < 0) {
                tabIndicatorHeight = height - tabIndicatorMarginTop - tabIndicatorMarginBottom;
            }
            if (tabIndicatorHeight > 0) {

                mIndicatorDrawable.setColor(tabIndicatorColor);
                mIndicatorDrawable.setBounds((int) (paddingLeft +tabIndicatorMarginLeft + mIndicatorRect.left),
                        (int) tabIndicatorMarginTop, (int) (paddingLeft + mIndicatorRect.right - tabIndicatorMarginRight),
                        (int) (height - tabIndicatorMarginBottom));
                mIndicatorDrawable.setCornerRadius(tabIndicatorCornerRadius);
                mIndicatorDrawable.draw(canvas);
            }
        } else {
            if (tabIndicatorHeight > 0) {
                mIndicatorDrawable.setColor(tabIndicatorColor);

                if (mIndicatorGravity == GRAVITY_BOTTOM) {
                    mIndicatorDrawable.setBounds(paddingLeft + (int) tabIndicatorMarginLeft + mIndicatorRect.left,
                            height - (int) tabIndicatorHeight - (int) tabIndicatorMarginBottom,
                            paddingLeft + mIndicatorRect.right - (int) tabIndicatorMarginRight,
                            height - (int) tabIndicatorMarginBottom);
                } else {
                    mIndicatorDrawable.setBounds(paddingLeft + (int) tabIndicatorMarginLeft + mIndicatorRect.left,
                            (int) tabIndicatorMarginTop,
                            paddingLeft + mIndicatorRect.right - (int) tabIndicatorMarginRight,
                            (int) tabIndicatorHeight + (int) tabIndicatorMarginTop);
                }
                mIndicatorDrawable.setCornerRadius(tabIndicatorCornerRadius);
                mIndicatorDrawable.draw(canvas);
            }
        }
    }
}
