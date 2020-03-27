package demo.lizl.com.psnine.mvp.base

interface BasePresenter<T : BaseView>
{
    fun onDestroy()
}