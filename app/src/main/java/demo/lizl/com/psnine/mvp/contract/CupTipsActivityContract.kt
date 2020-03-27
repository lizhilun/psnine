package demo.lizl.com.psnine.mvp.contract

import demo.lizl.com.psnine.bean.ReplyPostItem
import demo.lizl.com.psnine.mvp.base.BasePresenter
import demo.lizl.com.psnine.mvp.base.BaseView

class CupTipsActivityContract
{
    interface View : BaseView
    {
        fun onCupInfoRefresh(cupName: String, cupDescription: String, cupCover: String)

        fun onCupTipPostListRefresh(postList: List<ReplyPostItem>)
    }

    interface Presenter : BasePresenter<View>
    {
        fun setCupTipsUrl(cupTipsUrl: String)

        fun refreshTipsList()
    }
}