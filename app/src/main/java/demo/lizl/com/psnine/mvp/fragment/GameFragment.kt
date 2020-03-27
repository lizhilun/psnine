package demo.lizl.com.psnine.mvp.fragment

import android.content.Context
import android.text.InputFilter
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.adapter.DiscountGameListAdapter
import demo.lizl.com.psnine.adapter.GameListAdapter
import demo.lizl.com.psnine.adapter.ViewPagerAdapter
import demo.lizl.com.psnine.bean.DiscountGameItem
import demo.lizl.com.psnine.bean.GameInfoItem
import demo.lizl.com.psnine.custom.function.addOnPageChangeListener
import demo.lizl.com.psnine.mvp.activity.BaseActivity
import demo.lizl.com.psnine.mvp.contract.GameFragmentContract
import demo.lizl.com.psnine.mvp.presenter.GameFragmentPresenter
import demo.lizl.com.psnine.util.DialogUtil
import demo.lizl.com.psnine.util.UiUtil
import kotlinx.android.synthetic.main.fragment_game.*

class GameFragment : BaseFragment<GameFragmentPresenter>(), GameFragmentContract.View
{
    private lateinit var hotGameListAdapter: GameListAdapter
    private lateinit var searchResultListAdapter: GameListAdapter
    private lateinit var discountGameListAdapter: DiscountGameListAdapter

    private val tabTitleList = mutableListOf<String>()

    private var hasNoMoreDiscountGame = false

    override fun getLayoutResId() = R.layout.fragment_game

    override fun initPresenter() = GameFragmentPresenter(this)

    override fun initView()
    {
        refresh_layout.setRefreshHeader(UiUtil.getDefaultRefreshHeader(activity as Context))
        refresh_layout.setRefreshFooter(UiUtil.getDefaultRefreshFooter(activity as Context))
        refresh_layout.setEnableRefresh(true)
        refresh_layout.setEnableLoadMore(false)
        refresh_layout.isNestedScrollingEnabled = false

        searchResultListAdapter = GameListAdapter()
        rv_search_result_list.layoutManager = LinearLayoutManager(activity)
        rv_search_result_list.adapter = searchResultListAdapter

        et_search.filters = arrayOf(UiUtil.getNoWrapInputFilter(), InputFilter.LengthFilter(15))

        tabTitleList.add(getString(R.string.recent_hot))
        tabTitleList.add(getString(R.string.discount_game))

        initViewPager()
        initTabLayout()
        initListener()

        showSearchView(false)

        presenter.refreshHitGameList()
        presenter.refreshDiscountGameList()
    }

    private fun initViewPager()
    {
        hotGameListAdapter = GameListAdapter()
        discountGameListAdapter = DiscountGameListAdapter()

        val hotGameRecyclerView = RecyclerView(activity as Context)
        hotGameRecyclerView.layoutManager = LinearLayoutManager(activity as Context)
        hotGameRecyclerView.adapter = hotGameListAdapter

        val discountGameRecyclerView = RecyclerView(activity as Context)
        discountGameRecyclerView.layoutManager = LinearLayoutManager(activity as Context)
        discountGameRecyclerView.adapter = discountGameListAdapter

        val recyclerViewList: List<RecyclerView> = listOf(hotGameRecyclerView, discountGameRecyclerView)
        val viewPagerAdapter = ViewPagerAdapter(recyclerViewList)
        viewPagerAdapter.setPageTitleList(tabTitleList)
        vp_game.adapter = viewPagerAdapter
    }

    private fun initTabLayout()
    {
        for (title in tabTitleList)
        {
            tab_game.addTab(tab_game.newTab().setText(title))
        }

        tab_game.setupWithViewPager(vp_game)
    }

    private fun initListener()
    {
        refresh_layout.setOnRefreshListener {
            presenter.refreshHitGameList()
            presenter.refreshDiscountGameList()
        }

        refresh_layout.setOnLoadMoreListener { loadMoreData() }

        searchResultListAdapter.setGameItemClickListener {
            (activity as BaseActivity<*>).turnToGameDetailActivity(it.gameDetailUrl)
        }

        hotGameListAdapter.setGameItemClickListener {
            (activity as BaseActivity<*>).turnToGameDetailActivity(it.gameDetailUrl)
        }

        discountGameListAdapter.setOnDiscountGameItemClickListener {
            DialogUtil.showOperationConfirmDialog(activity as Context, getString(R.string.title_sure_to_open_psn_store),
                    getString(R.string.notify_sure_to_open_psn_store)) {
                val psnGameUrl = "https://store.playstation.com/zh-hans-hk/product/${it.psnGameId}"
                UiUtil.turnToWebBrowser(activity as Context, psnGameUrl)
            }
        }

        et_search.addTextChangedListener {
            if (it.isNullOrBlank())
            {
                showSearchView(false)
            }
            else
            {
                showSearchView(true)
                presenter.searchGame(it.toString())
            }
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
        if (group_game.visibility == View.VISIBLE)
        {
            presenter.loadMoreDiscountGameList()
        }
        else
        {
            presenter.loadMoreSearchResult()
        }
    }

    private fun showSearchView(show: Boolean)
    {
        if (show)
        {
            group_game.visibility = View.GONE
            rv_search_result_list.visibility = View.VISIBLE
            refresh_layout.setEnableRefresh(false)
        }
        else
        {
            group_game.visibility = View.VISIBLE
            rv_search_result_list.visibility = View.GONE
            refresh_layout.setEnableRefresh(true)
            refresh_layout.setNoMoreData(vp_game.currentItem == 0 || hasNoMoreDiscountGame)
        }
    }

    override fun onHotGameListRefresh(hotGameList: List<GameInfoItem>)
    {
        refresh_layout.finishRefresh()

        hotGameListAdapter.setNewData(hotGameList.toMutableList())
    }

    override fun onDiscountGameListRefresh(discountGameList: List<DiscountGameItem>, totalCount: Int)
    {
        refresh_layout.finishRefresh()

        discountGameListAdapter.setNewData(discountGameList.toMutableList())

        refresh_layout.setEnableLoadMore(true)
        hasNoMoreDiscountGame = discountGameListAdapter.data.size >= totalCount
        refresh_layout.setNoMoreData(hasNoMoreDiscountGame)
    }

    override fun onDiscountGameListLoadMore(discountGameList: List<DiscountGameItem>, totalCount: Int)
    {
        refresh_layout.finishLoadMore()

        discountGameListAdapter.addData(discountGameList)
        hasNoMoreDiscountGame = discountGameListAdapter.data.size >= totalCount
        refresh_layout.setNoMoreData(hasNoMoreDiscountGame)
    }

    override fun onGameSearchRefresh(gameList: List<GameInfoItem>, resultTotalCount: Int)
    {
        searchResultListAdapter.setNewData(gameList.toMutableList())

        rv_search_result_list.scrollToPosition(0)
        refresh_layout.setEnableLoadMore(true)
        refresh_layout.setNoMoreData(searchResultListAdapter.data.size >= resultTotalCount)
    }

    override fun onGameSearchLoadMore(gameList: List<GameInfoItem>, resultTotalCount: Int)
    {
        refresh_layout.finishLoadMore()

        searchResultListAdapter.addData(gameList)
        refresh_layout.setNoMoreData(searchResultListAdapter.data.size >= resultTotalCount)
    }
}