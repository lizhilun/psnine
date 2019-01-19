package demo.lizl.com.psnine.fragment

import android.content.Context
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.activity.BaseActivity
import demo.lizl.com.psnine.adapter.DiscountGameListAdapter
import demo.lizl.com.psnine.adapter.GameListAdapter
import demo.lizl.com.psnine.adapter.ViewPagerAdapter
import demo.lizl.com.psnine.customview.dialog.BaseDialog
import demo.lizl.com.psnine.customview.dialog.DialogOperationConfirm
import demo.lizl.com.psnine.iview.IGameFragmentView
import demo.lizl.com.psnine.model.DiscountGameItem
import demo.lizl.com.psnine.model.GameInfoItem
import demo.lizl.com.psnine.presenter.GameFragmentPresenter
import demo.lizl.com.psnine.util.UiUtil
import kotlinx.android.synthetic.main.fragment_game.*

class GameFragment : BaseFragment<GameFragmentPresenter>(), IGameFragmentView
{
    private lateinit var hotGameListAdapter: GameListAdapter
    private lateinit var searchResultListAdapter: GameListAdapter
    private lateinit var discountGameListAdapter: DiscountGameListAdapter

    private var dialogOperationConfirm: DialogOperationConfirm? = null

    private val tabTitleList = mutableListOf<String>()

    private var hasNoMoreDiscountGame = false

    override fun getLayoutResId(): Int
    {
        return R.layout.fragment_game
    }

    override fun initPresenter()
    {
        presenter = GameFragmentPresenter(activity as Context, this)
    }

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

        presenter.getHitGameList()
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
            presenter.getHitGameList()
            presenter.refreshDiscountGameList()
        }

        refresh_layout.setOnLoadMoreListener { loadMoreData() }

        searchResultListAdapter.setGameItemClickListener(object : GameListAdapter.GameItemClickListener
        {
            override fun onGameItemClick(gameInfoItem: GameInfoItem)
            {
                (activity as BaseActivity<*>).turnToGameDetailActivity(gameInfoItem.gameDetailUrl)
            }
        })

        hotGameListAdapter.setGameItemClickListener(object : GameListAdapter.GameItemClickListener
        {
            override fun onGameItemClick(gameInfoItem: GameInfoItem)
            {
                (activity as BaseActivity<*>).turnToGameDetailActivity(gameInfoItem.gameDetailUrl)
            }
        })

        discountGameListAdapter.setOnDiscountGameItemClickListener(object : DiscountGameListAdapter.OnDiscountGameItemClickListener
        {
            override fun onDiscountGameItemClick(discountGameItem: DiscountGameItem)
            {
                dialogOperationConfirm = DialogOperationConfirm(
                        activity as Context, getString(R.string.title_sure_to_open_psn_store), getString(R.string.notify_sure_to_open_psn_store)
                )

                dialogOperationConfirm?.setOnConfirmButtonClickListener(object : BaseDialog.OnConfirmButtonClickListener
                {
                    override fun onConfirmButtonClick()
                    {
                        val psnGameUrl = "https://store.playstation.com/zh-hans-hk/product/" + discountGameItem.psnGameId
                        UiUtil.turnToWebBrowser(activity as Context, psnGameUrl)
                    }
                })
                dialogOperationConfirm?.show()
            }
        })

        et_search.addTextChangedListener(object : TextWatcher
        {
            override fun afterTextChanged(p0: Editable?)
            {
                //do nothing
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int)
            {
                //do nothing
            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int)
            {
                if (TextUtils.isEmpty(p0.toString()))
                {
                    showSearchView(false)
                }
                else
                {
                    showSearchView(true)
                    presenter.searchGame(p0.toString())
                }
            }
        })

        vp_game.addOnPageChangeListener(object : ViewPager.OnPageChangeListener
        {
            override fun onPageScrollStateChanged(p0: Int)
            {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int)
            {
            }

            override fun onPageSelected(p0: Int)
            {
                when (p0)
                {
                    0 -> refresh_layout.setNoMoreData(true)
                    1 -> refresh_layout.setNoMoreData(hasNoMoreDiscountGame)
                }
            }
        })
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

        hotGameListAdapter.clear()
        hotGameListAdapter.addAll(hotGameList)
    }

    override fun onDiscountGameListRefresh(discountGameList: List<DiscountGameItem>, totalCount: Int)
    {
        refresh_layout.finishRefresh()

        discountGameListAdapter.clear()
        discountGameListAdapter.addAll(discountGameList)

        refresh_layout.setEnableLoadMore(true)
        hasNoMoreDiscountGame = discountGameListAdapter.data.size >= totalCount
        refresh_layout.setNoMoreData(hasNoMoreDiscountGame)
    }

    override fun onDiscountGameListLoadMore(discountGameList: List<DiscountGameItem>, totalCount: Int)
    {
        refresh_layout.finishLoadMore()

        discountGameListAdapter.insertAll(discountGameList, discountGameListAdapter.data.size)
        hasNoMoreDiscountGame = discountGameListAdapter.data.size >= totalCount
        refresh_layout.setNoMoreData(hasNoMoreDiscountGame)
    }

    override fun onGameSearchRefresh(gameList: List<GameInfoItem>, resultTotalCount: Int)
    {
        searchResultListAdapter.clear()
        searchResultListAdapter.addAll(gameList)

        rv_search_result_list.scrollToPosition(0)
        refresh_layout.setEnableLoadMore(true)
        refresh_layout.setNoMoreData(searchResultListAdapter.data.size >= resultTotalCount)
    }

    override fun onGameSearchLoadMore(gameList: List<GameInfoItem>, resultTotalCount: Int)
    {
        refresh_layout.finishLoadMore()

        searchResultListAdapter.insertAll(gameList, searchResultListAdapter.data.size)
        refresh_layout.setNoMoreData(searchResultListAdapter.data.size >= resultTotalCount)
    }

    override fun onStop()
    {
        super.onStop()

        dialogOperationConfirm?.dismiss()
    }
}