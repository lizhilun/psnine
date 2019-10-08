package demo.lizl.com.psnine.mvp.fragment

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.View
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.adapter.GameListAdapter
import demo.lizl.com.psnine.bean.GameInfoItem
import demo.lizl.com.psnine.bean.UserGameInfoItem
import demo.lizl.com.psnine.bean.UserInfoItem
import demo.lizl.com.psnine.customview.dialog.BaseDialog
import demo.lizl.com.psnine.customview.dialog.DialogGameSortCondition
import demo.lizl.com.psnine.customview.dialog.DialogLoading
import demo.lizl.com.psnine.customview.dialog.DialogOperationConfirm
import demo.lizl.com.psnine.event.LoginEvent
import demo.lizl.com.psnine.mvp.activity.BaseActivity
import demo.lizl.com.psnine.mvp.activity.UserDetailActivity
import demo.lizl.com.psnine.mvp.contract.UserFragmentContract
import demo.lizl.com.psnine.mvp.presenter.UserFragmentPresenter
import demo.lizl.com.psnine.util.Constant
import demo.lizl.com.psnine.util.GlideUtil
import demo.lizl.com.psnine.util.ToastUtil
import demo.lizl.com.psnine.util.UiUtil
import kotlinx.android.synthetic.main.fragment_user.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class UserFragment : BaseFragment<UserFragmentPresenter>(), UserFragmentContract.View
{
    private lateinit var gameListAdapter: GameListAdapter

    private var dialogLoading: DialogLoading? = null

    private var dialogOperationConfirm: DialogOperationConfirm? = null

    private var dialogGameSortCondition: DialogGameSortCondition? = null

    override fun getLayoutResId(): Int
    {
        return R.layout.fragment_user
    }

    override fun isNeedRegisterEventBus() = true

    override fun initPresenter() = UserFragmentPresenter(activity as Context, this)

    override fun initView()
    {
        var psnId = ""
        val bundle = activity!!.intent.extras
        if (bundle != null)
        {
            psnId = bundle.getString(Constant.BUNDLE_DATA_STRING, "")
        }

        val emptyPsnId = TextUtils.isEmpty(psnId)
        iv_back.visibility = if (emptyPsnId) View.GONE else View.VISIBLE
        fab_synchronize_level.visibility = if (emptyPsnId) View.VISIBLE else View.GONE
        fab_synchronize_game.visibility = if (emptyPsnId) View.VISIBLE else View.GONE
        presenter.setPsnId(psnId)

        gameListAdapter = GameListAdapter()
        rv_game_list.layoutManager = LinearLayoutManager(activity)
        rv_game_list.adapter = gameListAdapter

        refresh_layout.setRefreshHeader(UiUtil.getDefaultRefreshHeader(activity as Context))
        refresh_layout.setRefreshFooter(UiUtil.getDefaultRefreshFooter(activity as Context))
        refresh_layout.setEnableRefresh(false)
        refresh_layout.setEnableLoadMore(false)
        refresh_layout.isNestedScrollingEnabled = false
        refresh_layout.setOnRefreshListener { presenter.refreshUserPage() }
        refresh_layout.setOnLoadMoreListener { presenter.loadMoreGameList() }

        fam_menu.setClosedOnTouchOutside(true)

        fab_synchronize_level.setOnClickListener {
            showLoadingDialog()
            presenter.updateUserLevel()
            fam_menu.close(true)
        }

        fab_synchronize_game.setOnClickListener {
            showLoadingDialog()
            presenter.updateUserGame()
            fam_menu.close(true)
        }

        fab_sort_condition.setOnClickListener {
            showGameSortConditionDialog()
            fam_menu.close(true)
        }

        iv_back.setOnClickListener {
            if (activity is UserDetailActivity)
            {
                (activity as UserDetailActivity).finish()
            }
        }

        gameListAdapter.setGameItemClickListener {
            (activity as BaseActivity<*>).turnToGameDetailActivity(it.gameDetailUrl)
        }

        presenter.refreshUserPage()
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

    override fun onUserGameListUpdate(gameList: List<GameInfoItem>, gameTotalCount: Int)
    {
        refresh_layout.finishRefresh()

        gameListAdapter.clear()
        gameListAdapter.addAll(gameList)

        refresh_layout.setEnableLoadMore(true)
        refresh_layout.setNoMoreData(gameListAdapter.data.size >= gameTotalCount)
    }

    override fun onMoreGameLoadFinish(gameList: List<GameInfoItem>, gameTotalCount: Int)
    {
        refresh_layout.finishLoadMore()

        gameListAdapter.insertAll(gameList, gameListAdapter.data.size)

        refresh_layout.setNoMoreData(gameListAdapter.data.size >= gameTotalCount)
    }

    override fun onInfoUpdateFinish()
    {
        dialogLoading?.dismiss()
        ToastUtil.showToast(R.string.notify_success_to_update_info)
        presenter.refreshUserPage()
    }

    override fun onInfoUpdateFailed(reason: String)
    {
        dialogLoading?.dismiss()

        dialogOperationConfirm = DialogOperationConfirm(activity as Context, getString(R.string.notify_failed_to_update_info), reason)
        dialogOperationConfirm?.show()

        dialogOperationConfirm?.setOnConfirmButtonClickListener(object : BaseDialog.OnConfirmButtonClickListener
        {
            override fun onConfirmButtonClick()
            {
                if (reason == getString(R.string.notify_need_login_first))
                {
                    (activity as BaseActivity<*>).turnToLoginActivity()
                }
            }
        })
    }

    private fun showLoadingDialog()
    {
        if (dialogLoading == null)
        {
            dialogLoading = DialogLoading(activity as Context)
        }
        dialogLoading?.show()
    }

    @Subscribe(threadMode = ThreadMode.MAIN) fun onLoginResponse(loginEvent: LoginEvent)
    {
        Log.d(TAG, "onLoginResponse::" + loginEvent.result)
        presenter.refreshUserPage()
    }

    private fun showGameSortConditionDialog()
    {
        dialogGameSortCondition = DialogGameSortCondition(activity as Context)
        dialogGameSortCondition?.setOnConfirmButtonClickListener { gamePlatform, sortCondition ->
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
                getString(R.string.fastest_completion) -> UserFragmentPresenter.SORT_GAME_BY_FASTEST_PROGRESE
                getString(R.string.slowest_completion) -> UserFragmentPresenter.SORT_GAME_BY_SLOWEST_PROGRESE
                getString(R.string.perfect_difficult)  -> UserFragmentPresenter.SORT_GAME_BY_PERFECT_DIFFICULT
                else                                   -> UserFragmentPresenter.SORT_GAME_BY_TIME
            }
            presenter.refreshGameList(platform, condition)
        }
        dialogGameSortCondition?.show()
    }
}