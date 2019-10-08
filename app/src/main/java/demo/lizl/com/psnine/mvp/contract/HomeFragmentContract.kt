package demo.lizl.com.psnine.mvp.contract

import demo.lizl.com.psnine.bean.PostItem
import demo.lizl.com.psnine.mvp.BasePresenter
import demo.lizl.com.psnine.mvp.BaseView

class HomeFragmentContract
{
    interface View : BaseView
    {
        fun onPostListRefresh(postList: List<PostItem>)

        fun onPostListLoadMore(postList: List<PostItem>)
    }

    interface Presenter : BasePresenter<View>
    {
        fun refreshPostList()

        fun LoadMorePost()
    }
}