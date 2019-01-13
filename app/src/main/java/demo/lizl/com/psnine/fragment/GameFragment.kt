package demo.lizl.com.psnine.fragment

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.activity.BaseActivity
import demo.lizl.com.psnine.adapter.GameListAdapter
import demo.lizl.com.psnine.iview.IGameFragmentView
import demo.lizl.com.psnine.model.GameInfoItem
import demo.lizl.com.psnine.presenter.GameFragmentPresenter
import demo.lizl.com.psnine.util.UiUtil
import kotlinx.android.synthetic.main.fragment_game.*

class GameFragment : BaseFragment(), IGameFragmentView
{

    private lateinit var hotGameListAdapter: GameListAdapter
    private lateinit var searchResultListAdapter: GameListAdapter

    override fun getLayoutResId(): Int
    {
        return R.layout.fragment_game
    }

    override fun initPresenter()
    {
        presenter = GameFragmentPresenter(activity as Context, this)
        getPresenter().getHitGameList()
    }

    private fun getPresenter() = presenter as GameFragmentPresenter

    override fun initView()
    {
        refresh_layout.setRefreshHeader(UiUtil.getDefaultRefreshHeader(activity as Context))
        refresh_layout.setRefreshFooter(UiUtil.getDefaultRefreshFooter(activity as Context))
        refresh_layout.setEnableRefresh(false)
        refresh_layout.setEnableLoadMore(false)
        refresh_layout.isNestedScrollingEnabled = false
        refresh_layout.setOnLoadMoreListener { getPresenter().loadMoreSearchResult() }

        searchResultListAdapter = GameListAdapter()
        rv_search_result_list.layoutManager = LinearLayoutManager(activity)
        rv_search_result_list.adapter = searchResultListAdapter

        hotGameListAdapter = GameListAdapter()
        rv_hot_game_list.layoutManager = LinearLayoutManager(activity)
        rv_hot_game_list.adapter = hotGameListAdapter

        searchResultListAdapter.setGameItemClickListener(object : GameListAdapter.GameItemClickListener
        {
            override fun onGameItemClick(gameInfoItem: GameInfoItem)
            {
                (activity as BaseActivity).turnToGameDetailActivity(gameInfoItem.gameDetailUrl)
            }
        })

        hotGameListAdapter.setGameItemClickListener(object : GameListAdapter.GameItemClickListener
        {
            override fun onGameItemClick(gameInfoItem: GameInfoItem)
            {
                (activity as BaseActivity).turnToGameDetailActivity(gameInfoItem.gameDetailUrl)
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

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int)
            {
                getPresenter().searchGame(p0!!.toString())
            }
        })
        et_search.filters = arrayOf(UiUtil.getNoWrapInputFilter(), InputFilter.LengthFilter(15))
    }

    override fun onHotGameListRefresh(hotGameList: List<GameInfoItem>)
    {
        group_hot_game.visibility = View.VISIBLE
        rv_search_result_list.visibility = View.GONE
        hotGameListAdapter.clear()
        hotGameListAdapter.addAll(hotGameList)
    }

    override fun onGameSearchRefresh(gameList: List<GameInfoItem>, resultTotalCount: Int)
    {
        group_hot_game.visibility = View.GONE
        rv_search_result_list.visibility = View.VISIBLE
        searchResultListAdapter.clear()
        searchResultListAdapter.addAll(gameList)

        rv_search_result_list.scrollToPosition(0)
        refresh_layout.setEnableLoadMore(searchResultListAdapter.data.size < resultTotalCount)
    }

    override fun onGameSearchLoadMore(gameList: List<GameInfoItem>, resultTotalCount: Int)
    {
        refresh_layout.finishLoadMore()

        searchResultListAdapter.insertAll(gameList, searchResultListAdapter.data.size)
        refresh_layout.setEnableLoadMore(searchResultListAdapter.data.size < resultTotalCount)
    }
}