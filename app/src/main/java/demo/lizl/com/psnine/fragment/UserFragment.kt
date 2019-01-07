package demo.lizl.com.psnine.fragment

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.activity.MainActivity
import demo.lizl.com.psnine.adapter.GameListAdapter
import demo.lizl.com.psnine.customview.dialog.DialogGameSortCondition
import demo.lizl.com.psnine.iview.IUserFragmentView
import demo.lizl.com.psnine.model.GameInfoItem
import demo.lizl.com.psnine.model.UserGameInfoItem
import demo.lizl.com.psnine.model.UserInfoItem
import demo.lizl.com.psnine.presenter.UserFragmentPresenter
import demo.lizl.com.psnine.util.Constant
import demo.lizl.com.psnine.util.GlideUtil
import demo.lizl.com.psnine.util.UiUtil
import kotlinx.android.synthetic.main.fragment_user.*


class UserFragment : BaseFragment(), IUserFragmentView
{

    private lateinit var gameListAdapter: GameListAdapter

    private lateinit var dialogGameSortCondition: DialogGameSortCondition

    override fun getLayoutResId(): Int
    {
        return R.layout.fragment_user
    }

    private fun getPresenter() = presenter as UserFragmentPresenter

    override fun initPresenter()
    {
        presenter = UserFragmentPresenter(activity as Context, this)
    }

    override fun initView()
    {
        val bundle = activity!!.intent.extras
        if (bundle != null)
        {
            val psnId = bundle.getString(Constant.BUNDLE_DATA_STRING, "")
            getPresenter().setPsnId(psnId)
        }

        getPresenter().refreshUserPage()

        gameListAdapter = GameListAdapter()
        rv_game_list.layoutManager = LinearLayoutManager(activity)
        rv_game_list.adapter = gameListAdapter

        dialogGameSortCondition = DialogGameSortCondition(activity as Context)
        dialogGameSortCondition.setOnConfirmButtonClickListener(object : DialogGameSortCondition.OnConfirmButtonClickListener
        {
            override fun onConfirmButtonClick(gamePlatform: String, sortCondition: String)
            {
                val platform = when (gamePlatform)
                {
                    getString(R.string.all) -> UserFragmentPresenter.GAME_PLATFORM_ALL
                    "PSV" -> UserFragmentPresenter.GAME_PLATFORM_PSV
                    "PS3" -> UserFragmentPresenter.GAME_PLATFORM_PS3
                    "PS4" -> UserFragmentPresenter.GAME_PLATFORM_PS4
                    else -> UserFragmentPresenter.GAME_PLATFORM_ALL
                }
                val condition = when (sortCondition)
                {
                    getString(R.string.newest) -> UserFragmentPresenter.SORT_GAME_BY_TIME
                    getString(R.string.fastest_completion) -> UserFragmentPresenter.SORT_GAME_BY_FASTEST_PROGRESE
                    getString(R.string.slowest_completion) -> UserFragmentPresenter.SORT_GAME_BY_SLOWEST_PROGRESE
                    getString(R.string.perfect_difficult) -> UserFragmentPresenter.SORT_GAME_BY_PERFECT_DIFFICULT
                    else -> UserFragmentPresenter.SORT_GAME_BY_TIME
                }
                getPresenter().refreshGameList(platform, condition)
            }
        })

        refresh_layout.setRefreshHeader(UiUtil.getDefaultRefreshHeader(activity as Context))
        refresh_layout.setRefreshFooter(UiUtil.getDefaultRefreshFooter(activity as Context))
        refresh_layout.setEnableRefresh(true)
        refresh_layout.isNestedScrollingEnabled = false
        refresh_layout.setOnRefreshListener { getPresenter().refreshUserPage() }
        refresh_layout.setOnLoadMoreListener { getPresenter().loadMoreGameList() }

        fam_menu.setClosedOnTouchOutside(true)

        fab_synchronize_level.setOnClickListener {
            getPresenter().updateUserLevel()
            fam_menu.close(true)
        }

        fab_synchronize_game.setOnClickListener {
            getPresenter().updateUserGame()
            fam_menu.close(true)
        }

        fab_sort_condition.setOnClickListener {
            dialogGameSortCondition.show()
            fam_menu.close(true)
        }

        gameListAdapter.setGameItemClickListener(object : GameListAdapter.GameItemClickListener
        {
            override fun onGameItemClick(gameInfoItem: GameInfoItem)
            {
                (activity as MainActivity).turnToGameDetailActivity(gameInfoItem.gameDetailUrl)
            }
        })
    }

    override fun onUserInfoRefresh(userInfoItem: UserInfoItem)
    {
        refresh_layout.finishRefresh()
        GlideUtil.displayImage(activity as Context, userInfoItem.avatarUrl, iv_avatar)
        tv_user_account.text = userInfoItem.userId
        tv_user_experience.text = userInfoItem.userLevel
        tv_user_cup.text = userInfoItem.userCupInfo
    }

    override fun onUserGameInfoRefresh(userGameInfoItem: UserGameInfoItem)
    {
        refresh_layout.finishRefresh()

        tv_game_total_number.text = userGameInfoItem.totalCount.toString()
        tv_game_perfect_number.text = userGameInfoItem.perfectCount.toString()
        tv_game_pit_number.text = userGameInfoItem.pitCount.toString()
        tv_game_completion_rate.text = userGameInfoItem.completionRate
        tv_cup_total_number.text = userGameInfoItem.cupCount.toString()
    }

    override fun onUserGameListUpdate(gameList: List<GameInfoItem>)
    {
        refresh_layout.finishRefresh()

        gameListAdapter.clear()
        gameListAdapter.addAll(gameList)

        refresh_layout.setEnableLoadMore(true)
    }

    override fun onMoreGameLoadFinish(gameList: List<GameInfoItem>)
    {
        refresh_layout.finishLoadMore()

        gameListAdapter.insertAll(gameList, gameListAdapter.data.size)
    }

    override fun onNoMoreGame()
    {
        refresh_layout.setEnableLoadMore(false)
    }
}