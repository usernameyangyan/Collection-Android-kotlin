package com.youngmanster.collection_kotlin.recyclerview.tree.base;

import androidx.recyclerview.widget.RecyclerView;

import com.youngmanster.collection_kotlin.recyclerview.tree.TreeItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: yangyan
 */
public abstract class BaseMultipleAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    List<TreeItem> mDatas = new ArrayList<>();

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public void addMoreDatas(List<TreeItem> info) {
        if (info != null && info.size() > 0) {
            mDatas.addAll(mDatas.size(), info);
            notifyItemRangeInserted(mDatas.size(), info.size());
        }
    }

    public List<TreeItem> getDatas() {
        return mDatas;
    }

    public  void setDatas(ArrayList<TreeItem> info) {
        if (info != null) {
            mDatas = info;
        } else {
            mDatas.clear();
        }
        notifyDataSetChanged();
    }

}