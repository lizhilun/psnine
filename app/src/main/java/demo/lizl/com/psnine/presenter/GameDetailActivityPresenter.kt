package demo.lizl.com.psnine.presenter

import android.content.Context
import android.text.TextUtils
import demo.lizl.com.psnine.config.AppConfig
import demo.lizl.com.psnine.customview.GameCupListView
import demo.lizl.com.psnine.iview.IGameDetailActivityView
import demo.lizl.com.psnine.model.GameCupItem
import demo.lizl.com.psnine.model.GameInfoItem
import demo.lizl.com.psnine.util.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class GameDetailActivityPresenter(context: Context, iView: IGameDetailActivityView) : BasePresenter<IGameDetailActivityView>(context, iView)
{

    private lateinit var requestUrl: String

    fun setGameDetailUrl(gameDetailUrl: String)
    {
        requestUrl = if (!gameDetailUrl.contains("psnid"))
        {
            gameDetailUrl + "?psnid=" + AppConfig.CUR_PSN_ID
        }
        else
        {
            gameDetailUrl
        }
    }


    fun refreshGameDetailInfo()
    {

        GlobalScope.launch {

            val doc = Jsoup.connect(requestUrl).get()

            val gameNameInfo = doc.getElementsByTag("title").text()
            val gameName = gameNameInfo.substring(gameNameInfo.indexOf("《") + 1, gameNameInfo.lastIndexOf("》"))

            val gameInfoElement = doc.getElementsByClass("box pd10")[0]
            val gameCoverUrl = gameInfoElement.getElementsByTag("img").attr("src")

            val platinumCount = gameInfoElement.getElementsByClass("text-platinum")[0].text()
            val goldCount = gameInfoElement.getElementsByClass("text-gold")[0].text()
            val silverCount = gameInfoElement.getElementsByClass("text-silver")[0].text()
            val bronzeCount = gameInfoElement.getElementsByClass("text-bronze")[0].text()

            val boxElement = doc.getElementsByClass("box")
            if (boxElement.size >= 2)
            {
                val gameCupInfoElement = doc.getElementsByClass("box")[1]
                if (gameCupInfoElement.text().contains(AppConfig.CUR_PSN_ID))
                {
                    val gameProgress = gameCupInfoElement.getElementsByClass("text-strong").text()
                    val cupInfoElement = gameCupInfoElement.getElementsByClass("twoge")

                    var firstCupTime = "--"
                    var lastCupTime = "--"
                    var totalTime = "--"
                    if (cupInfoElement.size >= 3)
                    {
                        firstCupTime = cupInfoElement[0].ownText()
                        lastCupTime = cupInfoElement[1].ownText()
                        totalTime = cupInfoElement[2].ownText()
                    }

                    GlobalScope.launch(Dispatchers.Main) { iView.onUserGameCupInfoRefresh(gameProgress, firstCupTime, lastCupTime, totalTime) }
                }
            }

            val gameCupInfo = "$platinumCount $goldCount $silverCount $bronzeCount"

            val gameInfoItem = GameInfoItem(gameCoverUrl, gameName, "")
            gameInfoItem.gameCupInfo = gameCupInfo

            GlobalScope.launch(Dispatchers.Main) { iView.onGameInfoRefresh(gameInfoItem) }

            val allGameCupListElement = doc.getElementsByClass("list")
            val gameCupViewList = mutableListOf<GameCupListView>()

            for (element in allGameCupListElement)
            {
                val gameCupListElement = element.select("tr")
                val gameCupName = gameCupListElement[0].getElementsByClass("ml100").select("p").text()
                val gameCupCoverUrl = gameCupListElement[0].getElementsByTag("img").attr("src")
                val gameCupCount = gameCupListElement[0].getElementsByTag("em").text()

                val gameCupItemList = mutableListOf<GameCupItem>()

                for (i in 1 until gameCupListElement.size)
                {
                    val cupCoverUrl = gameCupListElement[i].getElementsByTag("img").attr("src")

                    val cupInfoElement = gameCupListElement[i].getElementsByTag("p")[0]
                    val cupName = cupInfoElement.select("a").text()
                    val cupTipsUrl = cupInfoElement.select("a").attr("href")
                    val cupType = if (cupInfoElement.getElementsByClass("text-platinum").size > 0)
                    {
                        Constant.GAME_CUP_TYPE_PLATINUM
                    }
                    else if (cupInfoElement.getElementsByClass("text-gold").size > 0)
                    {
                        Constant.GAME_CUP_TYPE_GOLD
                    }
                    else if (cupInfoElement.getElementsByClass("text-silver").size > 0)
                    {
                        Constant.GAME_CUP_TYPE_SILVER
                    }
                    else
                    {
                        Constant.GAME_CUP_TYPE_BRONZE
                    }

                    var cupDes = gameCupListElement[i].getElementsByClass("text-gray").text()
                    if (TextUtils.isEmpty(cupDes))
                    {
                        cupDes = gameCupListElement[i].getElementsByClass("text-strong mt10").text()
                    }
                    val cupTips = gameCupListElement[i].getElementsByClass("alert-success pd5").text()
                    val cupGetTime = gameCupListElement[i].getElementsByClass("lh180 alert-success pd5 r").text()

                    val gameCupItem = GameCupItem(cupCoverUrl, cupName, cupDes, cupTips, cupGetTime, cupType, cupTipsUrl)
                    gameCupItemList.add(gameCupItem)
                }

                val gameCupView = GameCupListView(context)
                gameCupView.setViewInfo(gameCupName, gameCupCoverUrl, gameCupCount, gameCupItemList)
                gameCupViewList.add(gameCupView)
            }

            GlobalScope.launch(Dispatchers.Main) { iView.onGameCupInfoRefresh(gameCupViewList) }
        }
    }
}