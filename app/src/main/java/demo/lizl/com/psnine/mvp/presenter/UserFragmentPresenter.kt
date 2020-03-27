package demo.lizl.com.psnine.mvp.presenter

import android.text.TextUtils
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.MutableLiveData
import demo.lizl.com.psnine.UiApplication
import demo.lizl.com.psnine.bean.GameInfoItem
import demo.lizl.com.psnine.bean.UserGameInfoItem
import demo.lizl.com.psnine.bean.UserInfoItem
import demo.lizl.com.psnine.config.AppConfig
import demo.lizl.com.psnine.mvp.contract.UserFragmentContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode
import org.jsoup.select.Elements

class UserFragmentPresenter(private var view: UserFragmentContract.View?) : UserFragmentContract.Presenter
{
    private val TAG = "UserFragmentPresenter"

    private var gameItemCount = 0
    private var curGamePage = 1

    private var curPsnId = AppConfig.CUR_PSN_ID

    private val webView = WebView(UiApplication.instance)

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

    init
    {
        initWebView()
    }

    private var sortGameCondition = SORT_GAME_BY_TIME
    private var sortGamePlatform = GAME_PLATFORM_ALL

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

    private val gamesLiveData = MutableLiveData<MutableList<GameInfoItem>>()

    override fun refreshUserPage()
    {
        GlobalScope.launch(Dispatchers.IO) {
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

            GlobalScope.launch(Dispatchers.Main) { view?.onUserInfoRefresh(userItemInfo) }

            val gameTableElement = psnInfoElement[1].select("td")
            val totalGameCount = (gameTableElement[0].childNodes()[0] as TextNode).text().toInt()
            val perfectGameCount = (gameTableElement[1].childNodes()[0] as TextNode).text().toInt()
            val pitGameCount = (gameTableElement[2].childNodes()[0] as Element).text().toInt()
            val gameCompletionRate = (gameTableElement[3].childNodes()[0] as TextNode).text()
            val totalCupCount = (gameTableElement[4].childNodes()[0] as TextNode).text().toInt()

            val userGameInfoItem = UserGameInfoItem(totalGameCount, perfectGameCount, pitGameCount, gameCompletionRate, totalCupCount)
            GlobalScope.launch(Dispatchers.Main) { view?.onUserGameInfoRefresh(userGameInfoItem) }

            refreshGameList(sortGamePlatform, SORT_GAME_BY_TIME)
        }
    }

    override fun refreshGameList(gamePlatform: Int, sortCondition: Int)
    {
        GlobalScope.launch(Dispatchers.IO) {

            curGamePage = 1
            sortGamePlatform = gamePlatform
            sortGameCondition = sortCondition

            val requestUrl = getRequestGameListUrl()

            val doc = Jsoup.connect(requestUrl).get()

            val gameCountInfo = doc.getElementsByClass("dropmenu")[0].getElementsByClass("h-p").text()
            val listElementList = doc.getElementsByClass("list")
            if (listElementList.size == 0)
            {
                return@launch
            }

            val gameElementList = listElementList.select("tr")
            val gameList = getGameListFromGameElementList(gameElementList)

            gameItemCount = gameCountInfo.substring(1, gameCountInfo.length - 1).toInt()
            GlobalScope.launch(Dispatchers.Main) { view?.onUserGameListUpdate(gameList, gameItemCount) }
        }
    }

    override fun loadMoreGameList()
    {
        GlobalScope.launch(Dispatchers.IO) {

            curGamePage++

            val requestUrl = getRequestGameListUrl()
            val doc = Jsoup.connect(requestUrl).get()

            val gameCountInfo = doc.getElementsByClass("dropmenu")[0].getElementsByClass("h-p").text()
            gameItemCount = gameCountInfo.substring(1, gameCountInfo.length - 1).toInt()
            val gameElementList = doc.getElementsByClass("list")[0].select("tr")
            val gameList = getGameListFromGameElementList(gameElementList)

            GlobalScope.launch(Dispatchers.Main) { view?.onMoreGameLoadFinish(gameList, gameItemCount) }
        }
    }

    override fun updateUserLevel()
    {
        val requestUrl = AppConfig.BASE_REQUEST_URL + "psnid/$curPsnId/upbase"
        webView.loadUrl(requestUrl)
    }

    override fun updateUserGame()
    {
        val requestUrl = AppConfig.BASE_REQUEST_URL + "psnid/$curPsnId/upgame"
        webView.loadUrl(requestUrl)
    }

    private fun getGameListFromGameElementList(gameElementList: Elements): MutableList<GameInfoItem>
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
            SORT_GAME_BY_TIME              -> "date"
            SORT_GAME_BY_FASTEST_PROGRESE  -> "ratio"
            SORT_GAME_BY_SLOWEST_PROGRESE  -> "slow"
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

    private fun initWebView()
    {
        webView.addJavascriptInterface(InJavaScriptLocalObj(), "java_obj")
        val wSetting = webView.settings
        wSetting.javaScriptEnabled = true
        wSetting.domStorageEnabled = true

        webView.webViewClient = object : WebViewClient()
        {
            override fun onPageFinished(view: WebView?, url: String?)
            {
                Log.d(TAG, "onPageFinished:$url")

                if (url!! == AppConfig.BASE_REQUEST_URL + "psnid/" + AppConfig.CUR_PSN_ID)
                {
                    return
                }

                // 获取页面内容
                view!!.loadUrl("javascript:window.java_obj.showSource(" + "document.getElementsByTagName('html')[0].innerHTML);");

                // 获取解析<meta name="share-description" content="获取到的值">
                view.loadUrl(
                        "javascript:window.java_obj.showDescription(" + "document.querySelector('meta[name=\"share-description\"]').getAttribute('content')" + ");");

                super.onPageFinished(view, url)
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean
            {
                Log.d(TAG, "shouldOverrideUrlLoading:$url")

                if (url!! == AppConfig.BASE_REQUEST_URL + "psnid/" + AppConfig.CUR_PSN_ID)
                {
                    this@UserFragmentPresenter.view?.onInfoUpdateFinish()
                }

                return super.shouldOverrideUrlLoading(view, url)
            }
        }
    }

    inner class InJavaScriptLocalObj
    {
        @JavascriptInterface
        fun showSource(html: String)
        {
            GlobalScope.launch {

                val doc = Jsoup.parse(html)
                val title = doc.getElementsByTag("title")[0].text()

                Log.d(TAG, "showSource:$title")

                GlobalScope.launch(Dispatchers.Main) {
                    view?.onInfoUpdateFailed(title)
                }
            }
        }

        @JavascriptInterface
        fun showDescription(str: String)
        {

        }
    }

    override fun onDestroy()
    {
        view = null
    }
}
