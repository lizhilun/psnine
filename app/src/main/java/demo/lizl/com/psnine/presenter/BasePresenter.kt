package demo.lizl.com.psnine.presenter

import android.content.Context
import demo.lizl.com.psnine.iview.IBaseView

abstract class BasePresenter(protected var context: Context, protected var iView: IBaseView)
{
    protected val TAG = this.javaClass.simpleName

    fun init()
    {
        iView.initView()
    }
}