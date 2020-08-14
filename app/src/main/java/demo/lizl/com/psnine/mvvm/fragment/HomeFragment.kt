package demo.lizl.com.psnine.mvvm.fragment

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.UiApplication
import demo.lizl.com.psnine.adapter.PostListAdapter
import demo.lizl.com.psnine.databinding.FragmentHomeBinding
import demo.lizl.com.psnine.mvvm.activity.PostDetailActivity
import demo.lizl.com.psnine.mvvm.base.BaseFragment
import demo.lizl.com.psnine.mvvm.viewmodel.PostViewModel
import demo.lizl.com.psnine.util.ActivityUtil
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home)
{
    override fun initView()
    {
        val viewModel = ViewModelProvider.AndroidViewModelFactory(UiApplication.instance).create(PostViewModel::class.java)

        refresh_layout.setOnRefreshListener { viewModel.refreshPostList() }
        refresh_layout.setOnLoadMoreListener { viewModel.loadMorePost() }

        val postListAdapter = PostListAdapter()
        dataBinding.postListAdapter = postListAdapter

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