package demo.lizl.com.psnine.mvvm.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.StringUtils
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.config.AppConfig
import demo.lizl.com.psnine.model.InfoModel
import demo.lizl.com.psnine.model.UserInfoModel
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

    private val userInfoLiveData = MutableLiveData<UserInfoModel>()
    private val userGameInfoLiveData = MutableLiveData<MutableList<InfoModel>>()

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

                val userItemInfo = UserInfoModel(userId, avatarUrl, userLevel, userExperience, userCupInfo)
                userInfoLiveData.postValue(userItemInfo)

                val gameTableElement = psnInfoElement[1].select("td")
                val totalGameCount = (gameTableElement[0].childNodes()[0] as TextNode).text()
                val perfectGameCount = (gameTableElement[1].childNodes()[0] as TextNode).text()
                val pitGameCount = (gameTableElement[2].childNodes()[0] as Element).text()
                val gameCompletionRate = (gameTableElement[3].childNodes()[0] as TextNode).text()
                val totalCupCount = (gameTableElement[4].childNodes()[0] as TextNode).text()

                userGameInfoLiveData.postValue(mutableListOf<InfoModel>().apply {
                    add(InfoModel(StringUtils.getString(R.string.total_game_count), totalGameCount))
                    add(InfoModel(StringUtils.getString(R.string.perfect_game_count), perfectGameCount))
                    add(InfoModel(StringUtils.getString(R.string.pit_game_count), pitGameCount))
                    add(InfoModel(StringUtils.getString(R.string.completion_rate), gameCompletionRate))
                    add(InfoModel(StringUtils.getString(R.string.total_cup_count), totalCupCount))
                })
            }
            catch (e: Exception)
            {
                Log.e(TAG, "refreshUserPage error:", e)
            }
        }
    }
}