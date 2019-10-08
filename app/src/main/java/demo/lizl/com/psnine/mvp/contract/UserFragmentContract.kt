package demo.lizl.com.psnine.mvp.contract

import demo.lizl.com.psnine.bean.GameInfoItem
import demo.lizl.com.psnine.bean.UserGameInfoItem
import demo.lizl.com.psnine.bean.UserInfoItem
import demo.lizl.com.psnine.mvp.BasePresenter
import demo.lizl.com.psnine.mvp.BaseView

class UserFragmentContract
{
    interface View : BaseView
    {
        fun onUserInfoRefresh(userInfoItem: UserInfoItem)

        fun onUserGameInfoRefresh(userGameInfoItem: UserGameInfoItem)

        fun onUserGameListUpdate(gameList: List<GameInfoItem>, gameTotalCount : Int)

        fun onMoreGameLoadFinish(gameList: List<GameInfoItem>, gameTotalCount : Int)

        fun onInfoUpdateFinish()

        fun onInfoUpdateFailed(reason: String)
    }

    interface Presenter : BasePresenter<View>
    {
        fun refreshUserPage()

        fun refreshGameList(gamePlatform: Int, sortCondition: Int)

        fun loadMoreGameList()

        fun updateUserLevel()

        fun updateUserGame()
    }
}