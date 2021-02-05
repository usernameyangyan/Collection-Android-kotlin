package com.youngmanster.collection_kotlin.recyclerview.tree.base;

import androidx.recyclerview.widget.RecyclerView;

import com.youngmanster.collection_kotlin.recyclerview.tree.TreeItem;
import com.youngmanster.collection_kotlin.recyclerview.tree.exception.StopMsgException;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author: yangyan
 * @desc:
 */
public abstract class BaseTreeAdapter extends BaseMultipleAdapter<RecyclerView.ViewHolder> {

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Observable.create(new ObservableOnSubscribe<StopMsgException>() {
            @Override
            public void subscribe(ObservableEmitter<StopMsgException> emitter) throws StopMsgException {
                try {
                    getLevel(position, mDatas, new PosBean());
                } catch (StopMsgException ex) {
                    emitter.onNext(ex);
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<StopMsgException>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(StopMsgException ex) {
                        int type = Integer.parseInt(ex.getMessage());
                        TreeItem tree = ex.getTree();
                        onBindViewHolder(type, tree, holder);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @Override
    public int getItemViewType(int position) {
        try {
            getLevel(position, mDatas, new PosBean());
        } catch (StopMsgException ex) {
            return Integer.parseInt(ex.getMessage());
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return getTotal(mDatas);
    }

    public abstract void onBindViewHolder(int type, TreeItem tree, RecyclerView.ViewHolder holder);

    private Integer getTotal(List<TreeItem> paramDatas) {
        int total = 0;
        for (int i = 0, size = paramDatas == null ? 0 : paramDatas.size(); i < size; i++) {
            TreeItem tree = paramDatas.get(i);
            if (tree.isExpand()) {
                total++;
                total = getTotal(tree.getChildren()) + total;
            } else {
                total++;
            }
        }
        return total;
    }


    private void getLevel(int position, List<TreeItem> paramDatas, PosBean posBean) throws StopMsgException {
        if (paramDatas == null) return;
        for (TreeItem tree : paramDatas) {
            if (tree.isExpand()) {
                posBean.setIndex(posBean.getIndex() + 1);
                if (position + 1 == posBean.getIndex())
                    throw new StopMsgException(String.valueOf(tree.getLevel())).setTree(tree);
                getLevel(position, tree.getChildren(), posBean);
            } else {
                posBean.setIndex(posBean.getIndex() + 1);
                if (position + 1 == posBean.getIndex())
                    throw new StopMsgException(String.valueOf(tree.getLevel())).setTree(tree);
            }
        }
    }


    private static class PosBean {
        int index = 0;

        int getIndex() {
            return index;
        }

        void setIndex(int index) {
            this.index = index;
        }
    }
}