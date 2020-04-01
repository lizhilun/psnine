package demo.lizl.com.psnine.mvvm.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import demo.lizl.com.psnine.util.DialogUtil

abstract class BaseFragment : Fragment()
{
    protected var TAG = this.javaClass.simpleName

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

        DialogUtil.dismissDialog()
    }

    override fun onDestroy()
    {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
    }

    abstract fun getLayoutResId(): Int

    abstract fun initView()
}