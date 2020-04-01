package demo.lizl.com.psnine.mvvm.activity

import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.UiApplication
import demo.lizl.com.psnine.constant.AppConstant
import demo.lizl.com.psnine.custom.view.GameCupListView
import demo.lizl.com.psnine.mvvm.viewmodel.GameDetailViewModel
import demo.lizl.com.psnine.util.GlideUtil
import kotlinx.android.synthetic.main.activity_game_detail.*

class GameDetailActivity : BaseActivity()
{
    override fun getLayoutResId() = R.layout.activity_game_detail

    override fun initView()
    {
        val gameDetailUrl = intent?.getStringExtra(AppConstant.BUNDLE_DATA_STRING).orEmpty()

        val viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(UiApplication.instance).create(GameDetailViewModel::class.java)

        viewModel.bindGameDetailUrl(gameDetailUrl)

        refresh_layout.setEnableRefresh(true)
        refresh_layout.isNestedScrollingEnabled = false
        refresh_layout.setOnRefreshListener { viewModel.refreshGameDetailInfo() }

        ic_back.setOnClickListener { onBackPressed() }

        viewModel.refreshGameDetailInfo()

        viewModel.getGameInfoLiveData().observe(this, Observer {
            GlideUtil.displayImage(iv_game_cover, it.coverUrl)
            tv_game_name.text = it.gameName
            tv_game_cup_info.text = it.gameCupInfo
        })

        viewModel.getGameCupInfoLiveData().observe(this, Observer {
            group_user_game_cup_info.isVisible = true
            tv_game_progress.text = it.gameProgress
            tv_first_cup_time.text = it.firstCupTime
            tv_last_cup_time.text = it.lastCupTime
            tv_total_time.text = it.totalTime
        })

        viewModel.getGameCupGroupLiveData().observe(this, Observer {
            ll_game_cup_view.removeAllViews()

            it.forEach { gameCupGroupItem ->
                val gameCupListView = GameCupListView(this)
                gameCupListView.bindGameCupGroupItem(gameCupGroupItem)
                val dividerView = LinearLayout(this)
                val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                layoutParams.height = resources.getDimensionPixelSize(R.dimen.global_divider_view_height)
                dividerView.layoutParams = layoutParams
                dividerView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorDivideView))
                ll_game_cup_view.addView(dividerView)
                gameCupListView.bindGameCupInfo()
                ll_game_cup_view.addView(gameCupListView)
            }
        })
    }
}