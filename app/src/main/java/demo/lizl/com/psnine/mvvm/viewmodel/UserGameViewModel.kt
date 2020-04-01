package demo.lizl.com.psnine.mvvm.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import demo.lizl.com.psnine.bean.GameInfoItem
import demo.lizl.com.psnine.config.AppConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.select.Elements

class UserGameViewModel : ViewModel()
{
    private val TAG = "UserGameLiveData"

    private var curPsnId = ""

    companion object
    {
        const val SORT_GAME_BY_TIME = 1
        const val SORT_GAME_BY_FASTEST_PROGRESS = 2
        const val SORT_GAME_BY_SLOWEST_PROGRESS = 3
        const val SORT_GAME_BY_PERFECT_DIFFICULT = 4

        const val GAME_PLATFORM_ALL = 1
        const val GAME_PLATFORM_PSV = 2
        const val GAME_PLATFORM_PS3 = 3
        const val GAME_PLATFORM_PS4 = 4
    }

    private var curGamePage = 1
    private var sortGameCondition = SORT_GAME_BY_TIME
    private var sortGamePlatform = GAME_PLATFORM_ALL

    private val userGameLiveData = MutableLiveData<MutableList<GameInfoItem>>()
    private val userGameCountLiveData = MutableLiveData<Int>()

    fun getUserGameLiveData() = userGameLiveData

    fun getUserGameCountLiveData() = userGameCountLiveData

    fun bindPsnId(psnId: String)
    {
        curPsnId = psnId
    }

    fun refreshUserGame()
    {
        sortUserGame(GAME_PLATFORM_ALL, SORT_GAME_BY_TIME)
    }

    fun sortUserGame(gamePlatform: Int, sortCondition: Int)
    {
        GlobalScope.launch(Dispatchers.IO) {

            try
            {
                curGamePage = 1
                sortGamePlatform = gamePlatform
                sortGameCondition = sortCondition

                val requestUrl = getRequestGameListUrl()
                val doc = Jsoup.connect(requestUrl).get()

                val gameCountInfo = doc.getElementsByClass("dropmenu")[0].getElementsByClass("h-p").text()
                val gameItemCount = gameCountInfo.substring(1, gameCountInfo.length - 1).toInt()
                userGameCountLiveData.postValue(gameItemCount)

                val gameElementList = doc.getElementsByClass("list").select("tr")
                val gameList = getGameListFromGameElementList(gameElementList)
                userGameLiveData.postValue(gameList)
            }
            catch (e: Exception)
            {
                Log.e(TAG, "sortUserGame error:", e)
            }
        }
    }

    fun loadMoreGameList()
    {
        GlobalScope.launch(Dispatchers.IO) {

            try
            {
                curGamePage++

                val requestUrl = getRequestGameListUrl()
                val doc = Jsoup.connect(requestUrl).get()

                val gameCountInfo = doc.getElementsByClass("dropmenu")[0].getElementsByClass("h-p").text()
                val gameItemCount = gameCountInfo.substring(1, gameCountInfo.length - 1).toInt()
                userGameCountLiveData.postValue(gameItemCount)

                val gameElementList = doc.getElementsByClass("list").first()?.select("tr")
                val gameList = userGameLiveData.value ?: mutableListOf()
                gameList.addAll(getGameListFromGameElementList(gameElementList))
                userGameLiveData.postValue(gameList)
            }
            catch (e: Exception)
            {
                Log.e(TAG, "loadMoreGameList error:", e)
            }
        }
    }

    private fun getGameListFromGameElementList(gameElementList: Elements?): MutableList<GameInfoItem>
    {
        val gameList = mutableListOf<GameInfoItem>()
        gameElementList?.forEach { gameElement ->
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
                Log.e(TAG, "getGameListFromGameElementList error:", e)
            }
        }
        return gameList
    }

    private fun getRequestGameListUrl(): String
    {
        val sortCondition = when (sortGameCondition)
        {
            SORT_GAME_BY_TIME              -> "date"
            SORT_GAME_BY_FASTEST_PROGRESS  -> "ratio"
            SORT_GAME_BY_SLOWEST_PROGRESS  -> "slow"
            SORT_GAME_BY_PERFECT_DIFFICULT -> "difficulty"
            else                           -> "date"
        }
        val gamePlatform = when (sortGamePlatform)
        {
            GAME_PLATFORM_ALL -> "all"
            GAME_PLATFORM_PSV -> "psvita"
            GAME_PLATFORM_PS3 -> "ps3"
            GAME_PLATFORM_PS4 -> "ps4"
            else              -> "all"
        }
        return AppConfig.BASE_REQUEST_URL + "psnid/" + curPsnId + "/psngame?ob=$sortCondition&pf=$gamePlatform&dlc=all&page=$curGamePage"
    }
}