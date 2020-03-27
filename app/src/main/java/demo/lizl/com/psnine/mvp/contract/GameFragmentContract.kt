package demo.lizl.com.psnine.mvp.contract

import demo.lizl.com.psnine.bean.DiscountGameItem
import demo.lizl.com.psnine.bean.GameInfoItem
import demo.lizl.com.psnine.mvp.base.BasePresenter
import demo.lizl.com.psnine.mvp.base.BaseView

class GameFragmentContract
{
    interface View : BaseView
    {
        fun onHotGameListRefresh(hotGameList: MutableList<GameInfoItem>)

        fun onDiscountGameListRefresh(discountGameList: MutableList<DiscountGameItem>, totalCount: Int)

        fun onDiscountGameListLoadMore(discountGameList: MutableList<DiscountGameItem>, totalCount: Int)

        fun onGameSearchRefresh(gameList: MutableList<GameInfoItem>, resultTotalCount: Int)

        fun onGameSearchLoadMore(gameList: MutableList<GameInfoItem>, resultTotalCount: Int)
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