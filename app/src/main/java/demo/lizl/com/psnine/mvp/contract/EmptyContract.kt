package demo.lizl.com.psnine.mvp.contract

import demo.lizl.com.psnine.mvp.base.BasePresenter
import demo.lizl.com.psnine.mvp.base.BaseView

class EmptyContract
{
    interface View : BaseView

    interface Presenter : BasePresenter<View>
}