package demo.lizl.com.psnine.mvvm.viewmodel

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import demo.lizl.com.psnine.bean.GameCupGroupItem
import demo.lizl.com.psnine.bean.GameCupInfoItem
import demo.lizl.com.psnine.bean.GameCupItem
import demo.lizl.com.psnine.bean.GameInfoItem
import demo.lizl.com.psnine.config.AppConfig
import demo.lizl.com.psnine.constant.AppConstant
import demo.lizl.com.psnine.custom.function.deleteStr
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class GameDetailViewModel : ViewModel()
{

    private var gameDetailUrl = ""

    private val gameInfoLiveData = MutableLiveData<GameInfoItem>()
    private val gameCupInfoLiveData = MutableLiveData<GameCupInfoItem>()
    private val gameCupGroupLiveData = MutableLiveData<MutableList<GameCupGroupItem>>()

    fun getGameInfoLiveData() = gameInfoLiveData
    fun getGameCupInfoLiveData() = gameCupInfoLiveData
    fun getGameCupGroupLiveData() = gameCupGroupLiveData

    fun bindGameDetailUrl(gameDetailUrl: String)
    {
        this.gameDetailUrl = if (!gameDetailUrl.contains("psnid"))
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
        GlobalScope.launch(Dispatchers.IO) {

            val doc = Jsoup.connect(gameDetailUrl).get()

            val gameNameInfo = doc.getElementsByTag("title").text()
            val gameName = gameNameInfo.deleteStr("《").deleteStr("》")

            val gameInfoElement = doc.getElementsByClass("box pd10").first() ?: return@launch
            val gameCoverUrl = gameInfoElement.getElementsByTag("img").attr("src")

            val platinumCount = gameInfoElement.getElementsByClass("text-platinum").first()?.text().orEmpty()
            val goldCount = gameInfoElement.getElementsByClass("text-gold").first()?.text().orEmpty()
            val silverCount = gameInfoElement.getElementsByClass("text-silver").first()?.text().orEmpty()
            val bronzeCount = gameInfoElement.getElementsByClass("text-bronze").first()?.text().orEmpty()
            val perfectRate = gameInfoElement.getElementsByTag("em").last()?.text().orEmpty()

            val boxElement = doc.getElementsByClass("box")
            if (boxElement.size >= 2)
            {
                val gameCupInfoElement = boxElement[1]
                if (gameCupInfoElement.text().contains(AppConfig.CUR_PSN_ID))
                {
                    val gameProgress = gameCupInfoElement.getElementsByClass("text-strong").text()
                    val cupInfoElement = gameCupInfoElement.getElementsByClass("twoge")

                    val firstCupTime = if (cupInfoElement.size > 0) cupInfoElement[0].ownText() else "--"
                    val lastCupTime = if (cupInfoElement.size > 1) cupInfoElement[1].ownText() else "--"
                    val totalTime = if (cupInfoElement.size > 2) cupInfoElement[2].ownText() else "--"

                    gameCupInfoLiveData.postValue(GameCupInfoItem(gameProgress, firstCupTime, lastCupTime, totalTime))
                }
            }

            val gameCupInfo = "$platinumCount $goldCount $silverCount $bronzeCount $perfectRate"

            val gameInfoItem = GameInfoItem(gameCoverUrl, gameName, "")
            gameInfoItem.gameCupInfo = gameCupInfo
            gameInfoLiveData.postValue(gameInfoItem)

            val gameCupGroupList = mutableListOf<GameCupGroupItem>()
            doc.getElementsByClass("list").forEach { element ->

                val gameCupListElement = element.select("tr")
                if (gameCupListElement.isEmpty()) return@forEach
                val gameCupName = gameCupListElement[0].getElementsByClass("ml100").select("p").text()
                val gameCupCoverUrl = gameCupListElement[0].getElementsByTag("img").attr("src")
                val gameCupCount = gameCupListElement[0].getElementsByTag("em").text()

                val gameCupItemList = mutableListOf<GameCupItem>()

                for (i in 1 until gameCupListElement.size)
                {
                    val cupCoverUrl = gameCupListElement[i].getElementsByTag("img").attr("src")

                    val cupInfoElement = gameCupListElement[i].getElementsByTag("p").first() ?: return@forEach
                    val cupName = cupInfoElement.select("a").text()
                    val cupTipsUrl = cupInfoElement.select("a").attr("href")
                    val cupType = when
                    {
                        cupInfoElement.getElementsByClass("text-platinum").isNotEmpty() -> AppConstant.GAME_CUP_TYPE_PLATINUM
                        cupInfoElement.getElementsByClass("text-gold").isNotEmpty()     -> AppConstant.GAME_CUP_TYPE_GOLD
                        cupInfoElement.getElementsByClass("text-silver").isNotEmpty()   -> AppConstant.GAME_CUP_TYPE_SILVER
                        else                                                            -> AppConstant.GAME_CUP_TYPE_BRONZE
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

                gameCupGroupList.add(GameCupGroupItem(gameCupName, gameCupCoverUrl, gameCupCount, gameCupItemList))
            }

            gameCupGroupLiveData.postValue(gameCupGroupList)
        }
    }
}