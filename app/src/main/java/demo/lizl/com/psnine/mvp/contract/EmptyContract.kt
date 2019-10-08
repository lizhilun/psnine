package demo.lizl.com.psnine.mvp.contract

import demo.lizl.com.psnine.mvp.BasePresenter
import demo.lizl.com.psnine.mvp.BaseView

class EmptyContract
{
    interface View : BaseView

    interface Presenter : BasePresenter<View>
}