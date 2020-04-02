package demo.lizl.com.psnine.mvvm.fragment

import android.content.Context
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.UiApplication
import demo.lizl.com.psnine.adapter.DiscountGameListAdapter
import demo.lizl.com.psnine.adapter.GameListAdapter
import demo.lizl.com.psnine.adapter.ViewPagerAdapter
import demo.lizl.com.psnine.custom.function.addOnPageChangeListener
import demo.lizl.com.psnine.databinding.FragmentGameBinding
import demo.lizl.com.psnine.mvvm.activity.GameDetailActivity
import demo.lizl.com.psnine.mvvm.base.BaseFragment
import demo.lizl.com.psnine.mvvm.viewmodel.DiscountViewModel
import demo.lizl.com.psnine.mvvm.viewmodel.GameSearchViewModel
import demo.lizl.com.psnine.mvvm.viewmodel.HotGameViewModel
import demo.lizl.com.psnine.util.ActivityUtil
import demo.lizl.com.psnine.util.DialogUtil
import demo.lizl.com.psnine.util.UiUtil
import kotlinx.android.synthetic.main.fragment_game.*

class GameFragment : BaseFragment<FragmentGameBinding>()
{
    private val hotGameListAdapter = GameListAdapter()
    private val searchResultListAdapter = GameListAdapter()
    private val discountGameListAdapter = DiscountGameListAdapter()

    private val hotGameViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(UiApplication.instance).create(HotGameViewModel::class.java)
    private val gameSearchViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(UiApplication.instance).create(GameSearchViewModel::class.java)
    private val discountViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(UiApplication.instance).create(DiscountViewModel::class.java)

    private var hasNoMoreDiscountGame = false

    override fun getLayoutResId() = R.layout.fragment_game

    override fun initView()
    {
        refresh_layout.setEnableRefresh(true)
        refresh_layout.isNestedScrollingEnabled = false

        rv_search_result_list.layoutManager = LinearLayoutManager(activity)
        rv_search_result_list.adapter = searchResultListAdapter

        val hotGameRecyclerView = RecyclerView(activity as Context)
        hotGameRecyclerView.layoutManager = LinearLayoutManager(activity as Context)
        hotGameRecyclerView.adapter = hotGameListAdapter

        val discountGameRecyclerView = RecyclerView(activity as Context)
        discountGameRecyclerView.layoutManager = LinearLayoutManager(activity as Context)
        discountGameRecyclerView.adapter = discountGameListAdapter

        val viewPagerAdapter = ViewPagerAdapter(listOf(hotGameRecyclerView, discountGameRecyclerView))
        viewPagerAdapter.setPageTitleList(mutableListOf<String>().apply {
            add(getString(R.string.recent_hot))
            add(getString(R.string.discount_game))
            forEach { tab_game.addTab(tab_game.newTab().setText(it)) }
        })
        vp_game.adapter = viewPagerAdapter
        tab_game.setupWithViewPager(vp_game)

        initListener()
        initData()

        showSearchView(false)

        hotGameViewModel.refreshHitGameList()
        discountViewModel.refreshDiscountGameList()
    }

    private fun initData()
    {
        hotGameViewModel.getHotGameLiveData().observe(this, Observer {
            refresh_layout.finishLoadMore()
            refresh_layout.finishRefresh()
            hotGameListAdapter.setDiffNewData(it)
        })

        gameSearchViewModel.getSearchResultLiveData().observe(this, Observer {
            refresh_layout.finishLoadMore()
            refresh_layout.finishRefresh()
            refresh_layout.setEnableLoadMore(true)
            refresh_layout.setNoMoreData(it.size >= gameSearchViewModel.getSearchResultCountLiveData().value ?: 0)
            searchResultListAdapter.setDiffNewData(it)
        })

        discountViewModel.getDiscountLiveData().observe(this, Observer {
            refresh_layout.finishLoadMore()
            refresh_layout.finishRefresh()
            refresh_layout.setEnableLoadMore(true)
            hasNoMoreDiscountGame = it.size >= discountViewModel.getDiscountCountLiveData().value ?: 0
            refresh_layout.setNoMoreData(hasNoMoreDiscountGame)
            discountGameListAdapter.setDiffNewData(it)
        })
    }

    private fun initListener()
    {
        refresh_layout.setOnRefreshListener {
            hotGameViewModel.refreshHitGameList()
            discountViewModel.refreshDiscountGameList()
        }

        refresh_layout.setOnLoadMoreListener { loadMoreData() }

        searchResultListAdapter.setGameItemClickListener { ActivityUtil.turnToActivity(GameDetailActivity::class.java, it.gameDetailUrl) }

        hotGameListAdapter.setGameItemClickListener { ActivityUtil.turnToActivity(GameDetailActivity::class.java, it.gameDetailUrl) }

        discountGameListAdapter.setOnDiscountGameItemClickListener {
            DialogUtil.showOperationConfirmDialog(activity as Context, getString(R.string.title_sure_to_open_psn_store),
                    getString(R.string.notify_sure_to_open_psn_store)) {
                val psnGameUrl = "https://store.playstation.com/zh-hans-hk/product/${it.psnGameId}"
                UiUtil.turnToWebBrowser(activity as Context, psnGameUrl)
            }
        }

        et_search.addTextChangedListener {
            showSearchView(!it.isNullOrBlank())
            gameSearchViewModel.searchGame(it.toString())
        }

        vp_game.addOnPageChangeListener {
            when (it)
            {
                0 -> refresh_layout.setNoMoreData(true)
                1 -> refresh_layout.setNoMoreData(hasNoMoreDiscountGame)
            }
        }
    }

    private fun loadMoreData()
    {
        if (group_game.isVisible)
        {
            discountViewModel.loadMoreDiscountGameList()
        }
        else
        {
            gameSearchViewModel.loadMoreSearchResult()
        }
    }

    private fun showSearchView(show: Boolean)
    {
        group_game.isVisible = !show
        refresh_layout.setEnableRefresh(!show)
        rv_search_result_list.isVisible = show
        if (!show) refresh_layout.setNoMoreData(vp_game.currentItem == 0 || hasNoMoreDiscountGame)
    }
}