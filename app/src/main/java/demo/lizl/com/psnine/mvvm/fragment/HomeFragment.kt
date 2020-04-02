package demo.lizl.com.psnine.mvvm.fragment

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.UiApplication
import demo.lizl.com.psnine.adapter.PostListAdapter
import demo.lizl.com.psnine.databinding.FragmentHomeBinding
import demo.lizl.com.psnine.mvvm.activity.PostDetailActivity
import demo.lizl.com.psnine.mvvm.base.BaseFragment
import demo.lizl.com.psnine.mvvm.viewmodel.PostViewModel
import demo.lizl.com.psnine.util.ActivityUtil
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment<FragmentHomeBinding>()
{
    private lateinit var postListAdapter: PostListAdapter

    override fun getLayoutResId() = R.layout.fragment_home

    override fun initView()
    {
        val viewModel = ViewModelProvider.AndroidViewModelFactory(UiApplication.instance).create(PostViewModel::class.java)

        refresh_layout.setEnableLoadMore(true)
        refresh_layout.setEnableRefresh(true)
        refresh_layout.setEnableAutoLoadMore(true)
        refresh_layout.setOnRefreshListener { viewModel.refreshPostList() }
        refresh_layout.setOnLoadMoreListener { viewModel.loadMorePost() }

        postListAdapter = PostListAdapter()
        rv_post_list.layoutManager = LinearLayoutManager(activity)
        rv_post_list.adapter = postListAdapter

        postListAdapter.setOnPostItemClickListener { ActivityUtil.turnToActivity(PostDetailActivity::class.java, it.postDetailUrl) }

        postListAdapter.setOnPostAvatarClickListener { ActivityUtil.turnToActivity(PostDetailActivity::class.java, it.postDetailUrl) }

        viewModel.refreshPostList()

        viewModel.getPostLiveData().observe(this, Observer {
            refresh_layout.finishRefresh()
            refresh_layout.finishLoadMore()
            postListAdapter.setDiffNewData(it.toMutableList())
        })
    }
}