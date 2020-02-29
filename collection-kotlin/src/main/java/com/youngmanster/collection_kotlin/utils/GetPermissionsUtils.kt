package com.youngmanster.collection_kotlin.utils

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PermissionGroupInfo

/**
 * Created by yangy
 *2020-02-21
 *Describe:
 */

class GetPermissionsUtils {
    companion object{
        fun getAllPermissons(context: Context): String {

            val stringBuffer = StringBuffer()
            try {
                val pm = context.packageManager
                val pi = pm.getPackageInfo(context.packageName, 0)
                //得到自己的包名
                val pkgName = pi.packageName

                val pkgInfo = pm.getPackageInfo(pkgName, PackageManager.GET_PERMISSIONS)
                val sharedPkgList = pkgInfo.requestedPermissions
                LogUtils.error("权限", sharedPkgList.size.toString() + "")

                for (i in sharedPkgList.indices) {
                    val permName = sharedPkgList[i]

                    val tmpPermInfo = pm.getPermissionInfo(permName, 0)

                    stringBuffer.append("========$permName========\n")
                    stringBuffer.append(tmpPermInfo.loadLabel(pm).toString() + "\n")
                }


            } catch (e: PackageManager.NameNotFoundException) {
                LogUtils.error("权限", "报错：$e")
            }

            return stringBuffer.toString()
        }
    }

}
