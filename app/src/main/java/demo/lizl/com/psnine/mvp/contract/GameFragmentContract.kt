package demo.lizl.com.psnine.mvp.contract

import demo.lizl.com.psnine.bean.DiscountGameItem
import demo.lizl.com.psnine.bean.GameInfoItem
import demo.lizl.com.psnine.mvp.BasePresenter
import demo.lizl.com.psnine.mvp.BaseView

class GameFragmentContract
{
    interface View : BaseView
    {
        fun onHotGameListRefresh(hotGameList: List<GameInfoItem>)

        fun onDiscountGameListRefresh(discountGameList: List<DiscountGameItem>, totalCount: Int)

        fun onDiscountGameListLoadMore(discountGameList: List<DiscountGameItem>, totalCount: Int)

        fun onGameSearchRefresh(gameList: List<GameInfoItem>, resultTotalCount: Int)

        fun onGameSearchLoadMore(gameList: List<GameInfoItem>, resultTotalCount: Int)
    }

    interface Presenter : BasePresenter<View>
    {
        fun refreshHitGameList()

        fun refreshDiscountGameList()

        fun loadMoreDiscountGameList()

        fun searchGame(searchStr: String)

        fun loadMoreSearchResult()
    }
}