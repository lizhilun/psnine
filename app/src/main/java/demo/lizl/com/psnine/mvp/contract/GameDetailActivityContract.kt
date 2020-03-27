package demo.lizl.com.psnine.mvp.contract

import demo.lizl.com.psnine.bean.GameInfoItem
import demo.lizl.com.psnine.custom.view.GameCupListView
import demo.lizl.com.psnine.mvp.base.BasePresenter
import demo.lizl.com.psnine.mvp.base.BaseView

class GameDetailActivityContract
{
    interface View : BaseView
    {
        fun onGameInfoRefresh(gameInfoItem: GameInfoItem)

        fun onUserGameCupInfoRefresh(gameProgress: String, firstCupTime: String, lastCupTime: String, totalTime: String)

        fun onGameCupInfoRefresh(gameCupViewList: List<GameCupListView>)
    }

    interface Presenter : BasePresenter<View>
    {
        fun setGameDetailUrl(gameDetailUrl: String)

        fun refreshGameDetailInfo()
    }
}