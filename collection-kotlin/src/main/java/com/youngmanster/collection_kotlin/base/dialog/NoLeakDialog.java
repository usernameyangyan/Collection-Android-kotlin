package com.youngmanster.collection_kotlin.base.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.lang.ref.WeakReference;

/**
 * author : yangyan
 * date : 2021/5/15 9:02 AM
 * description :
 */
public class NoLeakDialog extends Dialog {

    private WeakReference<DialogFragment> hostFragmentReference;

    public void setHostFragmentReference(DialogFragment hostFragment) {
        this.hostFragmentReference = new WeakReference<>(hostFragment);
    }

    public NoLeakDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }


    @Override
    public void setOnCancelListener(@Nullable OnCancelListener listener) {
        // 空实现，不持有外部的引用
    }

    @Override
    public void setOnShowListener(@Nullable OnShowListener listener) {
        // 空实现，不持有外部的引用
    }

    @Override
    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        // 空实现，不持有外部的引用
    }

    @Override
    public void setCancelMessage(@Nullable Message msg) {
        // 空实现，不持有外部的引用
    }

    @Override
    public void setDismissMessage(@Nullable Message msg) {
        // 空实现，不持有外部的引用
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (null != hostFragmentReference && null != hostFragmentReference.get()) {
            hostFragmentReference.get().dismissAllowingStateLoss();
        }
    }
}
