package demo.lizl.com.psnine.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import demo.lizl.com.psnine.bean.CupInfoItem
import demo.lizl.com.psnine.bean.ReplyPostItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class CupTipViewModel : ViewModel()
{

    private var curCupTipUrl = ""

    private val cupTipLiveData = MutableLiveData<MutableList<ReplyPostItem>>()
    private val cupInfoLiveData = MutableLiveData<CupInfoItem>()

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

            cupInfoLiveData.postValue(CupInfoItem(cupName, cupDescription, cupCover))

            val postList = mutableListOf<ReplyPostItem>()
            doc.getElementsByClass("post").forEach { postElement ->

                val writerUrl = postElement.getElementsByTag("a").first()?.attr("href").orEmpty()
                val postWriterAvatarUrl = postElement.getElementsByTag("img").attr("src")
                val postContent = postElement.getElementsByClass("content pb10").text()
                val writerId = postElement.getElementsByClass("psnnode").first()?.ownText().orEmpty()
                val postTime = postElement.getElementsByClass("meta").first()?.ownText().orEmpty()

                val replyPoItem = ReplyPostItem(postWriterAvatarUrl, postContent, writerId, postTime, writerUrl)

                postList.add(replyPoItem)
            }

            doc.getElementsByClass("list").select("li").forEach { newPostElement ->

                if (newPostElement.getElementsByClass("ml64").isEmpty()) return@forEach
                val writerUrl = newPostElement.getElementsByTag("a").first()?.attr("href").orEmpty()
                val postWriterAvatarUrl = newPostElement.getElementsByTag("img").attr("src")
                val postContent = newPostElement.getElementsByClass("content pb10").text()
                val writerId = newPostElement.getElementsByClass("psnnode").first()?.ownText().orEmpty()
                val metaElement = newPostElement.getElementsByClass("meta")
                val spanElementList = if (metaElement.size > 1) newPostElement.getElementsByClass("meta")[1].getElementsByTag("span") else null
                val postTime = spanElementList?.last()?.ownText().orEmpty()

                val subReplyList = mutableListOf<ReplyPostItem>()
                newPostElement.getElementsByClass("sonlist").select("li").forEach { subReplyPostElement ->
                    val subContent = subReplyPostElement.getElementsByClass("content").first()?.ownText().orEmpty()
                    val aElementList = subReplyPostElement.getElementsByTag("a")
                    val subWriterUrl = if (aElementList.size > 1) aElementList[1].attr("href") else ""
                    val subWriterId = if (aElementList.size > 1) aElementList[1].ownText() else ""
                    val atUserUrl = if (aElementList.size > 2) aElementList[2].attr("href") else ""
                    val atUserId = if (aElementList.size > 2) aElementList[2].ownText() else ""
                    val subPostTime = newPostElement.getElementsByClass("h-p").first()?.text().orEmpty()

                    val subReplyPostItem = ReplyPostItem("", subContent, subWriterId, subPostTime, subWriterUrl)
                    subReplyPostItem.atUserId = atUserId
                    subReplyPostItem.atUserUrl = atUserUrl

                    subReplyList.add(subReplyPostItem)
                }

                val replyPoItem = ReplyPostItem(postWriterAvatarUrl, postContent, writerId, postTime, writerUrl)
                replyPoItem.subReplyPostList.addAll(subReplyList)

                postList.add(replyPoItem)
            }

            cupTipLiveData.postValue(postList)
        }
    }
}