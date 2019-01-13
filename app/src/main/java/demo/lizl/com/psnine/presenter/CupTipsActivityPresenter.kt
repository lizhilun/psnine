package demo.lizl.com.psnine.presenter

import android.content.Context
import demo.lizl.com.psnine.iview.ICupTipsActivityView
import demo.lizl.com.psnine.model.ReplyPostItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class CupTipsActivityPresenter(context: Context, iView: ICupTipsActivityView) : BasePresenter<ICupTipsActivityView>(context, iView)
{

    private lateinit var requestUrl: String

    fun setCupTipsUrl(cupTipsUrl: String)
    {
        requestUrl = cupTipsUrl
    }

    fun refreshTipsList()
    {
        GlobalScope.launch {

            val doc = Jsoup.connect(requestUrl).get()

            val cupInfoElement = doc.getElementsByClass("min-inner mt40")[0]
            val cupCover = cupInfoElement.getElementsByTag("img").attr("src")
            val cupName = cupInfoElement.getElementsByTag("h1")[0].ownText()
            val cupDescription = cupInfoElement.getElementsByTag("em")[0].ownText()

            GlobalScope.launch(Dispatchers.Main) {
                iView.onCupInfoRefresh(cupName, cupDescription, cupCover)
            }

            val postList = mutableListOf<ReplyPostItem>()

            val postElementList = doc.getElementsByClass("post")
            for (postElement in postElementList)
            {
                val writerUrl = postElement.getElementsByTag("a")[0].attr("href")
                val postWriterAvatarUrl = postElement.getElementsByTag("img").attr("src")
                val postContent = postElement.getElementsByClass("content pb10").text()
                val writerId = postElement.getElementsByClass("psnnode")[0].ownText()
                val postTime = postElement.getElementsByClass("meta")[0].ownText()

                val replyPoItem = ReplyPostItem(postWriterAvatarUrl, postContent, writerId, postTime, writerUrl)

                postList.add(replyPoItem)
            }

            val newPostElementList = doc.getElementsByClass("list").select("li")
            for (newPostElement in newPostElementList)
            {
                if (newPostElement.getElementsByClass("ml64").size == 0)
                {
                    continue
                }
                val writerUrl = newPostElement.getElementsByTag("a")[0].attr("href")
                val postWriterAvatarUrl = newPostElement.getElementsByTag("img").attr("src")
                val postContent = newPostElement.getElementsByClass("content pb10").text()
                val writerId = newPostElement.getElementsByClass("psnnode")[0].ownText()

                val spanElementList = newPostElement.getElementsByClass("meta")[1].getElementsByTag("span")
                val postTime = spanElementList[spanElementList.size - 1].ownText()

                val subReplyList = mutableListOf<ReplyPostItem>()
                val subReplyPostListElement = newPostElement.getElementsByClass("sonlist").select("li")
                for (subReplyPostElement in subReplyPostListElement)
                {
                    val subContent = subReplyPostElement.getElementsByClass("content")[0].ownText()
                    val aElementList = subReplyPostElement.getElementsByTag("a")
                    val subWriterUrl = aElementList[1].attr("href")
                    val subWriterId = aElementList[1].ownText()
                    val atUserUrl = aElementList[2].attr("href")
                    val atUserId = aElementList[2].ownText()
                    val subPostTime = newPostElement.getElementsByClass("h-p")[0].text()

                    val subReplyPostItem = ReplyPostItem("", subContent, subWriterId, subPostTime, subWriterUrl)
                    subReplyPostItem.atUserId = atUserId
                    subReplyPostItem.atUserUrl = atUserUrl

                    subReplyList.add(subReplyPostItem)
                }

                val replyPoItem = ReplyPostItem(postWriterAvatarUrl, postContent, writerId, postTime, writerUrl)
                replyPoItem.subReplyPostList.addAll(subReplyList)

                postList.add(replyPoItem)
            }

            GlobalScope.launch(Dispatchers.Main) { iView.onCupTipPostListRefresh(postList) }
        }
    }
}