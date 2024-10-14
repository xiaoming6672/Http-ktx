package com.zhang.lib.httpktx.startup

import android.content.Context
import androidx.startup.Initializer
import com.zhang.library.utils.context.ContextUtils

/**
 * 初始化
 *
 * @author ZhangXiaoMing 2024-10-14 17:22 周一
 */
class RetrofitStartUp : Initializer<Unit> {

    /**
     * Initializes a library component within the application [Context].
     *
     * @param context The application context.
     */
    override fun create(context : Context) {
        ContextUtils.set(context)
    }

    /**
     * Gets a list of this initializer's dependencies.
     *
     * Dependencies are initialized before the dependent initializer. For
     * example, if initializer A defines initializer B as a dependency, B is
     * initialized before A.
     *
     * @return A list of initializer dependencies.
     */
    override fun dependencies() : MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}