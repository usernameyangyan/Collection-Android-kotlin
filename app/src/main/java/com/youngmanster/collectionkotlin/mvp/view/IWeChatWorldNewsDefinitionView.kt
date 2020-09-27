package com.youngmanster.collectionkotlin.mvp.view

import com.youngmanster.collection_kotlin.mvp.BaseView
import com.youngmanster.collectionkotlin.bean.WeChatNews

interface IWeChatWorldNewsDefinitionView :BaseView{
    fun refreshUI(weChatNews: List<WeChatNews>?)
}