package demo.lizl.com.psnine

import android.app.Application
import demo.lizl.com.psnine.config.ConfigHelper

class UiApplication : Application()
{
    private val TAG = javaClass.simpleName

    private var configHelper: ConfigHelper? = null

    init
    {
        instance = this
    }

    override fun onCreate()
    {
        super.onCreate()
    }

    companion object
    {
        lateinit var instance: UiApplication

    }

    fun getConfigHelper(): ConfigHelper
    {
        if (configHelper == null)
        {
            configHelper = ConfigHelper.getDefaultConfigHelper(instance)
        }
        return configHelper as ConfigHelper
    }
}