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
    }

    override fun requestData() {
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.saveBtn->{
                DataManager.DataForSharePreferences.saveObject("user","这是一条测试的内容")
                ToastUtils.showToast(activity,"保存成功")
            }

            R.id.queryBtn->{
                val con=DataManager.DataForSharePreferences.getObject("user","")
                ToastUtils.showToast(activity,con!!)
            }
        }
    }

}