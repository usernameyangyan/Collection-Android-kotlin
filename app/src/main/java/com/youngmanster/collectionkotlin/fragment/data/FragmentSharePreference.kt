package com.youngmanster.collectionkotlin.fragment.data

import android.view.View
import com.youngmanster.collection_kotlin.data.DataManager
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.utils.ToastUtils
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_sharepreference.*

/**
 * Created by yangy
 *2020-02-23
 *Describe:
 */
class FragmentSharePreference:BaseFragment<BasePresenter<*>>(), View.OnClickListener{


    override fun getLayoutId(): Int {
        return R.layout.fragment_sharepreference
    }

    override fun init() {
        saveBtn.setOnClickListener(this)
        queryBtn.setOnClickListener(this)
        saveBtn1.setOnClickListener(this)
        queryBtn1.setOnClickListener(this)
    }

    override fun requestData() {
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.saveBtn->{
                DataManager.DataForSharePreferences.saveObject("user","这是一条测试SP的内容")
                ToastUtils.showToast("保存成功")
            }

            R.id.queryBtn->{
                val con=DataManager.DataForSharePreferences.getObject("user","")
                ToastUtils.showToast(con!!)
            }


            R.id.saveBtn1->{
                DataManager.DataForMMKV.saveObject("user1","这是一条测试MMKV的内容")
                ToastUtils.showToast("保存成功")
            }

            R.id.queryBtn1->{
                val con=DataManager.DataForMMKV.getObject("user1","")
                ToastUtils.showToast(con!!)
            }
        }
    }

}