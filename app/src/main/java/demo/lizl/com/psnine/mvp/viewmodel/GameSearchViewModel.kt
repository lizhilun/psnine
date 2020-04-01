package demo.lizl.com.psnine.mvp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import demo.lizl.com.psnine.bean.GameInfoItem
import demo.lizl.com.psnine.config.AppConfig
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class GameSearchViewModel : ViewModel()
{
    private val TAG = "GameSearchViewModel"

    private var curSearchStr = ""
    private var curSearchPage = 1

    private val searchResultLiveData = MutableLiveData<MutableList<GameInfoItem>>()
    private val searchResultCountLiveData = MutableLiveData<Int>()

    fun getSearchResultLiveData() = searchResultLiveData

    fun getSearchResultCountLiveData() = searchResultCountLiveData

    fun searchGame(searchStr: String)
    {
        if (searchStr.isBlank()) return
        GlobalScope.launch {
            curSearchPage = 1
            curSearchStr = searchStr
            val gameList = getSearchResult(searchStr, curSearchPage)
            searchResultLiveData.postValue(gameList)
        }
    }

    fun loadMoreSearchResult()
    {
        GlobalScope.launch {
            curSearchPage++
            val gameList = searchResultLiveData.value ?: mutableListOf()
            gameList.addAll(getSearchResult(curSearchStr, curSearchPage))
            searchResultLiveData.postValue(gameList)
        }
    }

    private fun getSearchResult(searchStr: String, resultPage: Int): MutableList<GameInfoItem>
    {
        val gameList = mutableListOf<GameInfoItem>()

        try
        {
            val requestUrl = AppConfig.BASE_REQUEST_URL + "psngame?title=$searchStr&page=$resultPage"
            val doc = Jsoup.connect(requestUrl).get()

            doc.getElementsByClass("box mt20").first()?.select("tr")?.forEach { gameElement ->

                val aElementList = gameElement.getElementsByTag("a")
                if (aElementList.size <= 1) return@forEach
                val gameCoverUrl = aElementList[0].getElementsByTag("img").attr("src")
                val gameDetailUrl = aElementList[0].attr("href")
                val gameName = aElementList[1].ownText()
                val isPS4Game = gameElement.getElementsByClass("pf_ps4").isNotEmpty()
                val isPS3Game = gameElement.getElementsByClass("pf_ps3").isNotEmpty()
                val isPSVGame = gameElement.getElementsByClass("pf_psv").isNotEmpty()

                val platinumCount = gameElement.getElementsByClass("text-platinum").first()?.text().orEmpty()
                val goldCount = gameElement.getElementsByClass("text-gold").first()?.text().orEmpty()
                val silverCount = gameElement.getElementsByClass("text-silver").first()?.text().orEmpty()
                val bronzeCount = gameElement.getElementsByClass("text-bronze").first()?.text().orEmpty()
                val gameCupInfo = "$platinumCount $goldCount $silverCount $bronzeCount"
                val perfectRate = gameElement.getElementsByClass("twoge h-p").first()?.getElementsByTag("em")?.text().orEmpty()

                val resultCountInfo = doc.getElementsByClass("dropmenu")[0].getElementsByClass("h-p").text()
                val searchResultCount = resultCountInfo.substring(1, resultCountInfo.length - 1).toInt()
                if (searchResultCount != searchResultCountLiveData.value)
                {
                    searchResultCountLiveData.postValue(searchResultCount)
                }

                val gameInfoItem = GameInfoItem(gameCoverUrl, gameName, gameDetailUrl)
                gameInfoItem.isPS3Game = isPS3Game
                gameInfoItem.isPS4Game = isPS4Game
                gameInfoItem.isPSVGame = isPSVGame
                gameInfoItem.gameCupInfo = gameCupInfo
                gameInfoItem.perfectRate = perfectRate
                gameList.add(gameInfoItem)
            }
        }
        catch (e: Exception)
        {
            Log.e(TAG, "getSearchResult error:" + e.message)
        }
        return gameList
    }
}