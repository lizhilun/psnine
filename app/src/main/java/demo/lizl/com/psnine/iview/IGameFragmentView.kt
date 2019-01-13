package demo.lizl.com.psnine.iview

import demo.lizl.com.psnine.model.GameInfoItem

interface IGameFragmentView : IBaseView
{
    fun onHotGameListRefresh(hotGameList: List<GameInfoItem>)

    fun onGameSearchRefresh(gameList: List<GameInfoItem>, resultTotalCount : Int)

    fun onGameSearchLoadMore(gameList: List<GameInfoItem>, resultTotalCount : Int)
}