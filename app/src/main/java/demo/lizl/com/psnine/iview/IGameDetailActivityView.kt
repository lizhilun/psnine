package demo.lizl.com.psnine.iview

import demo.lizl.com.psnine.customview.GameCupListView
import demo.lizl.com.psnine.model.GameInfoItem

interface IGameDetailActivityView : IBaseView
{
    fun onGameInfoRefresh(gameInfoItem: GameInfoItem)

    fun onUserGameCupInfoRefresh(gameProgress: String, firstCupTime: String, lastCupTime: String, totalTime: String)

    fun onGameCupInfoRefresh(gameCupViewList: List<GameCupListView>)
}