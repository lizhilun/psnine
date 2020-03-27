package demo.lizl.com.psnine

import android.app.Application
import com.blankj.utilcode.util.Utils

class UiApplication : Application()
{
    private val TAG = javaClass.simpleName

    init
    {
        instance = this
    }

    companion object
    {
        lateinit var instance: UiApplication
    }

    override fun onCreate()
    {
        super.onCreate()

        Utils.init(this)
    }
}