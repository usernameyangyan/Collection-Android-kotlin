package com.youngmanster.collection_kotlin.theme;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.youngmanster.collection_kotlin.data.DataManager;
import com.youngmanster.collection_kotlin.theme.Inflater.SkinAndroidXViewInflater;
import com.youngmanster.collection_kotlin.theme.Inflater.SkinAppCompatViewInflater;
import com.youngmanster.collection_kotlin.theme.Inflater.SkinCustomViewInflater;
import com.youngmanster.collection_kotlin.theme.Inflater.SkinLayoutInflater;
import com.youngmanster.collection_kotlin.theme.Inflater.SkinWrapper;
import com.youngmanster.collection_kotlin.theme.load.SkinBuildInLoader;
import com.youngmanster.collection_kotlin.theme.load.SkinNoneLoader;
import com.youngmanster.collection_kotlin.theme.material.SkinMaterialViewInflater;
import com.youngmanster.collection_kotlin.theme.observe.SkinObservable;
import com.youngmanster.collection_kotlin.theme.res.SkinCompatResources;
import com.youngmanster.collection_kotlin.theme.utils.SkinConstants;

import java.util.ArrayList;
import java.util.List;

public class ThemeManager extends SkinObservable {
    public static final int SKIN_LOADER_STRATEGY_NONE = -1;
    public static final int SKIN_LOADER_STRATEGY_BUILD_IN = 0;
    private static volatile ThemeManager sInstance;
    private final Object mLock = new Object();
    private final Context mAppContext;
    private boolean mLoading = false;
    private List<SkinWrapper> mWrappers = new ArrayList<>();
    private List<SkinLayoutInflater> mInflaters = new ArrayList<>();
    private SparseArray<SkinLoaderStrategy> mStrategyMap = new SparseArray<>();
    private boolean mSkinAllActivityEnable = true;
    private boolean mSkinWindowBackgroundColorEnable = true;

    /**
     * 皮肤包加载监听.
     */
    public interface SkinLoaderListener {
        /**
         * 开始加载.
         */
        void onStart();

        /**
         * 加载成功.
         */
        void onSuccess();

        /**
         * 加载失败.
         *
         * @param errMsg 错误信息.
         */
        void onFailed(String errMsg);
    }

    /**
     * 皮肤包加载策略.
     */
    public interface SkinLoaderStrategy {
        /**
         * 加载皮肤包.
         *
         * @param context  {@link Context}
         * @param skinName 皮肤包名称.
         * @return 加载成功，返回皮肤包名称；失败，则返回空。
         */
        String loadSkinInBackground(Context context, String skinName);

        /**
         * 根据应用中的资源ID，获取皮肤包相应资源的资源名.
         *
         * @param context  {@link Context}
         * @param skinName 皮肤包名称.
         * @param resId    应用中需要换肤的资源ID.
         * @return 皮肤包中相应的资源名.
         */
        String getTargetResourceEntryName(Context context, String skinName, int resId);

        /**
         * 开发者可以拦截应用中的资源ID，返回对应color值。
         *
         * @param context  {@link Context}
         * @param skinName 皮肤包名称.
         * @param resId    应用中需要换肤的资源ID.
         * @return 获得拦截后的颜色值，添加到ColorStateList的defaultColor中。不需要拦截，则返回空
         */
        ColorStateList getColor(Context context, String skinName, int resId);

        /**
         * 开发者可以拦截应用中的资源ID，返回对应ColorStateList。
         *
         * @param context  {@link Context}
         * @param skinName 皮肤包名称.
         * @param resId    应用中需要换肤的资源ID.
         * @return 返回对应ColorStateList。不需要拦截，则返回空
         */
        ColorStateList getColorStateList(Context context, String skinName, int resId);

        /**
         * 开发者可以拦截应用中的资源ID，返回对应Drawable。
         *
         * @param context  {@link Context}
         * @param skinName 皮肤包名称.
         * @param resId    应用中需要换肤的资源ID.
         * @return 返回对应Drawable。不需要拦截，则返回空
         */
        Drawable getDrawable(Context context, String skinName, int resId);

        /**
         * @return 皮肤包加载策略类型.
         */
        int getType();
    }

    /**
     *
     * @param context
     * @return
     */
    private static ThemeManager init(Context context) {
        if (sInstance == null) {
            synchronized (ThemeManager.class) {
                if (sInstance == null) {
                    sInstance = new ThemeManager(context);
                }
            }
        }
        return sInstance;
    }

    public static ThemeManager getInstance() {
        return sInstance;
    }

    /**
     *
     * @param application 应用Application.
     * @return
     */
    public static ThemeManager with(Application application) {
        init(application);
        SkinActivityLifecycle.init(application);
        sInstance.addInflater(new SkinAppCompatViewInflater())
                .addInflater(new SkinMaterialViewInflater())
                .addInflater(new SkinAndroidXViewInflater())
                .addInflater(new SkinCustomViewInflater())
                .loadSkin();
        return sInstance;
    }

    private ThemeManager(Context context) {
        mAppContext = context.getApplicationContext();
        initLoaderStrategy();
    }

    private void initLoaderStrategy() {
        mStrategyMap.put(SKIN_LOADER_STRATEGY_NONE, new SkinNoneLoader());
        mStrategyMap.put(SKIN_LOADER_STRATEGY_BUILD_IN, new SkinBuildInLoader());
    }

    public Context getContext() {
        return mAppContext;
    }

    /**
     * 添加皮肤包加载策略.
     *
     * @param strategy 自定义加载策略
     * @return
     */
    public ThemeManager addStrategy(SkinLoaderStrategy strategy) {
        mStrategyMap.put(strategy.getType(), strategy);
        return this;
    }

    public SparseArray<SkinLoaderStrategy> getStrategies() {
        return mStrategyMap;
    }

    /**
     * 自定义View换肤时，可选择添加一个{@link SkinLayoutInflater}
     *
     * @return
     */
    public ThemeManager addInflater(SkinLayoutInflater inflater) {
        if (inflater instanceof SkinWrapper) {
            mWrappers.add((SkinWrapper) inflater);
        }
        mInflaters.add(inflater);
        return this;
    }

    public List<SkinWrapper> getWrappers() {
        return mWrappers;
    }

    public List<SkinLayoutInflater> getInflaters() {
        return mInflaters;
    }

    /**
     * 获取当前皮肤包.
     *
     * @return
     */
    public String getCurSkinName() {
        return DataManager.DataForSharePreferences.Companion.getObject(SkinConstants.KEY_SKIN_NAME,"");
    }

    /**
     * 恢复默认主题，使用应用自带资源.
     */
    public void restoreDefaultTheme() {
        loadSkin("", SKIN_LOADER_STRATEGY_NONE);
    }


    boolean isSkinAllActivityEnable() {
        return mSkinAllActivityEnable;
    }

    boolean isSkinWindowBackgroundEnable() {
        return mSkinWindowBackgroundColorEnable;
    }


    /**
     * 加载皮肤包.
     *
     * @param skinName 皮肤包名称.
     * @param strategy 皮肤包加载策略.
     * @return
     */
    private AsyncTask loadSkin(String skinName, int strategy) {
        return loadSkin(skinName, null, strategy);
    }


    /**
     * 加载皮肤包.
     *
     * @param skinName 皮肤包名称.
     * @param listener 皮肤包加载监听.
     * @param strategy 皮肤包加载策略.
     * @return
     */
    public AsyncTask loadSkin(String skinName, SkinLoaderListener listener, int strategy) {
        SkinLoaderStrategy loaderStrategy = mStrategyMap.get(strategy);
        if (loaderStrategy == null) {
            return null;
        }
        return new SkinLoadTask(listener, loaderStrategy).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, skinName);
    }

    private AsyncTask loadSkin() {
        String skin = DataManager.DataForSharePreferences.Companion.getObject(SkinConstants.KEY_SKIN_NAME,"");
        int strategy= DataManager.DataForSharePreferences.Companion.getObject(SkinConstants.KEY_SKIN_STRATEGY,ThemeManager.SKIN_LOADER_STRATEGY_NONE);
        if(TextUtils.isEmpty(skin) || strategy == SKIN_LOADER_STRATEGY_NONE) {
            return null;
        }
        return loadSkin(skin, null, strategy);
    }

    @SuppressLint("StaticFieldLeak")
    private class SkinLoadTask extends AsyncTask<String, Void, String> {
        private final SkinLoaderListener mListener;
        private final SkinLoaderStrategy mStrategy;

        SkinLoadTask(@Nullable SkinLoaderListener listener, @NonNull SkinLoaderStrategy strategy) {
            mListener = listener;
            mStrategy = strategy;
        }

        @Override
        protected void onPreExecute() {
            if (mListener != null) {
                mListener.onStart();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            synchronized (mLock) {
                while (mLoading) {
                    try {
                        mLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mLoading = true;
            }
            try {
                if (params.length == 1) {
                    String skinName = mStrategy.loadSkinInBackground(mAppContext, params[0]);
                    if (TextUtils.isEmpty(skinName)) {
                        SkinCompatResources.getInstance().reset(mStrategy);
                        return "";
                    }
                    return params[0];
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            SkinCompatResources.getInstance().reset();
            return null;
        }

        @Override
        protected void onPostExecute(String skinName) {
            synchronized (mLock) {
                // skinName 为""时，恢复默认皮肤
                if (skinName != null) {
                    DataManager.DataForSharePreferences.Companion.saveObject(SkinConstants.KEY_SKIN_NAME,skinName);
                    DataManager.DataForSharePreferences.Companion.saveObject(SkinConstants.KEY_SKIN_STRATEGY,mStrategy.getType());
                    notifyUpdateSkin();
                    if (mListener != null) {
                        mListener.onSuccess();
                    }
                } else {
                    DataManager.DataForSharePreferences.Companion.saveObject(SkinConstants.KEY_SKIN_NAME,"");
                    DataManager.DataForSharePreferences.Companion.saveObject(SkinConstants.KEY_SKIN_STRATEGY,SKIN_LOADER_STRATEGY_NONE);
                    if (mListener != null) {
                        mListener.onFailed("皮肤资源获取失败");
                    }
                }
                mLoading = false;
                mLock.notifyAll();
            }
        }
    }
}