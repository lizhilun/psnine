package demo.lizl.com.psnine.fragment

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.activity.MainActivity
import demo.lizl.com.psnine.adapter.PostListAdapter
import demo.lizl.com.psnine.iview.IHomeFragmentView
import demo.lizl.com.psnine.model.PostItem
import demo.lizl.com.psnine.presenter.HomeFragmentPresenter
import demo.lizl.com.psnine.util.UiUtil
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment(), IHomeFragmentView
{
    override fun getLayoutResId(): Int
    {
        return R.layout.fragment_home
    }

    private fun getPresenter() = presenter as HomeFragmentPresenter

    override fun initPresenter()
    {
        presenter = HomeFragmentPresenter(activity as Context, this)
        getPresenter().refreshPostList()
    }

    override fun initView()
    {
        refresh_layout.setEnableLoadMore(false)
        refresh_layout.setRefreshHeader(UiUtil.getDefaultRefreshHeader(activity as Context))
        refresh_layout.setEnableRefresh(true)
        refresh_layout.setOnRefreshListener { getPresenter().refreshPostList() }
    }

    override fun onPostListRefresh(postList: List<PostItem>)
    {
        refresh_layout.finishRefresh()
        val postListAdapter = PostListAdapter(activity as Context, postList)
        rv_post_list.layoutManager = LinearLayoutManager(activity)
        rv_post_list.adapter = postListAdapter

        postListAdapter.setOnPostItemClickListener(object : PostListAdapter.OnPostItemClickListener
        {
            override fun onPostItemClick(postItem: PostItem)
            {
                ((activity as MainActivity).turnToPostDetailActivity(postItem.postDetailUrl))
            }
        })
    }
}