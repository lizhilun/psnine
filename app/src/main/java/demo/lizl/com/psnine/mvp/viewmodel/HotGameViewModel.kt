package demo.lizl.com.psnine.mvp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import demo.lizl.com.psnine.bean.GameInfoItem
import demo.lizl.com.psnine.config.AppConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class HotGameViewModel : ViewModel()
{
    private val TAG = "HotGameViewModel"

    private val hotGameLiveData = MutableLiveData<MutableList<GameInfoItem>>()

    fun getHotGameLiveData() = hotGameLiveData

    fun refreshHitGameList()
    {
        GlobalScope.launch(Dispatchers.IO) {
            try
            {
                val hotGameList = mutableListOf<GameInfoItem>()

                val requestUrl = AppConfig.BASE_REQUEST_URL

                val doc = Jsoup.connect(requestUrl).get()
                val hotGameElementList = doc.getElementsByClass("showbar").select("a")

                hotGameElementList?.forEach { hotGameElement ->
                    try
                    {
                        val gameDetailUrl = hotGameElement.attr("href")
                        val gameInfoDoc = Jsoup.connect(gameDetailUrl).get()
                        val gameName = gameInfoDoc.getElementsByTag("title").text().split("　").first()

                        val gameInfoElement = gameInfoDoc.getElementsByClass("darklist").first()
                        val psnGameDetailUrl = gameInfoElement?.getElementsByTag("a")?.attr("href").orEmpty()
                        val gameCoverUrl = gameInfoElement?.getElementsByTag("img")?.attr("src").orEmpty()
                        val gameInfoItem = GameInfoItem(gameCoverUrl, gameName, psnGameDetailUrl)

                        val infoText = gameInfoElement?.text().orEmpty().split(" ")
                        gameInfoItem.isPS4Game = infoText.contains("PS4")
                        gameInfoItem.isPS3Game = infoText.contains("PS3")
                        gameInfoItem.isPSVGame = infoText.contains("PSVITA")
                        hotGameList.add(gameInfoItem)
                    }
                    catch (e: Exception)
                    {
                        Log.e(TAG, "refreshHitGameList error:", e)
                    }
                }

                hotGameLiveData.postValue(hotGameList)
            }
            catch (e: Exception)
            {
                Log.e(TAG, "refreshHitGameList error:" + e.message)
            }
        }
    }
}