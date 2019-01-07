package demo.lizl.com.psnine.iview

import demo.lizl.com.psnine.model.ReplyPostItem

interface ICupTipsActivityView : IBaseView
{
    fun onCupInfoRefresh(cupName: String, cupDescription: String, cupCover: String)

    fun onCupTipPostListRefresh(postList: List<ReplyPostItem>)
}