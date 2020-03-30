package demo.lizl.com.psnine.mvp.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.adapter.PostListAdapter
import demo.lizl.com.psnine.bean.PostItem
import demo.lizl.com.psnine.mvp.activity.PostDetailActivity
import demo.lizl.com.psnine.mvp.contract.HomeFragmentContract
import demo.lizl.com.psnine.mvp.presenter.HomeFragmentPresenter
import demo.lizl.com.psnine.util.ActivityUtil
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment<HomeFragmentPresenter>(), HomeFragmentContract.View
{
    private lateinit var postListAdapter: PostListAdapter

    override fun getLayoutResId() = R.layout.fragment_home

    override fun initPresenter() = HomeFragmentPresenter(this)

    override fun initView()
    {
        refresh_layout.setEnableLoadMore(true)
        refresh_layout.setEnableRefresh(true)
        refresh_layout.setOnRefreshListener { presenter.refreshPostList() }
        refresh_layout.setOnLoadMoreListener { presenter.LoadMorePost() }

        postListAdapter = PostListAdapter()
        rv_post_list.layoutManager = LinearLayoutManager(activity)
        rv_post_list.adapter = postListAdapter

        postListAdapter.setOnPostItemClickListener { ActivityUtil.turnToActivity(PostDetailActivity::class.java, it.postDetailUrl) }

        postListAdapter.setOnPostAvatarClickListener { ActivityUtil.turnToActivity(PostDetailActivity::class.java, it.postDetailUrl) }

        presenter.refreshPostList()
    }

    override fun onPostListRefresh(postList: MutableList<PostItem>)
    {
        refresh_layout.finishRefresh()
        postListAdapter.setNewData(postList)
    }

    override fun onPostListLoadMore(postList: MutableList<PostItem>)
    {
        refresh_layout.finishLoadMore()
        postListAdapter.addData(postList)
    }
}