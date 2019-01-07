package demo.lizl.com.psnine.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import demo.lizl.com.psnine.presenter.BasePresenter

abstract class BaseFragment : Fragment()
{
    protected var TAG = this.javaClass.simpleName

    protected lateinit var presenter: BasePresenter

    protected var isFragmentVisible = false

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

        initPresenter()
        presenter.init()
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

        isFragmentVisible = true
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

        isFragmentVisible = false
    }

    abstract fun getLayoutResId(): Int

    abstract fun initPresenter()
}