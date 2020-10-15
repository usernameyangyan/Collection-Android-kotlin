package com.youngmanster.collectionkotlin.activity.download
import com.mvp.annotation.MvpAnnotation
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.mvp.BaseView
import com.youngmanster.collection_kotlin.utils.LogUtils
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.base.BaseActivity
import com.youngmanster.collectionkotlin.mvp.presenter.DownloadFilePresenter
import com.youngmanster.collectionkotlin.mvp.view.IDownloadFileView
import kotlinx.android.synthetic.main.activity_downlaod.*

/**
 * Created by yangy
 *2020/9/19
 *Describe:
 */
@MvpAnnotation(
    prefixName = "DownloadFile",
    basePresenterClazz = BasePresenter::class,
    baseViewClazz = BaseView::class
)
class DownFileActivity:BaseActivity<DownloadFilePresenter>(),IDownloadFileView{
    /**
     * 布局文件加载
     */
    override fun getLayoutId(): Int {
        return R.layout.activity_downlaod
    }

    /**
     * 初始化参数
     */
    override fun init() {

        defineActionBarConfig.setTitle("断点下载文件")

        downloadBtn.setOnClickListener {
            mPresenter?.downloadFile(this)
        }

        stopDownloadBtn.setOnClickListener {
            mPresenter?.stopDownload()
        }

    }

    /**
     * 请求数据
     */
    override fun requestData() {

    }

    override fun updateProgress(progress: Float) {
        progressTv.text="已下载："+ String.format("%.1f",progress*100)+"%"
    }

    override fun onError(errorMsg: String) {

    }
}