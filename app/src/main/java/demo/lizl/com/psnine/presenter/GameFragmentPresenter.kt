package demo.lizl.com.psnine.presenter

import android.content.Context
import demo.lizl.com.psnine.config.AppConfig
import demo.lizl.com.psnine.iview.IGameFragmentView
import demo.lizl.com.psnine.model.DiscountGameItem
import demo.lizl.com.psnine.model.GameInfoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class GameFragmentPresenter(context: Context, iView: IGameFragmentView) : BasePresenter<IGameFragmentView>(context, iView)
{

    private var curSearchStr = ""
    private var curSearchPage = 1
    private var curSearchResultCount = 0

    private var curDiscountGamePage = 1
    private var curDiscountGameCount = 0

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

            GlobalScope.launch(Dispatchers.Main) { iView.onHotGameListRefresh(hotGameList) }
        }
    }

    fun refreshDiscountGameList()
    {
        GlobalScope.launch {

            curDiscountGamePage = 1

            val discountGameList = getDiscountGameList(curDiscountGamePage)

            GlobalScope.launch(Dispatchers.Main) { iView.onDiscountGameListRefresh(discountGameList, curDiscountGameCount) }
        }
    }

    fun loadMoreDiscountGameList()
    {
        GlobalScope.launch {

            curDiscountGamePage++

            val discountGameList = getDiscountGameList(curDiscountGamePage)

            GlobalScope.launch(Dispatchers.Main) { iView.onDiscountGameListLoadMore(discountGameList, curDiscountGameCount) }
        }

    }

    fun searchGame(searchStr: String)
    {
        GlobalScope.launch {

            curSearchPage = 1
            curSearchStr = searchStr
            val gameList = getSearchResult(searchStr, curSearchPage)
            GlobalScope.launch(Dispatchers.Main) { iView.onGameSearchRefresh(gameList, curSearchResultCount) }
        }
    }

    fun loadMoreSearchResult()
    {
        GlobalScope.launch {

            curSearchPage++
            val gameList = getSearchResult(curSearchStr, curSearchPage)
            GlobalScope.launch(Dispatchers.Main) { iView.onGameSearchLoadMore(gameList, curSearchResultCount) }
        }
    }

    private fun getDiscountGameList(pageIndex: Int): List<DiscountGameItem>
    {
        val requestUrl = AppConfig.BASE_REQUEST_URL + "dd?type=all&region=all&pf=all&ddstatus=on&page=$pageIndex"
        val doc = Jsoup.connect(requestUrl).get()

        val resultCountInfo = doc.getElementsByClass("dropmenu")[0].getElementsByClass("h-p").text()
        curDiscountGameCount = resultCountInfo.substring(1, resultCountInfo.length - 1).toInt()

        val discountGameList = mutableListOf<DiscountGameItem>()
        val discountGameElementList = doc.getElementsByClass("dd_ul")[0].select("li")
        for (discountGameElement in discountGameElementList)
        {
            val gameCoverUrl = discountGameElement.getElementsByTag("img").attr("src")
            val discountRate = discountGameElement.getElementsByClass("dd_tag_plus").text()

            val ps4PlatformElement = discountGameElement.getElementsByClass("dd_pf pf_ps4")
            val psvPlatformElement = discountGameElement.getElementsByClass("dd_pf pf_psv")
            val ps3PlatformElement = discountGameElement.getElementsByClass("dd_pf pf_ps3")

            val gamePlatform = when
            {
                ps4PlatformElement.size > 0 -> ps4PlatformElement.text()
                psvPlatformElement.size > 0 -> psvPlatformElement.text()
                ps3PlatformElement.size > 0 -> ps3PlatformElement.text()
                else -> ""
            }

            val isLowest = discountGameElement.getElementsByClass("dd_status dd_status_best").size > 0
            val gameNameInfo = discountGameElement.getElementsByClass("dd_title mb10").text()
            val gameName = gameNameInfo.substring(gameNameInfo.indexOf("《") + 1, gameNameInfo.lastIndexOf("》"))
            val discountInfoElements = discountGameElement.getElementsByClass("dd_text")
            val discountTime = discountInfoElements[discountInfoElements.size - 1].text()
            val oriPrice = discountGameElement.getElementsByClass("dd_price_old").text()
            val notMemberPrice = discountGameElement.getElementsByClass("dd_price_off").text()
            val memberPriceElement = discountGameElement.getElementsByClass("dd_price_plus")
            val memberPrice = if (memberPriceElement.size > 0) memberPriceElement.text() else ""

            val discountGameItem = DiscountGameItem(gameName, gameCoverUrl, discountRate, discountTime, gamePlatform, isLowest, oriPrice, notMemberPrice, memberPrice)
            discountGameList.add(discountGameItem)
        }
        return discountGameList
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
            curSearchResultCount = resultCountInfo.substring(1, resultCountInfo.length - 1).toInt()

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