package demo.lizl.com.psnine.presenter

import android.content.Context
import demo.lizl.com.psnine.config.AppConfig
import demo.lizl.com.psnine.iview.IGameFragmentView
import demo.lizl.com.psnine.model.GameInfoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class GameFragmentPresenter(context: Context, iView: IGameFragmentView) : BasePresenter(context, iView)
{

    private fun getIView() = iView as IGameFragmentView

    private var curSearchStr = ""
    private var curSearchPage = 1
    private var curSeachResultCount = 0

    fun getHitGameList()
    {
        GlobalScope.launch {

            val requestUrl = AppConfig.BASE_REQUEST_URL
            val doc = Jsoup.connect(requestUrl).get()

            val hotGameElementList = doc.getElementsByClass("showbar").select("a")
            val hotGameList = mutableListOf<GameInfoItem>()
            for (hotGameElement in hotGameElementList)
            {
                val gameDetailUrl = hotGameElement.attr("href")
                val gameInfoDoc = Jsoup.connect(gameDetailUrl).get()
                val gameName = gameInfoDoc.getElementsByTag("title").text().split("　")[0]

                val gameInfoElement = gameInfoDoc.getElementsByClass("darklist")[0]
                val psnGameDetailUrl = gameInfoElement.getElementsByTag("a").attr("href")
                val gameCoverUrl = gameInfoElement.getElementsByTag("img").attr("src")
                val gameInfoItem = GameInfoItem(gameCoverUrl, gameName, psnGameDetailUrl)

                val infoText = gameInfoElement.text().split(" ")
                val isPS4Game = infoText.contains("PS4")
                val isPS3Game = infoText.contains("PS3")
                val isPSVGame = infoText.contains("PSVITA")

                gameInfoItem.isPS3Game = isPS3Game
                gameInfoItem.isPS4Game = isPS4Game
                gameInfoItem.isPSVGame = isPSVGame
                hotGameList.add(gameInfoItem)
            }

            GlobalScope.launch(Dispatchers.Main) { getIView().onHotGameListRefresh(hotGameList) }
        }
    }

    fun searchGame(searchStr: String)
    {
        GlobalScope.launch {

            curSearchPage = 1
            curSearchStr = searchStr
            val gameList = getSearchResult(searchStr, curSearchPage)
            GlobalScope.launch(Dispatchers.Main) { getIView().onGameSearchRefresh(gameList, curSeachResultCount) }
        }
    }

    fun loadMoreSearchResult()
    {
        GlobalScope.launch {

            curSearchPage++
            val gameList = getSearchResult(curSearchStr, curSearchPage)
            GlobalScope.launch(Dispatchers.Main) { getIView().onGameSearchLoadMore(gameList, curSeachResultCount) }
        }
    }

    private fun getSearchResult(searchStr: String, resultPage: Int): List<GameInfoItem>
    {
        val requestUrl = AppConfig.BASE_REQUEST_URL + "psngame?title=$searchStr&page=$resultPage"
        val doc = Jsoup.connect(requestUrl).get()

        val gameList = mutableListOf<GameInfoItem>()
        val gameElementList = doc.getElementsByClass("box mt20")[0].select("tr")
        for (gameElement in gameElementList)
        {
            val aElementList = gameElement.getElementsByTag("a")
            val gameCoverUrl = aElementList[0].getElementsByTag("img").attr("src")
            val gameDetailUrl = aElementList[0].attr("href")
            val gameName = aElementList[1].ownText()
            val isPS4Game = gameElement.getElementsByClass("pf_ps4").size > 0
            val isPS3Game = gameElement.getElementsByClass("pf_ps3").size > 0
            val isPSVGame = gameElement.getElementsByClass("pf_psv").size > 0

            val platinumCount = gameElement.getElementsByClass("text-platinum")[0].text()
            val goldCount = gameElement.getElementsByClass("text-gold")[0].text()
            val silverCount = gameElement.getElementsByClass("text-silver")[0].text()
            val bronzeCount = gameElement.getElementsByClass("text-bronze")[0].text()
            val gameCupInfo = "$platinumCount $goldCount $silverCount $bronzeCount"
            val perfectRate = gameElement.getElementsByClass("twoge h-p")[0].getElementsByTag("em").text()

            val resultCountInfo = doc.getElementsByClass("dropmenu")[0].getElementsByClass("h-p").text()
            curSeachResultCount = resultCountInfo.substring(1, resultCountInfo.length - 1).toInt()

            val gameInfoItem = GameInfoItem(gameCoverUrl, gameName, gameDetailUrl)
            gameInfoItem.isPS3Game = isPS3Game
            gameInfoItem.isPS4Game = isPS4Game
            gameInfoItem.isPSVGame = isPSVGame
            gameInfoItem.gameCupInfo = gameCupInfo
            gameInfoItem.perfectRate = perfectRate
            gameList.add(gameInfoItem)
        }
        return gameList
    }
}