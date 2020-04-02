package demo.lizl.com.psnine.mvvm.fragment

import android.content.Context
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.jeremyliao.liveeventbus.LiveEventBus
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.UiApplication
import demo.lizl.com.psnine.adapter.GameListAdapter
import demo.lizl.com.psnine.adapter.InfoGridAdapter
import demo.lizl.com.psnine.bean.ResultItem
import demo.lizl.com.psnine.config.AppConfig
import demo.lizl.com.psnine.constant.AppConstant
import demo.lizl.com.psnine.constant.EventConstant
import demo.lizl.com.psnine.databinding.FragmentUserBinding
import demo.lizl.com.psnine.mvvm.activity.GameDetailActivity
import demo.lizl.com.psnine.mvvm.activity.LoginActivity
import demo.lizl.com.psnine.mvvm.base.BaseFragment
import demo.lizl.com.psnine.mvvm.viewmodel.UserGameViewModel
import demo.lizl.com.psnine.mvvm.viewmodel.UserInfoViewModel
import demo.lizl.com.psnine.util.ActivityUtil
import demo.lizl.com.psnine.util.DialogUtil
import demo.lizl.com.psnine.util.UserInfoUpdateUtil
import kotlinx.android.synthetic.main.fragment_user.*


class UserFragment : BaseFragment<FragmentUserBinding>()
{

    private val gameListAdapter = GameListAdapter()

    private val userInfoViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(UiApplication.instance).create(UserInfoViewModel::class.java)
    private val userGameViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(UiApplication.instance).create(UserGameViewModel::class.java)

    override fun getLayoutResId() = R.layout.fragment_user

    override fun initView()
    {
        var psnId = activity?.intent?.extras?.getString(AppConstant.BUNDLE_DATA_STRING, "")

        dataBinding.isOwenAccount = psnId.isNullOrBlank()

        if (psnId.isNullOrBlank()) psnId = AppConfig.CUR_PSN_ID
        userInfoViewModel.bindPsnId(psnId)
        userGameViewModel.bindPsnId(psnId)

        dataBinding.gameListAdapter = gameListAdapter

        refresh_layout.setOnLoadMoreListener { userGameViewModel.loadMoreGameList() }

        fam_menu.setClosedOnTouchOutside(true)

        val userInfoUpdateCallBack = { resultItem: ResultItem ->
            when (resultItem.result)
            {
                AppConstant.RESULT_SUCCESS ->
                {
                    DialogUtil.dismissDialog()
                    ToastUtils.showShort(R.string.notify_success_to_update_info)
                    refreshData()
                }
                else                       ->
                {
                    DialogUtil.showOperationConfirmDialog(activity as Context, getString(R.string.notify_failed_to_update_info), resultItem.failedReason) {
                        if (resultItem.failedReason == getString(R.string.notify_need_login_first))
                        {
                            ActivityUtil.turnToActivity(LoginActivity::class.java)
                        }
                    }
                }
            }
        }

        fab_synchronize_level.setOnClickListener {
            DialogUtil.showLoadingDialog(activity as Context)
            UserInfoUpdateUtil.updateUserInfo(psnId, AppConfig.BASE_REQUEST_URL + "psnid/$psnId/upbase", userInfoUpdateCallBack)
            fam_menu.close(true)
        }

        fab_synchronize_game.setOnClickListener {
            DialogUtil.showLoadingDialog(activity as Context)
            UserInfoUpdateUtil.updateUserInfo(psnId, AppConfig.BASE_REQUEST_URL + "psnid/$psnId/upgame", userInfoUpdateCallBack)
            fam_menu.close(true)
        }

        fab_sort_condition.setOnClickListener {
            showGameSortConditionDialog()
            fam_menu.close(true)
        }

        iv_back.setOnClickListener { activity?.onBackPressed() }

        gameListAdapter.setGameItemClickListener { ActivityUtil.turnToActivity(GameDetailActivity::class.java, it.gameDetailUrl) }

        LiveEventBus.get(EventConstant.EVENT_LOGIN_RESULT, Boolean::class.java).observe(this, Observer {
            if (it) refreshData()
        })

        refreshData()

        userInfoViewModel.getUserInfoLiveData().observe(this, Observer { dataBinding.userInfoItem = it })

        userInfoViewModel.getUserGameInfoLiveData().observe(this, Observer {
            dataBinding.rvGameInfo.adapter = InfoGridAdapter(it)
            dataBinding.rvGameInfo.layoutManager = GridLayoutManager(activity, it.size)
        })

        userGameViewModel.getUserGameLiveData().observe(this, Observer {
            refresh_layout.finishRefresh()
            refresh_layout.finishLoadMore()
            refresh_layout.setNoMoreData(it.size >= userGameViewModel.getUserGameCountLiveData().value ?: 0)
            gameListAdapter.setDiffNewData(it.toMutableList())
        })
    }

    private fun refreshData()
    {
        userInfoViewModel.refreshUserInfo()
        userGameViewModel.refreshUserGame()
    }

    private fun showGameSortConditionDialog()
    {
        DialogUtil.showGameSortConditionDialog(activity as Context) { gamePlatform, sortCondition ->
            val platform = when (gamePlatform)
            {
                getString(R.string.all) -> UserGameViewModel.GAME_PLATFORM_ALL
                "PSV"                   -> UserGameViewModel.GAME_PLATFORM_PSV
                "PS3"                   -> UserGameViewModel.GAME_PLATFORM_PS3
                "PS4"                   -> UserGameViewModel.GAME_PLATFORM_PS4
                else                    -> UserGameViewModel.GAME_PLATFORM_ALL
            }
            val condition = when (sortCondition)
            {
                getString(R.string.newest)             -> UserGameViewModel.SORT_GAME_BY_TIME
                getString(R.string.fastest_completion) -> UserGameViewModel.SORT_GAME_BY_FASTEST_PROGRESS
                getString(R.string.slowest_completion) -> UserGameViewModel.SORT_GAME_BY_SLOWEST_PROGRESS
                getString(R.string.perfect_difficult)  -> UserGameViewModel.SORT_GAME_BY_PERFECT_DIFFICULT
                else                                   -> UserGameViewModel.SORT_GAME_BY_TIME
            }
            userGameViewModel.sortUserGame(platform, condition)
        }
    }
}