package com.youngmanster.collectionkotlin.fragment.data

import android.view.View
import com.youngmanster.collection_kotlin.data.DataManager
import com.youngmanster.collection_kotlin.data.database.SQLiteDataBase
import com.youngmanster.collection_kotlin.mvp.BasePresenter
import com.youngmanster.collection_kotlin.utils.ToastUtils
import com.youngmanster.collectionkotlin.R
import com.youngmanster.collectionkotlin.base.BaseFragment
import com.youngmanster.collectionkotlin.bean.User
import kotlinx.android.synthetic.main.fragment_sqlite.*

/**
 * Created by yangy
 *2020-02-23
 *Describe:
 */
class FragmentSqlite:BaseFragment<BasePresenter<*>>(), View.OnClickListener{


    override fun getLayoutId(): Int {
        return R.layout.fragment_sqlite
    }

    override fun init() {
        saveBtn.setOnClickListener(this)
        queryBtn.setOnClickListener(this)
        btn1.setOnClickListener(this)
        btn2.setOnClickListener(this)
    }

    override fun requestData() {
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.saveBtn->{
                var user= User()
                user.id=0
                user.age=24
                user.name="张三"
                var isSuccess=DataManager.DataForSqlite.insert(user)
                if(isSuccess!!){
                    ToastUtils.showToast(activity,"保存成功")
                }else{
                    ToastUtils.showToast(activity,"保存失败")
                }

            }

            R.id.queryBtn->{
                val user=DataManager.DataForSqlite.queryByFirst(User().javaClass)
                ToastUtils.showToast(activity,"姓名："+user?.name+"  "+"年龄："+user?.age)
            }

            R.id.btn1->{
                var list=ArrayList<User>()
                for (i in 0..10000){
                    var user= User()
                    user.id=i
                    user.age=i
                    user.name="张$i"
                    list.add(user)
                }

                DataManager.DataForSqlite.insertListBySync(User().javaClass,list,object :SQLiteDataBase.InsertDataCompleteListener{
                    override fun onInsertDataComplete(isInsert: Boolean?) {
                        if(isInsert!!){
                            ToastUtils.showToast(activity,"保存成功")
                        }else{
                            ToastUtils.showToast(activity,"保存失败")
                        }
                    }

                })
            }

            R.id.btn2->{
                DataManager.DataForSqlite.queryAllBySync(User().javaClass,object :SQLiteDataBase.QueryDataCompleteListener<User>{
                    override fun onQueryComplete(datas: List<User>?) {
                        ToastUtils.showToast(activity,"查询到${datas?.size}条数据")
                    }

                })
            }
        }
    }

}