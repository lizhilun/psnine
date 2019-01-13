package demo.lizl.com.psnine.fragment

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.activity.BaseActivity
import demo.lizl.com.psnine.adapter.PostListAdapter
import demo.lizl.com.psnine.iview.IHomeFragmentView
import demo.lizl.com.psnine.model.PostItem
import demo.lizl.com.psnine.presenter.HomeFragmentPresenter
import demo.lizl.com.psnine.util.UiUtil
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment<HomeFragmentPresenter>(), IHomeFragmentView
{
    private lateinit var postListAdapter: PostListAdapter

    override fun getLayoutResId(): Int
    {
        return R.layout.fragment_home
    }

    override fun initPresenter()
    {
        presenter = HomeFragmentPresenter(activity as Context, this)
        presenter.refreshPostList()
    }

    override fun initView()
    {
        refresh_layout.setEnableLoadMore(true)
        refresh_layout.setRefreshHeader(UiUtil.getDefaultRefreshHeader(activity as Context))
        refresh_layout.setEnableRefresh(true)
        refresh_layout.setOnRefreshListener { presenter.refreshPostList() }
        refresh_layout.setOnLoadMoreListener { presenter.LoadMorePost() }

        postListAdapter = PostListAdapter()
        rv_post_list.layoutManager = LinearLayoutManager(activity)
        rv_post_list.adapter = postListAdapter

        postListAdapter.setOnPostItemClickListener(object : PostListAdapter.OnPostItemClickListener
        {
            override fun onPostItemClick(postItem: PostItem)
            {
                ((activity as BaseActivity<*>).turnToPostDetailActivity(postItem.postDetailUrl))
            }
        })

        postListAdapter.setOnPostAvatarClickListener(object : PostListAdapter.OnPostAvatarClickListener
        {
            override fun onPostAvatarClick(postItem: PostItem)
            {
                ((activity as BaseActivity<*>).turnToUserDetailActivity(postItem.postWriterId))
            }
        })
    }

    override fun onPostListRefresh(postList: List<PostItem>)
    {
        refresh_layout.finishRefresh()
        postListAdapter.clear()
        postListAdapter.addAll(postList)
    }

    override fun onPostListLoadMore(postList: List<PostItem>)
    {
        refresh_layout.finishLoadMore()
        postListAdapter.insertAll(postList, postListAdapter.data.size)
    }
}