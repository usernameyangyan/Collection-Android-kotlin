package androidx.appcompat.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

@SuppressLint("RestrictedApi")
public class SkinAppCompatDelegateImpl extends AppCompatDelegateImpl {
    private static Map<Activity, WeakReference<AppCompatDelegate>> sDelegateMap = new WeakHashMap<>();

    public static AppCompatDelegate get(Activity activity, AppCompatCallback callback) {
        WeakReference<AppCompatDelegate> delegateRef = sDelegateMap.get(activity);
        AppCompatDelegate delegate = (delegateRef == null ? null : delegateRef.get());
        if (delegate == null) {
            delegate = new SkinAppCompatDelegateImpl(activity, callback);
            sDelegateMap.put(activity, new WeakReference<>(delegate));
        }
        return delegate;
    }

    private SkinAppCompatDelegateImpl(@NonNull Activity activity,
                                      @Nullable AppCompatCallback callback) {
        super(activity, callback);
    }


    @Override
    public void installViewFactory() {
    }
}
