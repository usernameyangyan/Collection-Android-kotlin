package com.youngmanster.collectionkotlin.activity.status

import android.content.Intent
import android.view.View
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.base.BaseActivity
import kotlinx.android.synthetic.main.activity_change_statusbar.*

/**
 * Created by yangy
 *2020-02-23
 *Describe:
 */

class ChangeStatusBarActivity :BaseActivity<BasePresenter<*>>(),View.OnClickListener{
    override fun getLayoutId(): Int {
        return R.layout.activity_change_statusbar
    }

    override fun init() {
        defineActionBarConfig.setTitle(getString(R.string.status_bar_title))

        status_btn1.setOnClickListener(this)
        status_btn2.setOnClickListener(this)
        status_btn3.setOnClickListener(this)

    }

    override fun requestData() {
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.status_btn1 ->{
                val intent = Intent(this, StatusBarColorActivity().javaClass)
                intent.putExtra("type", 0)
                startActivity(intent)
            }

            R.id.status_btn2 ->{
                val intent = Intent(this, StatusBarColorActivity().javaClass)
                intent.putExtra("type", 1)
                startActivity(intent)
            }

            R.id.status_btn3 ->{
                startAc(TransparentStatusBarActivity::class.java)
            }

        }

    }

}