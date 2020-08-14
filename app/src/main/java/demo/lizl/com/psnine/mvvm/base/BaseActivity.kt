package demo.lizl.com.psnine.mvvm.base

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import demo.lizl.com.psnine.util.DialogUtil

abstract class BaseActivity<DB : ViewDataBinding>(private val layoutResId: Int) : AppCompatActivity()
{
    protected val TAG = this.javaClass.simpleName

    protected lateinit var dataBinding: DB

    abstract fun initView()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)

        dataBinding = DataBindingUtil.setContentView(this, layoutResId)
        dataBinding.lifecycleOwner = this

        initView()
    }

    override fun onResume()
    {
        Log.d(TAG, "onResume")
        super.onResume()
    }

    override fun onStart()
    {
        Log.d(TAG, "onStart")
        super.onStart()
    }

    override fun onRestart()
    {
        Log.d(TAG, "onRestart")
        super.onRestart()
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
}