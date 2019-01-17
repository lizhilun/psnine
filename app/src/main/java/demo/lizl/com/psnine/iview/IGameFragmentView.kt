package demo.lizl.com.psnine.iview

import demo.lizl.com.psnine.model.DiscountGameItem
import demo.lizl.com.psnine.model.GameInfoItem

interface IGameFragmentView : IBaseView
{
    fun onHotGameListRefresh(hotGameList: List<GameInfoItem>)

    fun onDiscountGameListRefresh(discountGameList: List<DiscountGameItem>, totalCount: Int)

    fun onDiscountGameListLoadMore(discountGameList: List<DiscountGameItem>, totalCount: Int)

    fun onGameSearchRefresh(gameList: List<GameInfoItem>, resultTotalCount: Int)

    fun onGameSearchLoadMore(gameList: List<GameInfoItem>, resultTotalCount: Int)
}