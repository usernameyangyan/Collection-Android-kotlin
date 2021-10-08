package com.youngmanster.collection_kotlin.mvvm.base

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.youngmanster.collection_kotlin.base.baseview.ICommonActivity
import com.youngmanster.collection_kotlin.mvvm.viewmode.BaseViewModel
import com.youngmanster.collection_kotlin.network.rx.RxManager
import java.lang.reflect.ParameterizedType


/**
 * Created by yangy
 *2021/3/29
 *Describe:
 */
abstract class IMVVMBaseActivity<V : ViewDataBinding, VM : BaseViewModel<*>>: ICommonActivity(){

    var mBinding: V? = null
    var viewModel: VM? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(isBinding()){
            initViewDataBinding()
            registerUIChangeLiveDataCallBack()
        }else{
            viewModel = createViewModel(this, BaseViewModel::class.java) as VM
        }
    }

    /**
     * 注入绑定
     */
    private fun initViewDataBinding() {
        mBinding = DataBindingUtil.bind(bindingActivityLayout!!)
        viewModel = initViewModel()
        if (viewModel == null) {
            val modelClass: Class<*>
            val type = javaClass.genericSuperclass
            modelClass = if (type is ParameterizedType) {
                type.actualTypeArguments[1] as Class<BaseViewModel<*>>
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                BaseViewModel::class.java
            }
            viewModel = createViewModel(this, modelClass) as VM
        }
        //关联ViewModel
        mBinding?.setVariable(1, viewModel)
        //支持LiveData绑定xml，数据改变，UI自动会更新
        mBinding?.lifecycleOwner = this
        //让ViewModel拥有View的生命周期感应
        lifecycle.addObserver(viewModel!!)
    }


    //注册ViewModel与View的契约UI回调事件
    open fun registerUIChangeLiveDataCallBack() {
        //跳入新页面
        viewModel?.getUcLiveData()?.getStartActivityEvent()?.observe(this,
            Observer { params ->
                val clz = params[BaseViewModel.ParameterField.CLASS] as Class<*>
                val bundle = params[BaseViewModel.ParameterField.BUNDLE] as Bundle
                startActivity(clz, bundle)
            })

        //关闭界面
        viewModel?.getUcLiveData()?.getFinishEvent()?.observe(this, Observer { finish() })
        //关闭上一层
        viewModel?.getUcLiveData()?.getOnBackPressedEvent()?.observe(this, Observer { onBackPressed() })
    }


    /**
     * 获取RxManager
     */
    open fun getRxManager():RxManager?{
        return viewModel?.rxManager
    }


    /**
     * 跳转页面
     *
     * @param clz 所跳转的目的Activity类
     */
    open fun startActivity(clz: Class<*>) {
        startActivity(Intent(this, clz))
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    open fun startActivity(clz: Class<*>, bundle: Bundle?) {
        val intent = Intent(this, clz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    /**
     * 是否使用DataBinding
     * @return  默认为true 表示使用。如果为false，则不会初始化 [.mBinding]。
     */
    open fun isBinding(): Boolean {
        return true
    }


    /**
     * 初始化ViewModel
     *
     * @return 继承BaseViewModel的ViewModel
     */
    open fun initViewModel(): VM? {
        return null
    }


    /**
     * 创建ViewModel
     *
     * @param cls
     * @param <T>
     * @return
    </T> */
    open fun <T : ViewModel> createViewModel(
        activity: FragmentActivity,
        cls: Class<T>
    ): T {
        return ViewModelProvider(activity).get(cls)
    }


    override fun onDestroy() {
        super.onDestroy()


        if (viewModel != null) {
            lifecycle.removeObserver(viewModel!!)
            viewModel = null
        }

        mBinding?.unbind()
    }

}