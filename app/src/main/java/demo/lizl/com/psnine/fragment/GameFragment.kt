package demo.lizl.com.psnine.fragment

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.activity.MainActivity
import demo.lizl.com.psnine.adapter.GameListAdapter
import demo.lizl.com.psnine.iview.IGameFragmentView
import demo.lizl.com.psnine.model.GameInfoItem
import demo.lizl.com.psnine.presenter.GameFragmentPresenter
import demo.lizl.com.psnine.util.UiUtil
import kotlinx.android.synthetic.main.fragment_game.*

class GameFragment : BaseFragment(), IGameFragmentView
{

    private lateinit var gameListAdapter: GameListAdapter

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
        gameListAdapter = GameListAdapter()
        rv_game_list.layoutManager = LinearLayoutManager(activity)
        rv_game_list.adapter = gameListAdapter

        gameListAdapter.setGameItemClickListener(object : GameListAdapter.GameItemClickListener
        {
            override fun onGameItemClick(gameInfoItem: GameInfoItem)
            {
                (activity as MainActivity).turnToGameDetailActivity(gameInfoItem.gameDetailUrl)
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
        tv_hot.visibility = View.VISIBLE
        gameListAdapter.clear()
        gameListAdapter.addAll(hotGameList)
    }

    override fun onGameSearchRefresh(gameList: List<GameInfoItem>)
    {
        tv_hot.visibility = View.GONE
        gameListAdapter.clear()
        gameListAdapter.addAll(gameList)
    }
}