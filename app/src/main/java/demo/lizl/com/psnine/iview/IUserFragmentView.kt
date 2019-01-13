package demo.lizl.com.psnine.iview

import demo.lizl.com.psnine.model.GameInfoItem
import demo.lizl.com.psnine.model.UserGameInfoItem
import demo.lizl.com.psnine.model.UserInfoItem

interface IUserFragmentView : IBaseView
{
    fun onUserInfoRefresh(userInfoItem: UserInfoItem)

    fun onUserGameInfoRefresh(userGameInfoItem: UserGameInfoItem)

    fun onUserGameListUpdate(gameList: List<GameInfoItem>)

    fun onMoreGameLoadFinish(gameList: List<GameInfoItem>)

    fun onNoMoreGame()

    fun onInfoUpdateFinish()

    fun onInfoUpdateFailed(reason: String)
}