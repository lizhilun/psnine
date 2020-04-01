package demo.lizl.com.psnine.mvp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import demo.lizl.com.psnine.bean.DiscountGameItem
import demo.lizl.com.psnine.config.AppConfig
import demo.lizl.com.psnine.custom.function.deleteStr
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class DiscountViewModel : ViewModel()
{
    private val TAG = "DiscountViewModel"

    private var curDiscountGamePage = 1

    private val discountLiveData = MutableLiveData<MutableList<DiscountGameItem>>()
    private val discountCountLiveData = MutableLiveData<Int>()

    fun getDiscountLiveData() = discountLiveData

    fun getDiscountCountLiveData() = discountCountLiveData

    fun refreshDiscountGameList()
    {
        GlobalScope.launch(Dispatchers.IO) {
            curDiscountGamePage = 1
            val discountGameList = getDiscountGameList(curDiscountGamePage)
            discountLiveData.postValue(discountGameList)
        }
    }

    fun loadMoreDiscountGameList()
    {
        GlobalScope.launch(Dispatchers.IO) {
            curDiscountGamePage++
            val discountGameList = discountLiveData.value ?: mutableListOf()
            discountGameList.addAll(getDiscountGameList(curDiscountGamePage))
            discountLiveData.postValue(discountGameList)
        }
    }

    private fun getDiscountGameList(pageIndex: Int): MutableList<DiscountGameItem>
    {
        val discountGameList = mutableListOf<DiscountGameItem>()

        try
        {
            val requestUrl = AppConfig.BASE_REQUEST_URL + "dd?type=all&region=all&pf=hk&ddstatus=on&page=$pageIndex"
            val doc = Jsoup.connect(requestUrl).get()

            val resultCountInfo = doc.getElementsByClass("dropmenu").first()?.getElementsByClass("h-p")?.text()
            if (resultCountInfo.isNullOrEmpty()) return discountGameList
            val discountGameCount = resultCountInfo.substring(1, resultCountInfo.length - 1).toInt()
            discountCountLiveData.postValue(discountGameCount)

            doc.getElementsByClass("dd_ul").first()?.select("li")?.forEach { discountGameElement ->

                val gameCoverUrl = discountGameElement.getElementsByTag("img").attr("src")
                val discountRate = discountGameElement.getElementsByClass("dd_tag_plus").text()

                val ps4PlatformElement = discountGameElement.getElementsByClass("dd_pf pf_ps4")
                val psvPlatformElement = discountGameElement.getElementsByClass("dd_pf pf_psv")
                val ps3PlatformElement = discountGameElement.getElementsByClass("dd_pf pf_ps3")

                val gamePlatform = when
                {
                    ps4PlatformElement.isNotEmpty() -> ps4PlatformElement.text()
                    psvPlatformElement.isNotEmpty() -> psvPlatformElement.text()
                    ps3PlatformElement.isNotEmpty() -> ps3PlatformElement.text()
                    else                            -> ""
                }

                val isLowest = discountGameElement.getElementsByClass("dd_status dd_status_best").isNotEmpty()
                val gameNameInfo = discountGameElement.getElementsByClass("dd_title mb10").text()
                val gameName = gameNameInfo.deleteStr("《").deleteStr("》")
                val discountInfoElements = discountGameElement.getElementsByClass("dd_text")
                if (discountInfoElements.size < 2) return@forEach
                val discountTime = discountInfoElements[discountInfoElements.size - 1].text()
                val oriPrice = discountGameElement.getElementsByClass("dd_price_old").text()
                val notMemberPrice = discountGameElement.getElementsByClass("dd_price_off").text()
                val memberPrice = discountGameElement.getElementsByClass("dd_price_plus").first()?.text().orEmpty()
                val psnGameInfo = discountInfoElements[2].select("a").attr("onclick")
                val firstQuaIndex = psnGameInfo.indexOf("'")
                val secondQuaIndex = psnGameInfo.indexOf("'", firstQuaIndex + 1)
                val psnGameId = psnGameInfo.substring(firstQuaIndex + 1, secondQuaIndex)

                val discountGameItem = DiscountGameItem(gameName, gameCoverUrl, discountRate, discountTime, gamePlatform, isLowest, oriPrice,
                        notMemberPrice, memberPrice, psnGameId)
                discountGameList.add(discountGameItem)
            }
        }
        catch (e: Exception)
        {
            Log.e(TAG, "getDiscountGameList error:" + e.message)
        }
        return discountGameList
    }
}