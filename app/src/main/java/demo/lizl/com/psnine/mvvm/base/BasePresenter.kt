package demo.lizl.com.psnine.mvvm.base

interface BasePresenter<T : BaseView>
{
    fun onDestroy()
}