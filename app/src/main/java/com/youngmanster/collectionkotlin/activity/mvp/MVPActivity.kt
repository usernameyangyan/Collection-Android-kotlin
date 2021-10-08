package com.youngmanster.collectionkotlin.activity.mvp

import android.Manifest
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.NonNull
import androidx.recyclerview.widget.LinearLayoutManager
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.permission.PermissionManager
import com.youngmanster.collection_kotlin.recyclerview.BaseRecyclerViewAdapter
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.adapter.recyclerview.PullToRecyclerViewAdapter
import com.youngmanster.collectionkotlin.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

/**
 * Created by yangy
 *2020-02-22
 *Describe:
 */
class MVPActivity:BaseActivity<BasePresenter<*>>(), BaseRecyclerViewAdapter.OnItemClickListener{


    private val listData = ArrayList<String>()

    private var permissionManager: PermissionManager? = null
    private var pullToRefreshAdapter: PullToRecyclerViewAdapter? = null

    private val PERMISSIONS = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun init() {
        defineActionBarConfig.setTitle(getString(R.string.all_title))

        permissionManager = PermissionManager.with(this)
            .setNecessaryPermissions(PERMISSIONS)
        permissionManager?.requestPermissions()

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recycler_rv.layoutManager = linearLayoutManager
        val header = LayoutInflater.from(this).inflate(R.layout.layout_network_header, null)
        recycler_rv.addHeaderView(header)
    }

    override fun requestData() {
        listData.add("MVP+RxJava+Retrofit+OkHttp的缓存机制")
        listData.add("MVP+RxJava+Retrofit+自定义磁盘缓存机制")
        refreshUI()
    }

    fun refreshUI() {

        pullToRefreshAdapter = PullToRecyclerViewAdapter(this,R.layout.item_pull_refresh, listData, recycler_rv)
        recycler_rv.adapter = pullToRefreshAdapter
        pullToRefreshAdapter?.setOnItemClickListener(this)
    }

    override fun onItemClick(view: View, position: Int) {
        when(position){
            0 -> {
                startAc(WeChatFeaturedActivity::class.java)
            }

            1 -> {
                startAc(WeChatNewsDefinitionActivity::class.java)
            }
        }
    }

    //重写
    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionManager.PERMISSION_REQUEST_CODE) {//PERMISSION_REQUEST_CODE为请求权限的请求值
            //有必须权限选择了禁止
            if (permissionManager?.shouldShowRequestPermissionsCode == PermissionManager.EXIST_NECESSARY_PERMISSIONS_PROHIBTED) {
                permissionManager?.requestPermissions()
            } //有必须权限选择了禁止不提醒
            else if (permissionManager?.shouldShowRequestPermissionsCode == PermissionManager.EXIST_NECESSARY_PERMISSIONS_PROHIBTED_NOT_REMIND) {
                permissionManager?.startAppSettings()
            }
        }
    }

}