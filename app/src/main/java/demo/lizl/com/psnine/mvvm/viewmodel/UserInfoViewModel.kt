package demo.lizl.com.psnine.mvvm.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import demo.lizl.com.psnine.bean.UserGameInfoItem
import demo.lizl.com.psnine.bean.UserInfoItem
import demo.lizl.com.psnine.config.AppConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode

class UserInfoViewModel : ViewModel()
{
    private val TAG = "UserInfoViewModel"

    private var curPsnId = ""

    private val userInfoLiveData = MutableLiveData<UserInfoItem>()
    private val userGameInfoLiveData = MutableLiveData<UserGameInfoItem>()

    fun getUserInfoLiveData() = userInfoLiveData

    fun getUserGameInfoLiveData() = userGameInfoLiveData

    fun bindPsnId(psnId: String)
    {
        curPsnId = psnId
    }

    fun refreshUserInfo()
    {
        GlobalScope.launch(Dispatchers.IO) {

            try
            {
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
                userInfoLiveData.postValue(userItemInfo)

                val gameTableElement = psnInfoElement[1].select("td")
                val totalGameCount = (gameTableElement[0].childNodes()[0] as TextNode).text().toInt()
                val perfectGameCount = (gameTableElement[1].childNodes()[0] as TextNode).text().toInt()
                val pitGameCount = (gameTableElement[2].childNodes()[0] as Element).text().toInt()
                val gameCompletionRate = (gameTableElement[3].childNodes()[0] as TextNode).text()
                val totalCupCount = (gameTableElement[4].childNodes()[0] as TextNode).text().toInt()

                val userGameInfoItem = UserGameInfoItem(totalGameCount, perfectGameCount, pitGameCount, gameCompletionRate, totalCupCount)
                userGameInfoLiveData.postValue(userGameInfoItem)
            }
            catch (e: Exception)
            {
                Log.e(TAG, "refreshUserPage error:", e)
            }
        }
    }
}