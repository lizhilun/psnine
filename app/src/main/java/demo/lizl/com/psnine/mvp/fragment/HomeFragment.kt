package demo.lizl.com.psnine.mvp.fragment

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.adapter.PostListAdapter
import demo.lizl.com.psnine.bean.PostItem
import demo.lizl.com.psnine.mvp.activity.BaseActivity
import demo.lizl.com.psnine.mvp.contract.HomeFragmentContract
import demo.lizl.com.psnine.mvp.presenter.HomeFragmentPresenter
import demo.lizl.com.psnine.util.UiUtil
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment<HomeFragmentPresenter>(), HomeFragmentContract.View
{
    private lateinit var postListAdapter: PostListAdapter

    override fun getLayoutResId(): Int
    {
        return R.layout.fragment_home
    }

    override fun isNeedRegisterEventBus() = false

    override fun initPresenter() = HomeFragmentPresenter(this)

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

        presenter.refreshPostList()
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