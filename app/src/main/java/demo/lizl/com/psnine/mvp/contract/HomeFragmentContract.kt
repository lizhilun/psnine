package demo.lizl.com.psnine.mvp.contract

import demo.lizl.com.psnine.bean.PostItem
import demo.lizl.com.psnine.mvp.base.BasePresenter
import demo.lizl.com.psnine.mvp.base.BaseView

class HomeFragmentContract
{
    interface View : BaseView
    {
        fun onPostListRefresh(postList: MutableList<PostItem>)

        fun onPostListLoadMore(postList: MutableList<PostItem>)
    }

    interface Presenter : BasePresenter<View>
    {
        fun refreshPostList()

        fun LoadMorePost()
    }
}