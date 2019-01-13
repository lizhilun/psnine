package demo.lizl.com.psnine.presenter

import android.content.Context
import demo.lizl.com.psnine.iview.IBaseView

abstract class BasePresenter<T : IBaseView>(protected var context: Context, protected var iView: T)
{
    protected val TAG = this.javaClass.simpleName

    fun init()
    {
        iView.initView()
    }
}