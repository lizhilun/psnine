package demo.lizl.com.psnine.mvp.fragment

import android.content.Context
import android.text.TextUtils
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.jeremyliao.liveeventbus.LiveEventBus
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.adapter.GameListAdapter
import demo.lizl.com.psnine.bean.GameInfoItem
import demo.lizl.com.psnine.bean.UserGameInfoItem
import demo.lizl.com.psnine.bean.UserInfoItem
import demo.lizl.com.psnine.constant.AppConstant
import demo.lizl.com.psnine.constant.EventConstant
import demo.lizl.com.psnine.mvp.activity.GameDetailActivity
import demo.lizl.com.psnine.mvp.activity.LoginActivity
import demo.lizl.com.psnine.mvp.contract.UserFragmentContract
import demo.lizl.com.psnine.mvp.presenter.UserFragmentPresenter
import demo.lizl.com.psnine.util.ActivityUtil
import demo.lizl.com.psnine.util.DialogUtil
import demo.lizl.com.psnine.util.GlideUtil
import kotlinx.android.synthetic.main.fragment_user.*


class UserFragment : BaseFragment<UserFragmentPresenter>(), UserFragmentContract.View
{
    private lateinit var gameListAdapter: GameListAdapter

    override fun getLayoutResId() = R.layout.fragment_user

    override fun initPresenter() = UserFragmentPresenter(this)

    override fun initView()
    {
        val psnId = activity?.intent?.extras?.getString(AppConstant.BUNDLE_DATA_STRING, "").orEmpty()

        val emptyPsnId = TextUtils.isEmpty(psnId)
        iv_back.isVisible = !emptyPsnId
        fab_synchronize_level.isVisible = emptyPsnId
        fab_synchronize_game.isVisible = emptyPsnId
        presenter.bindPsnId(psnId)

        gameListAdapter = GameListAdapter()
        rv_game_list.layoutManager = LinearLayoutManager(activity)
        rv_game_list.adapter = gameListAdapter

        refresh_layout.setEnableRefresh(false)
        refresh_layout.setEnableLoadMore(false)
        refresh_layout.isNestedScrollingEnabled = false
        refresh_layout.setOnRefreshListener { presenter.refreshUserPage() }
        refresh_layout.setOnLoadMoreListener { presenter.loadMoreGameList() }

        fam_menu.setClosedOnTouchOutside(true)

        fab_synchronize_level.setOnClickListener {
            DialogUtil.showLoadingDialog(activity as Context)
            presenter.updateUserLevel()
            fam_menu.close(true)
        }

        fab_synchronize_game.setOnClickListener {
            DialogUtil.showLoadingDialog(activity as Context)
            presenter.updateUserGame()
            fam_menu.close(true)
        }

        fab_sort_condition.setOnClickListener {
            showGameSortConditionDialog()
            fam_menu.close(true)
        }

        iv_back.setOnClickListener { activity?.onBackPressed() }

        gameListAdapter.setGameItemClickListener { ActivityUtil.turnToActivity(GameDetailActivity::class.java, it.gameDetailUrl) }

        LiveEventBus.get(EventConstant.EVENT_LOGIN_RESULT, Boolean::class.java).observe(this, Observer {
            if (it) presenter.refreshUserPage()
        })

        presenter.refreshUserPage()
    }

    override fun onUserInfoRefresh(userInfoItem: UserInfoItem)
    {
        refresh_layout.finishRefresh()
        GlideUtil.displayImage(iv_avatar, userInfoItem.avatarUrl)
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

    override fun onUserGameListUpdate(gameList: MutableList<GameInfoItem>, gameTotalCount: Int)
    {
        refresh_layout.finishRefresh()

        gameListAdapter.setNewData(gameList)

        refresh_layout.setEnableLoadMore(true)
        refresh_layout.setNoMoreData(gameListAdapter.data.size >= gameTotalCount)
    }

    override fun onMoreGameLoadFinish(gameList: MutableList<GameInfoItem>, gameTotalCount: Int)
    {
        refresh_layout.finishLoadMore()

        gameListAdapter.addData(gameList)

        refresh_layout.setNoMoreData(gameListAdapter.data.size >= gameTotalCount)
    }

    override fun onInfoUpdateFinish()
    {
        DialogUtil.dismissDialog()
        ToastUtils.showShort(R.string.notify_success_to_update_info)
        presenter.refreshUserPage()
    }

    override fun onInfoUpdateFailed(reason: String)
    {
        DialogUtil.showOperationConfirmDialog(activity as Context, getString(R.string.notify_failed_to_update_info), reason) {
            if (reason == getString(R.string.notify_need_login_first))
            {
                ActivityUtil.turnToActivity(LoginActivity::class.java)
            }
        }
    }

    private fun showGameSortConditionDialog()
    {
        DialogUtil.showGameSortConditionDialog(activity as Context) { gamePlatform, sortCondition ->
            val platform = when (gamePlatform)
            {
                getString(R.string.all) -> UserFragmentPresenter.GAME_PLATFORM_ALL
                "PSV"                   -> UserFragmentPresenter.GAME_PLATFORM_PSV
                "PS3"                   -> UserFragmentPresenter.GAME_PLATFORM_PS3
                "PS4"                   -> UserFragmentPresenter.GAME_PLATFORM_PS4
                else                    -> UserFragmentPresenter.GAME_PLATFORM_ALL
            }
            val condition = when (sortCondition)
            {
                getString(R.string.newest)             -> UserFragmentPresenter.SORT_GAME_BY_TIME
                getString(R.string.fastest_completion) -> UserFragmentPresenter.SORT_GAME_BY_FASTEST_PROGRESS
                getString(R.string.slowest_completion) -> UserFragmentPresenter.SORT_GAME_BY_SLOWEST_PROGRESS
                getString(R.string.perfect_difficult)  -> UserFragmentPresenter.SORT_GAME_BY_PERFECT_DIFFICULT
                else                                   -> UserFragmentPresenter.SORT_GAME_BY_TIME
            }
            presenter.refreshGameList(platform, condition)
        }
    }
}