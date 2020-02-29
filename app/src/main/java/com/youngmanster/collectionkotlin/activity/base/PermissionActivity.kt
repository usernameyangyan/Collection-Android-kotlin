package com.youngmanster.collectionkotlin.activity.base

import android.Manifest
import androidx.annotation.NonNull
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.permission.PermissionManager
import com.youngmanster.collection_kotlin.utils.ToastUtils
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.base.BaseActivity

/**
 * Created by yangy
 *2020-02-23
 *Describe:
 */

class PermissionActivity :BaseActivity<BasePresenter<*>>(){

    //权限管理
    private var permissionManager: PermissionManager? = null

    // 项目的必须权限，没有这些权限会影响项目的正常运行
    private val PERMISSIONS = arrayOf(
        Manifest.permission.READ_SMS,
        Manifest.permission.RECEIVE_WAP_PUSH,
        Manifest.permission.READ_CONTACTS
    )

    override fun getLayoutId(): Int {
        return R.layout.activity_permission
    }

    override fun init() {
        defineActionBarConfig.setTitle(getString(R.string.activity_permission))
    }

    override fun requestData() {
        permissionManager = PermissionManager.with(this).setNecessaryPermissions(PERMISSIONS)

        permissionManager?.requestPermissions()
    }

    //重写
    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        if (requestCode == PermissionManager.PERMISSION_REQUEST_CODE) {//PERMISSION_REQUEST_CODE为请求权限的请求值
            //有必须权限选择了禁止
            if (permissionManager?.shouldShowRequestPermissionsCode == PermissionManager.EXIST_NECESSARY_PERMISSIONS_PROHIBTED) {
                ToastUtils.showToast(this, "可以在这里设置重新跳出权限请求提示框")
            } //有必须权限选择了禁止不提醒
            else if (permissionManager?.shouldShowRequestPermissionsCode == PermissionManager.EXIST_NECESSARY_PERMISSIONS_PROHIBTED_NOT_REMIND) {
                ToastUtils.showToast(this, "可以在这里弹出提示框提示去应用设置页开启权限")
                permissionManager?.startAppSettings()
            }
        }
    }

}