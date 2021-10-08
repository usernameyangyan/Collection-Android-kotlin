package com.youngmanster.collection_kotlin.config.moudle;

import android.app.Application;
import android.util.Log;

import io.reactivex.annotations.Nullable;

/**
 * 作为组件生命周期初始化的配置类，通collection-kotlin过反射机制，动态调用每个组件初始化逻辑
 */

public class ModuleLifecycleConfig {
    //内部类，在装载该内部类时才会去创建单例对象
    private static class SingletonHolder {
        public static ModuleLifecycleConfig instance = new ModuleLifecycleConfig();
    }



    public static ModuleLifecycleConfig getInstance() {
        return SingletonHolder.instance;
    }

    private ModuleLifecycleConfig() {}

    //初始化组件
    public void initModule(@Nullable Application application) {
        if(ModuleLifecycleReflexs.initModuleNames!=null){
            for (String moduleInitName : ModuleLifecycleReflexs.initModuleNames) {
                try {
                    Class<?> clazz = Class.forName(moduleInitName);
                    IModuleInit init = (IModuleInit) clazz.newInstance();
                    //调用初始化方法
                    init.onInitModule(application);
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                }
            }
        }

    }
}
