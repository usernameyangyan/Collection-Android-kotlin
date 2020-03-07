package com.youngmanster.collectionkotlin.activity
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.adapter.MainViewAdapter
import com.youngmanster.collectionkotlin.base.BaseActivity
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.recyclerview.BaseRecyclerViewAdapter
import com.youngmanster.collection_kotlin.utils.LogUtils
import com.youngmanster.collectionkotlin.activity.base.BaseUiActivity
import com.youngmanster.collectionkotlin.activity.baseadapter.BaseAdapterActivity
import com.youngmanster.collectionkotlin.activity.customview.CustomViewActivity
import com.youngmanster.collectionkotlin.activity.data.DataManagerActivity
import com.youngmanster.collectionkotlin.activity.mvp.MVPActivity
import com.youngmanster.collectionkotlin.activity.recyclerview.RecyclerViewActivity
import com.youngmanster.collectionkotlin.common.AppConfig
import java.util.ArrayList
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by yangy
 *2020-02-20
 *Describe:
 */

class MainActivity:BaseActivity<BasePresenter<*>>(), BaseRecyclerViewAdapter.OnItemClickListener{

    private var mainViewAdapter: MainViewAdapter? = null
    private val listData = ArrayList<String>()

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun init() {
        defineActionBarConfig.hideBackBtn()
            .setTitle(getString(R.string.activity_main_title))

        val layoutManager = GridLayoutManager(this, 2)
        recycler_rv.layoutManager = layoutManager
        val header = LayoutInflater.from(this).inflate(R.layout.layout_main_header, null)
        recycler_rv.addHeaderView(header)
    }

    override fun requestData() {
        listData.add("RecyclerView")
        listData.add("BaseAdapter")
        listData.add("MVP+RxJava+Retrofit")
        listData.add("DataManager(Retrofit/SharePreference/SQLite)")
        listData.add("Base")
        listData.add("CustomView")
        mainViewAdapter = MainViewAdapter(this,R.layout.item_main, listData, recycler_rv)
        recycler_rv.adapter = mainViewAdapter
        mainViewAdapter?.setOnItemClickListener(this)
    }


    override fun onItemClick(view: View, position: Int) {
        when(position){
            0 -> {
                startAc(RecyclerViewActivity::class.java)
            }

            1 -> {
                startAc(BaseAdapterActivity::class.java)
            }

            2 -> {
                startAc(MVPActivity::class.java)
            }

            3 ->{
                startAc(DataManagerActivity::class.java)
            }

            4 ->{
                startAc(BaseUiActivity::class.java)
            }

            5 ->{
                startAc(CustomViewActivity::class.java)
            }
        }
    }


}