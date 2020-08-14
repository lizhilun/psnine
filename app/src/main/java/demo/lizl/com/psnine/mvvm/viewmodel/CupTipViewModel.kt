package demo.lizl.com.psnine.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import demo.lizl.com.psnine.model.CupInfoModel
import demo.lizl.com.psnine.model.ReplyPostModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class CupTipViewModel : ViewModel()
{

    private var curCupTipUrl = ""

    private val cupTipLiveData = MutableLiveData<MutableList<ReplyPostModel>>()
    private val cupInfoLiveData = MutableLiveData<CupInfoModel>()

    fun getCupTipLiveData() = cupTipLiveData

    fun getCupInfoLivaData() = cupInfoLiveData

    fun bindCupTipsUrl(cupTipUrl: String)
    {
        curCupTipUrl = cupTipUrl
    }

    fun refreshTipsList()
    {
        GlobalScope.launch(Dispatchers.IO) {

            val doc = Jsoup.connect(curCupTipUrl).get()

            val cupInfoElement = doc.getElementsByClass("min-inner mt40").first() ?: return@launch
            val cupCover = cupInfoElement.getElementsByTag("img").attr("src")
            val cupName = cupInfoElement.getElementsByTag("h1").first()?.ownText().orEmpty()
            val cupDescription = cupInfoElement.getElementsByTag("em").first()?.ownText().orEmpty()

            cupInfoLiveData.postValue(CupInfoModel(cupName, cupDescription, cupCover))

            val postList = mutableListOf<ReplyPostModel>()
            doc.getElementsByClass("post").forEach { postElement ->

                val writerUrl = postElement.getElementsByTag("a").first()?.attr("href").orEmpty()
                val postWriterAvatarUrl = postElement.getElementsByTag("img").attr("src")
                val postContent = postElement.getElementsByClass("content pb10").text()
                val writerId = postElement.getElementsByClass("psnnode").first()?.ownText().orEmpty()
                val postTime = postElement.getElementsByClass("meta").first()?.ownText().orEmpty()

                val replyPoItem = ReplyPostModel(postWriterAvatarUrl, postContent, writerId, postTime, writerUrl)

                postList.add(replyPoItem)
            }

            doc.getElementsByClass("list").select("li").forEach { newPostElement ->

                if (newPostElement.getElementsByClass("ml64").isEmpty()) return@forEach
                val writerUrl = newPostElement.getElementsByTag("a").first()?.attr("href").orEmpty()
                val postWriterAvatarUrl = newPostElement.getElementsByTag("img").attr("src")
                val postContent = newPostElement.getElementsByClass("content pb10").text()
                val writerId = newPostElement.getElementsByClass("psnnode").first()?.ownText().orEmpty()
                val spanElementList = newPostElement.getElementsByClass("meta").getOrNull(1)?.getElementsByTag("span")
                val postTime = spanElementList?.last()?.ownText().orEmpty()

                val subReplyList = mutableListOf<ReplyPostModel>()
                newPostElement.getElementsByClass("sonlist").select("li").forEach { subReplyPostElement ->
                    val subContent = subReplyPostElement.getElementsByClass("content").first()?.ownText().orEmpty()
                    val aElementList = subReplyPostElement.getElementsByTag("a")
                    val subWriterUrl = aElementList.getOrNull(1)?.attr("href").orEmpty()
                    val subWriterId = aElementList.getOrNull(1)?.ownText().orEmpty()
                    val atUserUrl = aElementList.getOrNull(2)?.attr("href").orEmpty()
                    val atUserId = aElementList.getOrNull(2)?.ownText().orEmpty()
                    val subPostTime = newPostElement.getElementsByClass("h-p").first()?.text().orEmpty()

                    val subReplyPostItem = ReplyPostModel("", subContent, subWriterId, subPostTime, subWriterUrl).apply {
                        this.atUserId = atUserId
                        this.atUserUrl = atUserUrl
                    }

                    subReplyList.add(subReplyPostItem)
                }

                val replyPoItem = ReplyPostModel(postWriterAvatarUrl, postContent, writerId, postTime, writerUrl)
                replyPoItem.subReplyPostList.addAll(subReplyList)

                postList.add(replyPoItem)
            }

            cupTipLiveData.postValue(postList)
        }
    }
}