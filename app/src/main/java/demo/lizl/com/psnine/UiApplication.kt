package demo.lizl.com.psnine

import android.app.Application

class UiApplication : Application()
{
    private val TAG = javaClass.simpleName

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
}