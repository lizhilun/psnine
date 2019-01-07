package demo.lizl.com.psnine.presenter

import android.content.Context
import android.text.TextUtils
import demo.lizl.com.psnine.activity.BaseActivity
import demo.lizl.com.psnine.config.AppConfig
import demo.lizl.com.psnine.iview.IUserFragmentView
import demo.lizl.com.psnine.model.GameInfoItem
import demo.lizl.com.psnine.model.UserGameInfoItem
import demo.lizl.com.psnine.model.UserInfoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode
import org.jsoup.select.Elements

class UserFragmentPresenter(context: Context, iView: IUserFragmentView) : BasePresenter(context, iView)
{

    private var gameItemCount = 0
    private var curGamePage = 1
    private var loadedGameCount = 0

    private var curPsnId = AppConfig.CUR_PSN_ID

    companion object
    {
        const val SORT_GAME_BY_TIME = 1
        const val SORT_GAME_BY_FASTEST_PROGRESE = 2
        const val SORT_GAME_BY_SLOWEST_PROGRESE = 3
        const val SORT_GAME_BY_PERFECT_DIFFICULT = 4

        const val GAME_PLATFORM_ALL = 1
        const val GAME_PLATFORM_PSV = 2
        const val GAME_PLATFORM_PS3 = 3
        const val GAME_PLATFORM_PS4 = 4
    }


    private var sortGameCondition = SORT_GAME_BY_TIME
    private var sortGamePlatform = GAME_PLATFORM_ALL

    private fun getIView() = iView as IUserFragmentView

    fun setPsnId(psnId: String)
    {
        curPsnId = if (TextUtils.isEmpty(psnId))
        {
            AppConfig.CUR_PSN_ID
        }
        else
        {
            psnId
        }
    }

    fun refreshUserPage()
    {
        GlobalScope.launch {
            val requestUrl = AppConfig.BASE_REQUEST_URL + "psnid/" + curPsnId
            val doc = Jsoup.connect(requestUrl).get()

            val psnElement = doc.getElementsByClass("psnava")
            val imageElement = psnElement[0].getElementsByTag("img")
            val avatarUrl = imageElement[0].attr("src")

            val psnInfoElement = doc.getElementsByClass("psninfo")
            val userId = psnInfoElement[0].text().split(" ")[0]
            val userLevelElement = psnInfoElement[0].getElementsByClass("text-level")
            val userLevel = userLevelElement[0].text()
            val userExperience = userLevelElement.attr("tips")
            val userCupInfo = doc.getElementsByClass("psntrophy")[0].text()

            val userItemInfo = UserInfoItem(userId, avatarUrl, userLevel, userExperience, userCupInfo)

            GlobalScope.launch(Dispatchers.Main) { getIView().onUserInfoRefresh(userItemInfo) }

            val gameTableElement = psnInfoElement[1].select("td")
            val totalGameCount = (gameTableElement[0].childNodes()[0] as TextNode).text().toInt()
            val perfectGameCount = (gameTableElement[1].childNodes()[0] as TextNode).text().toInt()
            val pitGameCount = (gameTableElement[2].childNodes()[0] as Element).text().toInt()
            val gameCompletionRate = (gameTableElement[3].childNodes()[0] as TextNode).text()
            val totalCupCount = (gameTableElement[4].childNodes()[0] as TextNode).text().toInt()

            val userGameInfoItem = UserGameInfoItem(totalGameCount, perfectGameCount, pitGameCount, gameCompletionRate, totalCupCount)
            GlobalScope.launch(Dispatchers.Main) { getIView().onUserGameInfoRefresh(userGameInfoItem) }

            refreshGameList(sortGamePlatform, SORT_GAME_BY_TIME)
        }
    }

    fun refreshGameList(gamePlatform: Int, sortCondition: Int)
    {
        GlobalScope.launch {

            curGamePage = 1
            sortGamePlatform = gamePlatform
            sortGameCondition = sortCondition

            val requestUrl = getRequestGameListUrl()

            val doc = Jsoup.connect(requestUrl).get()

            val gameCountInfo = doc.getElementsByClass("dropmenu")[0].getElementsByClass("h-p").text()
            val gameElementList = doc.getElementsByClass("list")[0].select("tr")
            val gameList = getGameListFromGameElementList(gameElementList)

            GlobalScope.launch(Dispatchers.Main) { getIView().onUserGameListUpdate(gameList) }

            gameItemCount = gameCountInfo.substring(1, gameCountInfo.length - 1).toInt()
            loadedGameCount = gameList.size

            if (gameItemCount <= loadedGameCount)
            {
                getIView().onNoMoreGame()
            }
        }
    }

    fun loadMoreGameList()
    {
        GlobalScope.launch {

            curGamePage++

            val requestUrl = getRequestGameListUrl()
            val doc = Jsoup.connect(requestUrl).get()

            val gameElementList = doc.getElementsByClass("list")[0].select("tr")
            val gameList = getGameListFromGameElementList(gameElementList)

            GlobalScope.launch(Dispatchers.Main) { getIView().onMoreGameLoadFinish(gameList) }

            loadedGameCount += gameList.size
            if (gameItemCount <= loadedGameCount)
            {
                getIView().onNoMoreGame()
            }
        }
    }

    fun updateUserLevel()
    {
        val requestUrl = AppConfig.BASE_REQUEST_URL + "psnid/$curPsnId/upbase"
        (context as BaseActivity).turnToLoginActivity(requestUrl)
    }

    fun updateUserGame()
    {
        val requestUrl = AppConfig.BASE_REQUEST_URL + "psnid/$curPsnId/upgame"
        (context as BaseActivity).turnToLoginActivity(requestUrl)
    }

    private fun getGameListFromGameElementList(gameElementList: Elements): List<GameInfoItem>
    {
        val gameList = mutableListOf<GameInfoItem>()
        for (gameElement in gameElementList)
        {
            try
            {
                val coverUrl = gameElement.getElementsByClass("pd15")[0].getElementsByTag("img").attr("src")
                val gameName = gameElement.select("p")[0].select("a").text()
                val gameDetailUrl = gameElement.select("p")[0].select("a").attr("href")
                val completionRate = gameElement.getElementsByClass("pd10").text().split(" ")[0]
                val lastUpdateTime = gameElement.getElementsByTag("small")[0].text()
                val gameInfoItem = GameInfoItem(coverUrl, gameName, gameDetailUrl)
                val isPS4Game = gameElement.getElementsByClass("pf_ps4").size > 0
                val isPS3Game = gameElement.getElementsByClass("pf_ps3").size > 0
                val isPSVGame = gameElement.getElementsByClass("pf_psv").size > 0
                gameInfoItem.completionRate = completionRate
                gameInfoItem.lastUpdateTime = lastUpdateTime
                gameInfoItem.isPS3Game = isPS3Game
                gameInfoItem.isPS4Game = isPS4Game
                gameInfoItem.isPSVGame = isPSVGame
                gameList.add(gameInfoItem)
            }
            catch (e: Exception)
            {
                continue
            }
        }
        return gameList
    }

    private fun getRequestGameListUrl(): String
    {
        val sortCondition = when (sortGameCondition)
        {
            SORT_GAME_BY_TIME -> "date"
            SORT_GAME_BY_FASTEST_PROGRESE -> "ratio"
            SORT_GAME_BY_SLOWEST_PROGRESE -> "slow"
            SORT_GAME_BY_PERFECT_DIFFICULT -> "difficulty"
            else -> "date"
        }
        val gamePlatform = when (sortGamePlatform)
        {
            GAME_PLATFORM_ALL -> "all"
            GAME_PLATFORM_PSV -> "psvita"
            GAME_PLATFORM_PS3 -> "ps3"
            GAME_PLATFORM_PS4 -> "ps4"
            else -> "all"
        }
        return AppConfig.BASE_REQUEST_URL + "psnid/" + curPsnId + "/psngame?ob=$sortCondition&pf=$gamePlatform&dlc=all&page=$curGamePage"
    }
}
