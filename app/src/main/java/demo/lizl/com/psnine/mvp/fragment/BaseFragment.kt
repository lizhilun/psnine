package demo.lizl.com.psnine.mvp.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import demo.lizl.com.psnine.mvp.BasePresenter
import org.greenrobot.eventbus.EventBus

abstract class BaseFragment<T : BasePresenter<*>> : Fragment()
{
    protected var TAG = this.javaClass.simpleName

    protected lateinit var presenter: T

    override fun onCreate(savedInstanceState: Bundle?)
    {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(getLayoutResId(), container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)

        presenter = initPresenter()

        if (isNeedRegisterEventBus() && !EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().register(this)
        }

        initView()
    }

    override fun onStart()
    {
        Log.d(TAG, "onStart")
        super.onStart()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean)
    {
        Log.d(TAG, "setUserVisibleHint:$isVisibleToUser")
        super.setUserVisibleHint(isVisibleToUser)
    }

    override fun onResume()
    {
        Log.d(TAG, "onResume")
        super.onResume()
    }

    override fun onPause()
    {
        Log.d(TAG, "onPause")
        super.onPause()
    }

    override fun onStop()
    {
        Log.d(TAG, "onStop")
        super.onStop()
    }

    override fun onDestroy()
    {
        Log.d(TAG, "onDestroy")
        super.onDestroy()

        if (isNeedRegisterEventBus()) EventBus.getDefault().unregister(this)
    }

    abstract fun getLayoutResId(): Int

    abstract fun isNeedRegisterEventBus(): Boolean

    abstract fun initPresenter(): T

    abstract fun initView()
}