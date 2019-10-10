package demo.lizl.com.psnine.mvp.activity

import androidx.core.content.ContextCompat
import android.view.View
import android.widget.LinearLayout
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.customview.GameCupListView
import demo.lizl.com.psnine.bean.GameInfoItem
import demo.lizl.com.psnine.mvp.contract.GameDetailActivityContract
import demo.lizl.com.psnine.mvp.presenter.GameDetailActivityPresenter
import demo.lizl.com.psnine.util.Constant
import demo.lizl.com.psnine.util.GlideUtil
import demo.lizl.com.psnine.util.UiUtil
import kotlinx.android.synthetic.main.activity_game_detail.*

class GameDetailActivity : BaseActivity<GameDetailActivityPresenter>(), GameDetailActivityContract.View
{
    override fun getLayoutResId(): Int
    {
        return R.layout.activity_game_detail
    }

    override fun initPresenter() = GameDetailActivityPresenter(this, this)

    override fun initView()
    {
        val bundle = intent.extras!!
        val gameDetailUrl = bundle.getString(Constant.BUNDLE_DATA_STRING, "")

        presenter.setGameDetailUrl(gameDetailUrl)
        presenter.refreshGameDetailInfo()

        refresh_layout.setEnableLoadMore(false)
        refresh_layout.setRefreshHeader(UiUtil.getDefaultRefreshHeader(this))
        refresh_layout.setEnableRefresh(true)
        refresh_layout.isNestedScrollingEnabled = false
        refresh_layout.setOnRefreshListener { presenter.refreshGameDetailInfo() }

        ic_back.setOnClickListener { finish() }
    }

    override fun onGameInfoRefresh(gameInfoItem: GameInfoItem)
    {
        refresh_layout.finishRefresh()
        GlideUtil.displayImage(this, gameInfoItem.coverUrl, iv_game_cover)
        tv_game_name.text = gameInfoItem.gameName
        tv_game_cup_info.text = gameInfoItem.gameCupInfo
    }

    override fun onGameCupInfoRefresh(gameCupViewList: List<GameCupListView>)
    {
        ll_game_cup_view.removeAllViews()

        for (gameCupView in gameCupViewList)
        {
            val dividerView = LinearLayout(this)
            val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.height = resources.getDimensionPixelSize(R.dimen.global_divider_view_height)
            dividerView.layoutParams = layoutParams
            dividerView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorDivideView))
            ll_game_cup_view.addView(dividerView)
            gameCupView.bindGameCupInfo()
            ll_game_cup_view.addView(gameCupView)
        }
    }

    override fun onUserGameCupInfoRefresh(gameProgress: String, firstCupTime: String, lastCupTime: String, totalTime: String)
    {
        group_user_game_cup_info.visibility = View.VISIBLE
        tv_game_progress.text = gameProgress
        tv_first_cup_time.text = firstCupTime
        tv_last_cup_time.text = lastCupTime
        tv_total_time.text = totalTime
    }
}