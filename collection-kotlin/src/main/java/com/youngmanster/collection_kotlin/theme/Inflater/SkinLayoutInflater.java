package com.youngmanster.collection_kotlin.theme.Inflater;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;


public interface SkinLayoutInflater {
    View createView(@NonNull Context context, final String name, @NonNull AttributeSet attrs);
}
