package demo.lizl.com.psnine.mvp.fragment

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
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

    override fun getLayoutResId() = R.layout.fragment_home

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

        postListAdapter.setOnPostItemClickListener {
            ((activity as BaseActivity<*>).turnToPostDetailActivity(it.postDetailUrl))
        }

        postListAdapter.setOnPostAvatarClickListener {
            ((activity as BaseActivity<*>).turnToUserDetailActivity(it.postWriterId))
        }

        presenter.refreshPostList()
    }

    override fun onPostListRefresh(postList: List<PostItem>)
    {
        refresh_layout.finishRefresh()
        postListAdapter.setNewData(postList.toMutableList())
    }

    override fun onPostListLoadMore(postList: List<PostItem>)
    {
        refresh_layout.finishLoadMore()
        postListAdapter.addData(postList)
    }
}