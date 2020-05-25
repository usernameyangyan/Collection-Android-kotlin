## Collection-Kotlin

![Travis](https://img.shields.io/badge/release-1.4.7-green.svg)
![Travis](https://img.shields.io/badge/llicense-MIT-green.svg)
![Travis](https://img.shields.io/badge/build-passing-green.svg)


Collection聚合了项目搭建的一些基本模块，节约开发者时间，协助项目的快速搭建,RecyclerView+Adapter+Retrofit+RxJava+MVP+DataManager+基本Base,能够满足一个项目的基本实现。


#### 更多交流请加微信公众号
![](https://upload-images.jianshu.io/upload_images/4361802-88c89753c38ddf70.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
> Collection聚合了项目搭建的一些基本模块，节约开发者时间，协助项目的快速搭建,RecyclerView+Adapter+Retrofit+RxJava+MVP+DataManager+基本Base,能够满足一个项目的基本实现。

###  推荐文件
>Android X库之前版本可使用Collection-Android：[https://juejin.im/post/5ab9987451882555635e5401](https://juejin.im/post/5ab9987451882555635e5401)

> Collection-iOS库:[https://juejin.im/post/5e423d4ef265da572a0cec9f](https://juejin.im/post/5e423d4ef265da572a0cec9f)


###  Collection-kotlin作为Collection-Android的更新篇，主要是为了解决使用Android X库带来的一些问题以及模块的优化

> ######  简书：https://www.jianshu.com/p/a6cb49532760
> ######  掘金地址：https://juejin.im/post/5e59d0eef265da57315b0b0e

###  更新说明

#### v1.4.7
> 1.DialogFragment替换AlertDialog   
> 2.SharePreference统一初始化  
> 3.增加Fragment的跳转
> 4.DataManager.DataForHttp增加文件下载

####   v1.1.1
> 网络框架增加文件下载以及进度

####   v1.1.0
> 1.修复SQLite没有创建表查询异常

> 2.SQLite增加按条件查询List

> 3.增加PopupWindow显示位置设置

> 4.解决SQLit内容为null报错

####   v1.0.1
>  状态栏修改：增加设置状态栏透明+黑色字体

####   v1.0.0
>  1.在Collection-Android的基础上适配Android X库  
>  2.去掉Relam数据模块,安装包大小减少  
>  3.对原生SQLite数据库进行封装，使用更加方便
>  4.对DataManager的使用进行修整
>  5.增加AutoLineLayout/TagView
>  6.增加LinkedMultiValueMap  
>  7.增加RxJavaUtils,可进行子/主线程数据处理切换

###  框架的引入
>  **implementation 'com.youngman:collection_kotlin:1.4.7'**


###   一、框架整体模块
![效果图](https://upload-images.jianshu.io/upload_images/4361802-3af08da14f6f4231.gif?imageMogr2/auto-orient/strip)



###  二、PullToRefreshRecyclerView的使用

| 属性 | 作用 | 
| :-----| :---- | 
|addHeaderView | 增加头部布局， 暂时只能添加一个头布局|
|setEmptyView | 设置自定义的加载布局和空布局|
|setRefreshView | 自定义刷新View|
|setDefaultLoadingMoreNoDataMessage | 设置默认没有数据的内容|
|setLoadMoreView | 自定义加载更多View|
|setNoMoreDate | 显示没有更多数据|
|setAutoRefresh | 自动刷新|
|refreshComplete | 刷新数据完成|
|loadMoreComplete | 加载更多数据完成|
|setPullRefreshEnabled | 是否允许刷新|
|setLoadMoreEnabled | 是否允许加载更多|
|setRefreshTimeVisible |显示加载更新时间|
|isLoading | 是否正在loading数据|
|isRefreshing | 正在refreshing数据|
|setRefreshAndLoadMoreListener |刷新和加载更多回调|
|destroy |内存回收|

####  1.框架默认下拉刷新、上拉加载更多样式  

![效果图](https://upload-images.jianshu.io/upload_images/4361802-322d12a7ccffd66f.gif?imageMogr2/auto-orient/strip)


##### （1）布局文件  
  
     <com.youngmanster.collection_kotlin.recyclerview.PullToRefreshRecyclerView
		android:id="@+id/recycler_rv"
		android:layout_width="match_parent"
		android:layout_height="match_parent" />

#####  （2）代码设置

     recycler_rv.setPullRefreshEnabled(true)
     recycler_rv.setLoadMoreEnabled(true)
  

####   2、自定义下拉刷新、上拉加载更多样式

![效果图](https://upload-images.jianshu.io/upload_images/4361802-a04743d4ca751974.gif?imageMogr2/auto-orient/strip)

 #####  刷新几种状态：
| 属性 | 作用 | 
| :-----| :---- | 
| STATE_PULL_DOWN | 拉的状态（还没到下拉到固定的高度时）|
| STATE_RELEASE_REFRESH | 下拉到固定高度提示释放刷新的状态|
| STATE_REFRESHING | 正在刷新状态|
| STATE_DONE | 刷新完成|

 #####  加载更多几种状态：

| 属性 | 作用 | 
| :-----| :---- | 
| STATE_LOADING | 正在加载|
| STATE_COMPLETE |加 载完成|
| STATE_NODATA | 没有数据|

##### （1）代码设置

    recycler_rv.setPullRefreshEnabled(true)
    recycler_rv.setLoadMoreEnabled(true)
    recycler_rv.setRefreshAndLoadMoreListener(this)
    recycler_rv.setRefreshView(DefinitionAnimationRefreshHeaderView(activity))
    recycler_rv.setLoadMoreView(DefinitionAnimationLoadMoreView(activity))


##### 自定义刷新的步骤：

###### ①自定义View继承BasePullToRefreshView，重写initView()、setRefreshTimeVisible(boolean show)、destroy()方法: 

1.在initView()做自定义布局、相关动画的初始化，最后在initView()方法的最后面添加以下代码即可。

         mContainer =
            LayoutInflater.from(context).inflate(R.layout.layout_definition_animation_refresh, null)
        //把刷新头部的高度初始化为0
        val lp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        lp.setMargins(0, 0, 0, 0)
        this.layoutParams = lp
        this.setPadding(0, 0, 0, 0)
        addView(mContainer, LayoutParams(LayoutParams.MATCH_PARENT, 0))
        gravity = Gravity.BOTTOM

        //测量高度
        measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        mMeasuredHeight = measuredHeight


2.setRefreshTimeVisible(boolean show)是用来设置是否显示刷新时间控件，在默认刷新样式中通过mRecyclerView.setRefreshTimeVisible(false)即可隐藏刷新时间，如果在自定义的布局中没有这项这个方法就可以忽略。

3.destroy()是用来关掉改页面时把刷新View的一些动画等释放，防止内存泄漏。


######  ②实现BasePullToRefreshView.OnStateChangeListener监听(重点，主要是进行状态切换后的相关操作逻辑） 

1.在构造函数中设置 onStateChangeListener=this
2.onStateChange的模板样式

    override fun onStateChange(state: Int) {

        if(isDestroy){
            return
        }
        //下拉时状态相同不做继续保持原有的状态
        if (state == mState) return
        //根据状态进行动画显示
        when (state) {
            STATE_PULL_DOWN -> {
                clearAnim()
                startAnim()
            }
            STATE_RELEASE_REFRESH -> {
            }
            STATE_REFRESHING -> {
                clearAnim()
                startAnim()
                scrollTo(mMeasuredHeight)
            }
            STATE_DONE -> {
            }
        }
        mState = state
    }


#####  自定义加载更多的步骤(包括没有更多数据显示的操作)：
######  ①自定义View继承BaseLoadMoreView，重写initView()、setState()、destroy()方法:

1.在initView()做自定义布局、相关动画的初始化，最后在initView()方法的最后面添加以下代码即可

        mContainer = LayoutInflater.from(context)
            .inflate(R.layout.layout_definition_animation_loading_more, null)
        addView(mContainer)
        gravity = Gravity.CENTER

   


2.destroy()是用来关掉改页面时把刷新View的一些动画等释放，防止内存泄漏。
3.在setState()进行状态切换后的相关操作逻辑，模板样式

    override fun setState(state: Int) {

        if(isDestroy){
            return
        }

        when (state) {
            STATE_LOADING -> {
                loadMore_Ll?.visibility = VISIBLE
                noDataTv?.visibility = INVISIBLE
                animationDrawable = loadingIv?.drawable as AnimationDrawable
                animationDrawable?.start()
                this.visibility = VISIBLE
            }
            STATE_COMPLETE -> {
                animationDrawable?.stop()
                this.visibility = GONE
            }
            STATE_NODATA -> {
                loadMore_Ll?.visibility = INVISIBLE
                noDataTv?.visibility = VISIBLE
                animationDrawable = loadingIv?.drawable as AnimationDrawable
                animationDrawable?.start()
                this.visibility = VISIBLE
            }
        }

        mState = state
    }

4.注意：在自定义加载更多样式时，如果需要有没有更多加载更多数据提示同样需要在布局中写好，然后在onSatae中根据状态对加载和没有跟多显示提示进行显示隐藏操作。


#### 3、上拉加载更多配合SwipeRefreshLayout使用  
![效果图](https://upload-images.jianshu.io/upload_images/4361802-7e148b8ad3ad149e.gif?imageMogr2/auto-orient/strip)


##### （1）布局文件    

    <androidx.swiperefreshlayout.widget.SwipeRefreshLayout
        android:id="@+id/swipeRefreshLayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

         <com.youngmanster.collection_kotlin.recyclerview.PullToRefreshRecyclerView
            android:id="@+id/recycler_rv"
            android:layout_width="match_parent"
            android:layout_height="match_parent"/>

    </androidx.swiperefreshlayout.widget.SwipeRefreshLayout>

##### （2）代码设置

    recycler_rv.setLoadMoreEnabled(true)
    recycler_rv.setRefreshAndLoadMoreListener(this)
    recycler_rv.setLoadMoreView(DefinitionAnimationLoadMoreView(activity))
    swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent)
    swipeRefreshLayout.setOnRefreshListener(this)


#####   (3)注意的问题
> 由于PullToRefreshRecyclerView的下拉刷新和下拉加载更多完成时会自动刷新Adapter,而SwipeRefreshLayout刷新完成时需要手动进行notifyDataSetChanged刷新适配器。

###   4、RecyclerView添加头部、空布局
![效果图](https://upload-images.jianshu.io/upload_images/4361802-174ba31760700eaa.gif?imageMogr2/auto-orient/strip)



#####  （1）代码设置

        //增加头布局
        val header = LayoutInflater.from(activity).inflate(R.layout.layout_main_header, null)
        recycler_rv.addHeaderView(header)

        //增加空布局
        val emptyView = LayoutInflater.from(activity).inflate(R.layout.layout_empty, null)
        recycler_rv.emptyView = emptyView
        emptyView.setOnClickListener {
            for (i in 0..9) {
                mDatas.add("item$i")
            }
            definitionRefreshAdapter?.notifyDataSetChanged()
        }

####   5、上拉加载更多实现NoMoreData、自动刷新

![效果图](https://upload-images.jianshu.io/upload_images/4361802-de5e0724eb09f38d.gif?imageMogr2/auto-orient/strip)



##### （1）上拉加载更多数据的布局设置在上面的自定义LoadingMoreView中有介绍，如果要显示没有更多数据提示只需要在LoadMore返回数据之后设置：

    mRecyclerView.setNoMoreDate(true)


##### （2）自动刷新需要列表已经填充了数据之后再做自动刷新操作才会生效：

    mRecyclerView.setAutoRefresh()


####   6.Collection库的刷新加载文本已经适配了中文、英文、繁体，如果对默认刷新加载显示文本进行修改，通过LoadingTextConfig设置，可在Application全局设置


    val textConfig = LoadingTextConfig.getInstance(applicationContext)
    textConfig
         .setCollectionLoadingMore("加载更多")
         .setCollectionLastRefreshTimeTip("更新时间：")
         .setCollectionNoMoreData("没有更多数据")
         .setCollectionPullReleaseText("释放刷新")
         .setCollectionRefreshing("正在刷新")
         .setCollectionPullDownRefreshText("下拉刷新")
         .setCollectionRefreshDone("加载完成")
     PullToRefreshRecyclerViewUtils.loadingTextConfig = textConfig

####   7、PullToRefreshRecyclerView的其他使用以及注意问题

1.下面是下拉刷新上拉加载更多的一些操作模板

    private fun refreshUI() {
        if (defaultRefreshAdapter == null) {
            defaultRefreshAdapter =
                DefaultRecyclerAdapter(
                    activity!!,
                    R.layout.item_pull_refresh,
                    mDatas,
                    recycler_rv
                )
            recycler_rv.adapter = defaultRefreshAdapter
        } else {
            if (recycler_rv != null) {
                if (recycler_rv.isLoading) {
                    recycler_rv.loadMoreComplete()
                } else if (recycler_rv.isRefreshing) {
                    recycler_rv.refreshComplete()
                }
            }
        }
    }


2.注意问题

######  ①在设置RecyclerView是要设LayoutManager
######  ②如果使用PullToRefreshRecyclerView在Activty/Fragment中的onDestroy（）调用mRecyclerView.destroy()防止内存泄漏。
    @Override
	public void onDestroy() {
		super.onDestroy();
		mRecyclerView?.destroy()
	}


######  ③设置refreshRv.setLoadMoreEnabled(true)，当填充的数据的列表size为0的同时还通过RecyclerView设置分割线底部就会出现一个空白的item，这个item就是加载更多显示的Item。 
######  解决办法：不通过RecyclerView设置分割线，直接在布局自定义分割线。


###  三、BaseRecyclerViewAdapter的使用

####  1.BaseRecyclerViewAdapter的比原始Adapter的代码量减小

在BaseRecyclerViewAdapter中的BaseViewHolder进行布局转化，同时定义了一些比较基本的View操作，使用简单。

##### （1）使用代码：

    class DefinitionRecyclerAdapter(
          mContext: Context,
          mLayoutResId: Int,
          mDatas: ArrayList<String>,
          pullToRefreshRecyclerView: PullToRefreshRecyclerView
    ) : BaseRecyclerViewAdapter<String>(mContext, mLayoutResId, mDatas, pullToRefreshRecyclerView) {
    
        override fun convert(baseViewHolder: BaseViewHolder, t: String) {
              baseViewHolder.setText(R.id.title, t)
        }
    }


######  ①使用者需要在继承BaseRecyclerViewAdapter时传入一个数据实体类型，具体的操作在convert()方法中操作。
###### ②BaseViewHolder提供了一些常用View的基本操作，通过baseViewHolder.getView()可得到布局中的控件。


##### （2）BaseRecyclerViewAdapter提供了两个构造函数

	public BaseRecyclerViewAdapter(Context mContext, int mLayoutResId, List<T> mDatas, PullToRefreshRecyclerView pullToRefreshRecyclerView) {
		this.mContext = mContext;
		this.mLayoutResId = mLayoutResId;
		this.mDatas = mDatas;
		this.mRecyclerView=pullToRefreshRecyclerView;
	}

	public BaseRecyclerViewAdapter(Context mContext, int mLayoutResId, List<T> mDatas) {
		this.mContext = mContext;
		this.mLayoutResId = mLayoutResId;
		this.mDatas = mDatas;
	}

######  主要是对PullToRefreshRecyclerView和RecyclerView的适配，使用时适配器根据需要使用对应的构造函数。

####   2.添加Item的点击和长按事件 

![效果图](https://upload-images.jianshu.io/upload_images/4361802-d443a4b5875f71f5.gif?imageMogr2/auto-orient/strip)



##### （1）Item长按事件实现

     itemClickAdapter.setOnItemLongClickListener(object :
            BaseRecyclerViewAdapter.onItemLongClickListener {
            override fun onItemLongClick(view: View, position: Int): Boolean {
                showToast("进行长按操作")
                return true
            }
        })

####   3.多布局的使用 

![效果图](https://upload-images.jianshu.io/upload_images/4361802-855cf331c4a556b0.gif?imageMogr2/auto-orient/strip)


##### BaseRecyclerViewAdapter的多布局实现需要注意的四步：

######  ①自定义Adapter需要继承BaseRecyclerViewMultiItemAdapter。
######  ② 数据实体类需要继承BaseMultiItemEntity，在getItemViewType()返回布局类型。
######  ③ 在自定义Adapter中的构造函数中通过addItemType()传入不同类型对应的布局。
######   ④在自定义Adapter中的convert进行类型判断，做相对应的操作。


    class MultipleAdapter(mContext: Context, mDatas: ArrayList<MultiItem>) :
    BaseRecyclerViewMultiItemAdapter<MultiItem>(mContext, mDatas) {

        private var mHeight: Int=0

        init {
            mHeight = DisplayUtils.dip2px(mContext, 100f)
            addItemType(MultiItem.TYPE_TEXT, R.layout.item_main)
            addItemType(MultiItem.TYPE_IMG, R.layout.item_img)
            addItemType(MultiItem.TYPE_TEXT_IMG, R.layout.item_click)
      }

    override fun convert(baseViewHolder: BaseViewHolder, t: MultiItem) {

        when (baseViewHolder.itemViewType) {
            MultiItem.TYPE_TEXT -> {
                baseViewHolder.getView<CardView>(R.id.card_view).layoutParams.height = mHeight
                baseViewHolder.setText(R.id.title, t.title)
            }
            MultiItem.TYPE_IMG -> baseViewHolder.setImageResource(R.id.ivImg, t.res)
            MultiItem.TYPE_TEXT_IMG -> {
                baseViewHolder.setImageResource(R.id.ivImg, t.res)
                baseViewHolder.setText(R.id.titleTv, t.title)
            }
        }
      }

    }

####   4.添加拖拽、滑动删除 

![效果图](https://upload-images.jianshu.io/upload_images/4361802-172230556c3660ab.gif?imageMogr2/auto-orient/strip)


 
###### 局限：只针对RecyclerView，对本框架封装的PullToRefreshRecyclerView会出现混乱。

######   ①BaseRecyclerViewAdapter和BaseRecyclerViewMultiItemAdapter都已经封装支持拖拽、滑动，适配器只需要根据需求继承其中一个即可。
######   ②框架提供了一个BaseRecycleItemTouchHelper，对于普通的左右滑动删除、拖拽已经实现，如果想自定义可以继承BaseRecycleItemTouchHelper类，再重写相对应的方法进行实现。

######  ④在Activity/Fragment中需要实现以下代码：

        dragAndDeleteAdapter?.setDragAndDeleteListener(this)
        recycler_rv.adapter = dragAndDeleteAdapter

        val callback = BaseRecycleItemTouchHelper(dragAndDeleteAdapter)
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recycler_rv)


######  ⑤BaseRecyclerViewAdapter.OnDragAndDeleteListener进行操作动作完成之后的回调。


    override fun onMoveComplete() {
        ToastUtils.showToast(this, "移动操作完成")

    }

    override fun onDeleteComplete() {
        ToastUtils.showToast(this, "删除操作完成")
    }


###  四、MVP+RxJava+Retrofit的封装使用

![效果图](https://upload-images.jianshu.io/upload_images/4361802-2dd7e515529cfb1d.gif?imageMogr2/auto-orient/strip)



####  1.在使用Retrofit请求网络之前需要进行配置，在框架中提供了了Config配置类

| 属性 | 作用 | 
| :-----| :---- | 
|DEBUG | 是否为BuildConfig.DEBUG,日志输出需要|
|CONTEXT | 设置Context,必填项|
|URL_DOMAIN | 网络请求的域名，需要以“/”结尾|
|URL_CACHE | 网络缓存地址，需要设置缓存才可以成功|
|MAX_CACHE_SECONDS | 设置OkHttp的缓存机制的最大缓存时间,默认为一天|
|MAX_MEMORY_SIZE | 缓存最大的内存,默认为10M|
|MClASS | 设置网络请求json通用解析类|
|EXPOSEPARAM | Json数据某些字段在没有数据是会不返回来，可通过这个属性设置过滤|
|USER_CONFIG | SharePreference保存的名称|
|CONNECT_TIMEOUT_SECONDS | 请求接口超时设定|
|READ_TIMEOUT_SECONDS | 请求接口超时设定|
|HEADERS | 设置Http全局请求头|
|SQLITE_DB_NAME | 数据库名称|
|SQLITE_DB_VERSION | 数据库版本名|

######  在项目中需要根据项目需要进行配置，在Application中设置

    private fun config(){
        //基本配置
        Config.DEBUG = BuildConfig.DEBUG
        Config.CONTEXT = this
        Config.URL_CACHE = AppConfig.getURLCacheDir(this)
        Config.MClASS=HttpResult::class.java
        Config.USER_CONFIG = "Collection_User"
        Config.URL_DOMAIN = "https://api.apiopen.top/"
        Config.SQLITE_DB_VERSION=0
    }

######  根据项目需要定义一个通用的数据实体类，这是本例通用实体类，这个类需要设置到Applicatin中

    class HttpResult<T> : Serializable {
      var code: Int = 0
      var message: String? = null
      var result: T? = null
    }

######  注意：由于每个项目返回来的json数据格式有所不同，如果Result中代表的字段例如newslist没有内容返回来的时候这个字段需要后台控制不返回，如果不做处理会报解析错误,可以通过设置Config.EXPOSEPARAM属性过滤字段。

####   2.RxJava+Retrofit+OkHttp

（1）RequestBuilder的设置（网络请求的配置）

| 属性 | 作用 | 
| :-----| :---- | 
| ReqType |数据处理的方式，默认DEFAULT_CACHE_LIST,使用到OkHttp缓存的需要需要设置Config.URL_CACHE  | 
| NO_CACHE_MODEL | 不设置缓存，返回model|
| NO_CACHE_LIST | 不设置缓存,返回list|
| DEFAULT_CACHE_MODEL | 使用Okttp默认缓存，返回model|
| DEFAULT_CACHE_LIST | 使用Okttp默认缓存，返回list|
| DISK_CACHE_LIST_LIMIT_TIME | 限时使用自定义磁盘缓存，返回List|
| DISK_CACHE_MODEL_LIMIT_TIME | 限时使用自定义磁盘缓存，返回model|
| DISK_CACHE_NO_NETWORK_LIST | 自定义磁盘缓存，没有网络返回磁盘缓存，返回List|
| DISK_CACHE_NO_NETWORK_MODEL | /自定义磁盘缓存，没有网络返回磁盘缓存，返回Model|
|DOWNLOAD_FILE_MODEL|文件下载模式，返回Model|
| HttpType |网络请求方式,默认DEFAULT_GET | 
| DEFAULT_GET |GET请求| 
| DEFAULT_POST |POST请求 | 
| FIELDMAP_POST |如果请求URL出现中文乱码，可选择这个 | 
| JSON_PARAM_POST |json格式请求参数 | 
| ONE_MULTIPART_POST |上传一张图片 |
| MULTIPLE_MULTIPART_POST |上传多张图片 |
|DOWNLOAD_FILE_GET|下载文件|
| ReqMode |请求模式，默认ASYNCHRONOUS| 
| ASYNCHRONOUS |异步请求| 
| SYNCHRONIZATION |同步请求| 
| 其它参数 | | 
| setTransformClass |设置请求转化Class | 
| setUrl |设置请求url,如果不设置完全连接则会使用Config.URL_DOMIN进行拼接 | 
| setFilePathAndFileName |设置自定义缓存时的路径和文件名 | 
| setLimtHours |设置自定义缓存的有效时间 | 
| setHeader |设置请求头 | 
 | setHeaders |设置请求头集合 | 
| setHttpTypeAndReqType |设置请求数据类型和请求方式 | 
| setImagePath |设置上传图片路径 | 
| setImagePaths |设置多张图片路径 | 
| isUserCommonClass |设置是否使用公用类转化 | 
| setReqMode |设置同步异步 | 

（2）使用模块

         val requestBuilder=RequestBuilder(object :RxObservableListener<HttpResult<List<WeChatNews>>>(mView){
            override fun onNext(result: HttpResult<List<WeChatNews>>) {
                mView?.refreshUI(result.result)
            }
        })

        requestBuilder
            .setUrl(ApiUrl.URL_WETCHAT_FEATURED)
            .setTransformClass(WeChatNews().javaClass)
            .setHttpTypeAndReqType(RequestBuilder.HttpType.DEFAULT_GET,RequestBuilder.ReqType.NO_CACHE_LIST)
            .setParam("page",page)
            .setParam("type","video")
            .setParam("count",num)

        rxManager.addObserver(DataManager.DataForHttp.httpRequest(requestBuilder))

#####   注意：

######   （1）RxObservableListener有三个回调方法
    void onNext(T result);
    void onComplete();
    void onError(NetWorkCodeException.ResponseThrowable e);
######  只会重写onNext方法，其它两个方法可以自行选择重写。
######   （2）RxObservableListener提供两个构造函数
    protected RxObservableListener(BaseView view){
	    this.mView = view;
    }

    protected RxObservableListener(BaseView view, String errorMsg){
	     this.mView = view;
         this.mErrorMsg = errorMsg;
    }

######  这两个构造函数主要主要是为了统一处理onError的，如果要自定义错误提醒，则可以选择第二个构造函数。

######  （3）通过DataManager的网络请求方式会返回来一个DisposableObserver，需要把它通过rxManager.addObserver()添加进CompositeDisposable才能正常执行,方便对网络请求的销毁管理。

######  （4）如果项目没有统一的解析been类，那么Config的公用类就不用设置了，在Retrofit请求的时候直接setTransformClass指定一个解析类就可以了

######  （5）如果项目想两种方式共存，那么在请求的时候需要通过setUserCommonClass（false）设置才能不使用统一解析类进行解析

####   3.MVP
1.写一个Contract类对Presenter和View进行统一管理(View需要实现BaseView,Presenter需要实现BasePresenter<T:BaseView>)

    interface WeChatChinaNewsContract {
      interface View : BaseView {
        fun refreshUI(weChatNews: List<WeChatNews>?)
      }

      abstract class Presenter : BasePresenter<View>() {
          abstract fun requestChinaNews(context: Context, page: Int, num: Int)
      }
    }
2.写一个具体的Presenter类实现WeChatChinaNewsContract.Presenter,在里面做具体的逻辑处理，处理完成再通过mView进行View的处理

3.Activity/Fragment实现IBaseActivity<T:BasePresenter>/IBaseFragment<T:BasePresenter>以及定义好的WeChatChinaNewsContract.View

4.缺陷：View在使用时需要转化成在具体的子类才能调用相关方法。

5.具体使用可以参照demo


###  五、DataManager的使用(Http、Sharepreference、SQLite)

![效果图](https://upload-images.jianshu.io/upload_images/4361802-249f682764c7e8b5.gif?imageMogr2/auto-orient/strip)



（1）DataManager基本属性

| 属性 | 作用 | 
| :-----| :---- | 
| DataForSqlite |数据库模块  | 
| insert | 插入bean数据|
| insertList | 插入List数据|
| insertListBySync | 异步插入List数据|
| queryByFirstByWhere | 根据条件查询|
| queryAll | 查询某个bean类的全部数据|
| queryAllBySync | 异步查询某个bean类的全部数据|
| queryByFirst | 查询某个bean类的第一条数据|
| delete |根据条件删除数据|
| deleteAll | 删除某个bean类的所有数据|
| deleteTable | 删除数据表|
| update | 更新某个bean类的|
| queryOfPageByWhere |根据条件分页查询，实体类必须包含PrimaryKey|
| queryOfPage | 分页查询，实体类必须包含PrimaryKey|
| updateTable | 更新数据表，用于增加字段|
| execQuerySQL | Sql语句查询|
| DataForHttp |Http模块  | 
| httpRequest | 网络请求，传入RequestBuilder|
| DataForSharePreferences | SharePreference模块|
| saveObject | 保存基本类型数据|
| getObject | 获取基本类型数据|


（2）DataForSqlite


1.插入一条数据

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

2.查询数据

    val user=DataManager.DataForSqlite.queryByFirst(User().javaClass)
    ToastUtils.showToast(activity,"姓名："+user?.name+"  "+"年龄："+user?.age)

3.批量插入数据（可同步可异步）

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

4.bean类的定义


    class User{
      @Column(isPrimaryKey = true)
      var id:Int=0
      var name:String?="你好"
      var age:Int?=0
    }

其中可以通过Column进行注解定义（isPrimaryKey、isNull、isUnique）

5.数据表格变化（只支持增加字段）

* 修改数据库版本号Config.SQLITE_DB_VERSION，往上递增
* 在Application中对版本号进行监听，并对数据表进行更新


     SQLiteVersionMigrate().setMigrateListener(object :SQLiteVersionMigrate.MigrateListener{
            override fun onMigrate(oldVersion: Int, newVersion: Int) {
                for (i in oldVersion..newVersion){
                    if(i==2){
                    }

                }
            }

      })

（3）DataForHttp


    val requestBuilder=RequestBuilder(object :RxObservableListener<HttpResult<List<WeChatNews>>>(mView){
            override fun onNext(result: HttpResult<List<WeChatNews>>) {
                mView?.refreshUI(result.result)
            }
     })

    requestBuilder
            .setUrl(ApiUrl.URL_WETCHAT_FEATURED)
            .setTransformClass(WeChatNews().javaClass)
            .setHttpTypeAndReqType(RequestBuilder.HttpType.DEFAULT_GET,RequestBuilder.ReqType.NO_CACHE_LIST)
            .setParam("page",page)
            .setParam("type","video")
            .setParam("count",num)

    rxManager.addObserver(DataManager.DataForHttp.httpRequest(requestBuilder))

（4）DataForSharePreferences

1.插入基本数据

    DataManager.DataForSharePreferences.saveObject("user","这是一条测试的内容")
    ToastUtils.showToast(activity,"保存成功")

2.查询基本类型数据

    val con=DataManager.DataForSharePreferences.getObject("user","")
    ToastUtils.showToast(activity,con!!)


###  六.Base的使用

#####  1.为了方便Activity/Fragment设置顶部菜单栏，继承IBaseActivity/IBaseFragment即可显示一个简单的顶部菜单，IBaseFragment的顶部菜单默认隐藏，下面以IBaseActivity的顶部菜单作为例子

| 属性 | 作用 | 
| :-----| :---- | 
| isShowSystemActionBar | 重写该方法设置实现显示系统ActionBar | 
| isShowCustomActionBar | 重写该方法设置显示自定义Bar|
| setCustomActionBar | 重写该方法设置自定义Bar|


* 如果使用默认自定义Bar可通过DefaultDefineActionBarConfig进行相关设置

| 属性 | 作用 | 
| :-----| :---- | 
| hideBackBtn | 隐藏返回按钮| 
| setBarBackgroundColor | 设置Bar的背景颜色|
| setBarHeight | 设置Bar的高度|
| setTitleColor | 设置标题颜色|
| setTitle | 设置标题|
| setBackClick | 设置返回按钮监听|

- 代码使用

     
    defineActionBarConfig
           .setTitleSize(20f)
           .setBarHeight(DisplayUtils.dip2px(this,60f))
           .setBarBackgroundColor(this,R.color.driver_font)
           .setTitle(getString(R.string.tab_Indicator_title))

#####  2.StateView（数据加载页面）

![效果图](https://upload-images.jianshu.io/upload_images/4361802-a5bdf5937016db04.gif?imageMogr2/auto-orient/strip)



| 属性 | 作用 | 
| :-----| :---- | 
| STATE_NO_DATA | 不显示加载框状态码 | 
| STATE_LOADING | 加载数据显示状态码|
| STATE_EMPTY | 没有数据显示状态码|
| STATE_DISCONNECT | 没有网络状态码|
| setOnDisConnectViewListener |点击没有网络图标回调|
| setOnEmptyViewListener | 点击没有没有数据图标回调|
| showViewByState | 设置显示状态|
| getmEmptyView |获取无数据状态View|
|布局可设置参数 ||
|loadingViewAnimation |设置加载的drawable动画|
|loadingText |加载时的文本|
|emptyImage |空布局显示的图片|
|emptyText |空布局文本|
|emptyViewRes |设置自定义空布局|
|disConnectImage |设置断网显示的图片|
|disConnectText |设置断网显示的文本|
|tipTextSize |文本字体大小|
|tipTextColor |文本字体颜色|


######  （1）定义一个通用布局


    <com.youngmanster.collection_kotlin.base.stateview.StateView xmlns:android="http://schemas.android.com/apk/res/android"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    android:layout_centerInParent="true"
	    android:id="@+id/state_view">

    </com.youngmanster.collection_kotlin.base.stateview.StateView >


######  (2)添加到Ui页面的layout中

	<include layout="@layout/layout_state"/>

###### 注意：上面的语句添加的layout最外层最好是LinearLayout以及设置为android:orientation="vertical"


######  (3)通过以下语句进行状态切换
 
	stateView.showViewByState(StateView.STATE_LOADING)
	stateView.showViewByState(StateView.STATE_EMPTY)
	stateView.showViewByState(StateView.STATE_NO_DATA)
	stateView.showViewByState(StateView.STATE_DISCONNECT)



####   3.三步实现Permission(权限)设置

![效果图](https://upload-images.jianshu.io/upload_images/4361802-9e90267e0cc6e924.gif?imageMogr2/auto-orient/strip)



##### （1）设置好要请求的权限

    // 项目的必须权限，没有这些权限会影响项目的正常运行
    private val PERMISSIONS = arrayOf(
        Manifest.permission.READ_SMS,
        Manifest.permission.RECEIVE_WAP_PUSH,
        Manifest.permission.READ_CONTACTS
    )


##### （2）权限通过PermissionManager管理

     permissionManager = PermissionManager
        .with(this)
        .setNecessaryPermissions(PERMISSIONS)

    permissionManager?.requestPermissions()

#####  （3）页面重写onRequestPermissionsResult

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

######   注意：如果有需求先判断是否所有权限都已经允许之后再进入主页面可以通过permissionManager.isLackPermission()进行判断，如果返回true则进行权限请求，如果返回false则进入主页面。


- 多个权限请求如果其中某一个被禁止提醒，会先把没有禁止提醒的权限处理完之后再进行处理。
- 如果是必要权限被禁止而没有选择禁止提醒退出之后下次会重新请求权限。
- 如果必要权限被禁止和选择了禁止提醒重新进入页面在onRequestPermissionsResult会重新回调方法。
- 使用者可以根据onRequestPermissionsResult（）方法中返回来的标志PermissionManager.EXIST_NECESSARY_PERMISSIONS_PROHIBTED和PermissionManager.EXIST_NECESSARY_PERMISSIONS_PROHIBTED_NOT_REMIND做出对应的显示和操作（例如弹框提示跳转到设置页面或者toat提示）。

####   4.提供几种比较常用的Dialog弹框

![效果图](https://upload-images.jianshu.io/upload_images/4361802-53d247ce86547c61.gif?imageMogr2/auto-orient/strip)


#####  （1）提供的常用的CommonDialog

| 属性 | 作用 | 
| :-----| :---- | 
| DIALOG_TEXT_TWO_BUTTON_DEFAULT | 默认弹出按钮提示 | 
| DIALOG_TEXT_TWO_BUTTON_CUSTOMIZE | 自定义弹出按钮提示 | 
| DIALOG_LOADING_PROGRASSBAR | 默认加载弹框|
| DIALOG_CHOICE_ITEM | 没有数据显示状态码|

根据不同的构造函数设置不同的参数

#####  （2）自定义Dialog样式

- BaseDialogFragment

| 属性 | 作用 | 
| :-----| :---- | 
|  setContentView | 设置弹框布局样式 |
| onViewCreated | 初始化完成后的回调，可在此做一些初始化 | 
| show(...) | 显示弹框 | 
| dismiss | 弹框销毁|
| setAllCancelable| 点击返回键和外部不可取消|
| setOnlyBackPressDialogCancel | 点击返回键可以取消|
| setDialogInterval| 设置弹框和屏幕两边的间距|
| setDialogHeight | 设置弹框高度|
| setOnDismissListener | 弹框销毁回调|

- 继承BaseDialogFragment，通过setContentView(R.layout.dialog_list)设置弹窗布局。
- 在提供的onViewCreated方法中进行相应的逻辑设置


####   5.自定义PopupWindow弹框

![效果图](https://upload-images.jianshu.io/upload_images/4361802-52b0f6b11fcc8a61.gif?imageMogr2/auto-orient/strip)



- BasePopupWindow

| 属性 | 作用 | 
| :-----| :---- | 
| BasePopupWindow(Context context) |调用该构造函数默认弹出框铺满全屏 | 
| BasePopupWindow(Context context, int w, int h) | 自定义弹出框高宽 | 
| showPopup |在屏幕中央显示弹框|
| showPopupAsDropDown | 在指定控件底部显示弹框|
| showPopup |在屏幕中央显示弹框|
| showPopupAsDropDown | 在指定控件底部显示弹框|
| setShowMaskView | 设置是否显示遮层|
| dismiss|销毁弹出框|
| getPopupLayoutRes | 自定义弹出框的布局文件|
| getPopupAnimationStyleRes | 自定义弹出框的动画文件，不设置动画返回0|

- 继承BasePopupWindow。
- 通过getPopupLayoutRes(R.layout.xxx)设置弹窗布局。
- 通过getPopupAnimationStyleRes(R.style.xxx)设置弹窗动画，不需要动画可以设置为0。
- 如果需要显示遮层，在构造函数通过setShowMaskView(true)设置。

###  七、CustomView的使用

####   1.CommonTabLayout的使用

![效果图](https://upload-images.jianshu.io/upload_images/4361802-348b46993780cbbf.gif?imageMogr2/auto-orient/strip)


| 属性 | 作用 | 
| :-----| :---- | 
| tab_tabIndicatorWidth |设置下滑线的长度| 
| tab_tabIndicatorHeight | 设置下滑线的高度 | 
| tab_tabIndicatorColor |下滑线颜色|
| tab_indicator_marginLeft | 设置下滑线外边距|
| tab_indicator_marginRight |设置下滑线外边距|
| tab_indicator_marginTop | 设置下滑线外边距|
| tab_indicator_marginBottom | 设置下滑线外边距|
| tab_tabTextColor|没选中字体颜色|
| tab_tabTextSize | 字体大小|
| tab_tabSelectedTextColor | 选中字体颜色|
| tab_padding | 下滑线内边距，block样式时可以通过该属性设置距离|
| tab_tabBackground |Tab 的背景颜色|
| tab_indicator_corner|下滑线的圆角大小|
| tab_indicator_gravity（bottom、top | 设置下滑线显示的位置，只针对line和triangle|
| tab_tabMode（scrollable、fixed） | Tab的显示模式|
| tab_indicator_style（line、triangle、block） | 下滑线的样式|

#####   具体可参照例子使用。

####   2.OutSideFrameTabLayout的使用

![效果图](https://upload-images.jianshu.io/upload_images/4361802-061a8ad6aef94cee.gif?imageMogr2/auto-orient/strip)



| 属性 | 作用 | 
| :-----| :---- | 
|  tab_tabIndicatorColor |设置Tab颜色| 
| tab_indicator_corner | 圆角大小 | 
| tab_indicator_marginLeft |下滑线外边距|
| tab_indicator_marginRight | 下滑线外边距|
| tab_indicator_marginTop |下滑线外边距|
| tab_indicator_marginBottom | 下滑线外边距|
| tab_tabTextColor | 没选中字体颜色|
| tab_tabSelectedTextColor|选中字体颜色|
| tab_tabTextSize | 字体大小|
| tab_tabSelectedTextColor | 选中字体颜色|
| tab_padding | 内边距|
|  tab_bar_color |bar的背景颜色|
| tab_bar_stroke_color|外框的颜色|
| tab_bar_stroke_width | 外框的大小|
| tab_width |bar的长度|

#####   具体可参照例子使用。


####   3.AutoLineLayout的使用

![效果图](https://upload-images.jianshu.io/upload_images/4361802-778c3cf7dfbdebf5.gif?imageMogr2/auto-orient/strip)



- 在外层布局使用AutoLineLayout

      <com.youngmanster.collection_kotlin.base.customview.wraplayout.AutoLineLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

      </com.youngmanster.collection_kotlin.base.customview.wraplayout.AutoLineLayout>


####   4.TagView的使用

![效果图](https://upload-images.jianshu.io/upload_images/4361802-01cb3b80c1c0a659.gif?imageMogr2/auto-orient/strip)



######   TagViewConfigBuilder

| 属性 | 作用 | 
| :-----| :---- | 
| setTitles |设置TagItem内容 | 
| setTextSize | 设置TagItem字体大小 | 
| setTextColor |设置TagItem字体颜色|
| setTextSelectColor | 设置TagItem选择字体颜色|
| setPaddingLeftAndRight |设置TagIttem左右内边距|
| setPaddingTopAndBottom | 设置TagIttem上下内边距|
| setMarginAndTopBottom | 设置TagItem上下外边距|
| setMarginLeftAndRight|设置TagItem左右外边距|
| setackgroudRes | 设置background Drawable|
| setTagViewAlign| 设置整体TagItem的Align(LEFT,RIGHT,CENTER) |

######  1.布局

    <com.youngmanster.collection_kotlin.base.customview.tagview.TagView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:id="@+id/tagView" />

#####  2.代码设置

    tagView.create(builder,object :TagView.TagViewPressListener{
            override fun onPress(view: View, title: String, position: Int) {
                ToastUtils.showToast(this@TagViewActivity,title)
            }
     })


###  八、Fragment间的跳转

####   Fragment的跳转

![](https://upload-images.jianshu.io/upload_images/4361802-d636c05aab353cd2.gif?imageMogr2/auto-orient/strip)

| 属性 | 作用 | 
| :-----| :---- | 
| fragmentLayoutId |设置显示Fragment的根布局id| 
| startFragmentForResult(...) |和回调结果跳转| 
| onFragmentResult(....) |Fragment的结果回调|
| setResult(...) |onFragmentResult回调的结果设置 |
| startFragment(...) |普通跳转，具体使用查看IBaseActivity|

###  九、工具类的使用

#####  1.Density（适配不同手机像素）
 * 在Applicaton的onCreate中设置 Density.setDensity(this, 375f)
*  375f代表设计稿的宽度，以dp为单位，后面需要以f（浮点型）

#####  2.DisplayUtils

![效果图](https://upload-images.jianshu.io/upload_images/4361802-e241e639553a7450.gif?imageMogr2/auto-orient/strip)



| 属性 | 作用 | 
| :-----| :---- | 
| px2dip |px值转换为dip或dp值，保证尺寸大小不变（有精度损失）| 
| px2dipByFloat |px值转换为dip或dp值，保证尺寸大小不变（无精度损失 | 
| dip2px |dip或dp值转换为px值，保证尺寸大小不变（有精度损失），类似Context.getDimensionPixelSize方法（四舍五入|
| dip2pxByFloat | dip或dp值转换为px值，保证尺寸大小不变（无精度损失），类似Context.getDimension方法|
| px2sp |px值转换为sp值，保证文字大小不变|
| sp2px | sp值转换为px值，保证文字大小不变|
| getScreenWidthPixels | 屏幕宽度|
| getScreenHeightPixels|屏幕高度|
| getDisplayInfo | 获取设备信息|
| setStatusBarBlackFontBgColor | 设置黑色字体状态的背景颜色|
| setStatusBarColor |设置状态栏背景颜色|
| setStatusBarFullTranslucent | 设置状态栏透明|
| getStatusBarHeight |获取状态栏高度|
| getActionBarHeight|获取ActionBar高度|

#####  3.ColorUtils

| 属性 | 作用 | 
| :-----| :---- | 
| createColorStateList |获取ColorStateList| 

#####  4.FileUtils

| 属性 | 作用 | 
| :-----| :---- | 
| WriterTxtFile |写文件，其中append可设置是否添加在原内容的后边|
| ReadTxtFile |读取文本文件中的内容，strFilePath代表文件详细路径| 
| isCacheDataFailure |判断缓存是否失效| 
| checkFileExists |检查文件是否存在|  
| checkSaveLocationExists |检查是否安装SD卡|
| deleteDirectory |删除目录(包括：目录里的所有文件)| 
| deleteFile |删除文件| 
| getFileOrFilesSize |获取文件指定文件的指定单位的大小，其中sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB|  
| getFileSize|获取指定文件大小| 


#####  5.GetPermissionsUtils

| 属性 | 作用 | 
| :-----| :---- | 
| getAllPermissons |获取应用用到的所有权限|

#####  6.GlideUtils

| 属性 | 作用 | 
| :-----| :---- | 
| loadImg |加载图片|
| loadImgBlur |Glide实现高斯模糊|
| loadImgBlur |Glide实现高斯模糊，可设置模糊的程度|

#####  7.ThreadPoolManager：线程池管理类

#####  8.LogUtils：日记工具类

#####  9.NetworkUtils：网络工具类

#####  10.SoftInputUtils：键盘工具类

#####  11.ToastUtils：Toast工具类

#####  12.RxJavaUtil：主/子线程的切换


####  本文章会根据需要持续更新，建议点赞收藏，便于查看。也欢迎大家提出更多建议。
