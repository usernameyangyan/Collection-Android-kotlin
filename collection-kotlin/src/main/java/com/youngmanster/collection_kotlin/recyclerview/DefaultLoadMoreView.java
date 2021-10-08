package com.youngmanster.collection_kotlin.recyclerview;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.youngmanster.collection_kotlin.R;
import com.youngmanster.collection_kotlin.recyclerview.BaseLoadMoreView;
import com.youngmanster.collection_kotlin.utils.LogUtils;

/**
 * Created by yangyan
 * on 2018/3/9.
 */

public class DefaultLoadMoreView extends BaseLoadMoreView {

	private boolean isDestroy=false;
	private TextView noDataTv;
	private LinearLayout loadMoreLl;
	private TextView refreshStatusTv;

	public DefaultLoadMoreView(Context context) {
		super(context);
	}

	@Override
	public void initView(Context context){
		mContainer = LayoutInflater.from(context).inflate(R.layout.collection_library_layout_default_loading_more, null);
		addView(mContainer);
		setGravity(Gravity.CENTER);
		noDataTv=mContainer.findViewById(R.id.no_data);
		loadMoreLl=mContainer.findViewById(R.id.loadMore_Ll);
		refreshStatusTv=mContainer.findViewById(R.id.refresh_status_tv);


		if(PullToRefreshRecyclerViewUtils.loadingTextConfig!=null){
			noDataTv.setText(PullToRefreshRecyclerViewUtils.loadingTextConfig.getCollectionNoMoreData());
			refreshStatusTv.setText(PullToRefreshRecyclerViewUtils.loadingTextConfig.getCollectionLoadingMore());
			if(PullToRefreshRecyclerViewUtils.loadingTextConfig.getCollectionTextColor()!=0){
				refreshStatusTv.setTextColor(ContextCompat.getColor(context,PullToRefreshRecyclerViewUtils.loadingTextConfig.getCollectionTextColor()));
				noDataTv.setTextColor(ContextCompat.getColor(context,PullToRefreshRecyclerViewUtils.loadingTextConfig.getCollectionTextColor()));
			}
		}
	}

	@Override
	public void setState(int state) {
		if(isDestroy){
			return;
		}
		this.setVisibility(VISIBLE);
		switch (state){
			case STATE_LOADING:
				loadMoreLl.setVisibility(VISIBLE);
				noDataTv.setVisibility(GONE);
				break;
			case STATE_COMPLETE:
				this.setVisibility(GONE);
				break;
			case STATE_NODATA:
				loadMoreLl.setVisibility(GONE);
				noDataTv.setVisibility(VISIBLE);
				break;
		}
		mState = state;

	}

	@Override
	public void destroy() {
		isDestroy=true;
	}

}
