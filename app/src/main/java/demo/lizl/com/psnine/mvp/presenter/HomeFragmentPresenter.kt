package demo.lizl.com.psnine.mvp.presenter

import android.util.Log
import demo.lizl.com.psnine.bean.PostItem
import demo.lizl.com.psnine.config.AppConfig
import demo.lizl.com.psnine.mvp.contract.HomeFragmentContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class HomeFragmentPresenter(private val iView: HomeFragmentContract.View) : HomeFragmentContract.Presenter
{
    private val TAG = "HomeFragmentPresenter"

    private var curPostPage = 1

    override fun refreshPostList()
    {
        GlobalScope.launch {
            curPostPage = 1

            val postList = getPostItemListFromPostPage(curPostPage)

            GlobalScope.launch(Dispatchers.Main) {
                iView.onPostListRefresh(postList)
            }
        }
    }

    override fun LoadMorePost()
    {
        GlobalScope.launch {
            curPostPage++

            val postList = getPostItemListFromPostPage(curPostPage)

            GlobalScope.launch(Dispatchers.Main) {
                iView.onPostListLoadMore(postList)
            }
        }
    }

    private fun getPostItemListFromPostPage(postPage: Int): List<PostItem>
    {
        val postList = mutableListOf<PostItem>()

        try
        {
            val requestUrl = AppConfig.BASE_REQUEST_URL + "topic?page=$postPage"

            val doc = Jsoup.connect(requestUrl).get()

            val elements = doc.getElementsByClass("list")[0].select("li")

            for (postElement in elements)
            {
                val imageElement = postElement.getElementsByTag("img")
                val imageUrl = imageElement.attr("src")
                val postDetailElement = postElement.getElementsByClass("title font16")[0]
                val text = postDetailElement.text()
                val postDetailUrl = postDetailElement.select("a").attr("href")
                val userId = postElement.getElementsByClass("psnnode").text()
                val timeInfo = postElement.getElementsByClass("meta")[0].ownText()
                val postItem = PostItem(imageUrl, text, userId, timeInfo, postDetailUrl)
                postList.add(postItem)
            }
        }
        catch (e: Exception)
        {
            Log.e(TAG, "getPostItemListFromPostPage error:" + e.message)
        }

        return postList
    }
}