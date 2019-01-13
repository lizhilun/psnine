package demo.lizl.com.psnine.iview

import demo.lizl.com.psnine.model.PostItem

interface IHomeFragmentView : IBaseView
{
    fun onPostListRefresh(postList: List<PostItem>)

    fun onPostListLoadMore(postList: List<PostItem>)
}