package demo.lizl.com.psnine.iview

import demo.lizl.com.psnine.model.GameInfoItem
import demo.lizl.com.psnine.model.UserGameInfoItem
import demo.lizl.com.psnine.model.UserInfoItem

interface IUserFragmentView : IBaseView
{
    fun onUserInfoRefresh(userInfoItem: UserInfoItem)

    fun onUserGameInfoRefresh(userGameInfoItem: UserGameInfoItem)

    fun onUserGameListUpdate(gameList: List<GameInfoItem>, gameTotalCount : Int)

    fun onMoreGameLoadFinish(gameList: List<GameInfoItem>, gameTotalCount : Int)

    fun onInfoUpdateFinish()

    fun onInfoUpdateFailed(reason: String)
}