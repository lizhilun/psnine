package demo.lizl.com.psnine.presenter

import android.content.Context
import android.util.Log
import demo.lizl.com.psnine.config.AppConfig
import demo.lizl.com.psnine.iview.IHomeFragmentView
import demo.lizl.com.psnine.model.PostItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class HomeFragmentPresenter(context: Context, iView: IHomeFragmentView) : BasePresenter(context, iView)
{

    private var curPostPage = 1

    fun getIView() = iView as IHomeFragmentView

    fun refreshPostList()
    {
        GlobalScope.launch {
            try
            {
                curPostPage = 1

                val postList = getPostItemListFromPostPage(curPostPage)

                GlobalScope.launch(Dispatchers.Main) {
                    getIView().onPostListRefresh(postList)
                }
            }
            catch (e: Exception)
            {
                Log.e(TAG, e.toString())
            }
        }
    }

    fun LoadMorePost()
    {
        GlobalScope.launch {
            try
            {
                curPostPage++

                val postList = getPostItemListFromPostPage(curPostPage)

                GlobalScope.launch(Dispatchers.Main) {
                    getIView().onPostListLoadMore(postList)
                }
            }
            catch (e: Exception)
            {
                Log.e(TAG, e.toString())
            }
        }
    }

    private fun getPostItemListFromPostPage(postPage: Int): List<PostItem>
    {
        val requestUrl = AppConfig.BASE_REQUEST_URL + "topic?page=$postPage"

        val doc = Jsoup.connect(requestUrl).get()

        val elements = doc.getElementsByClass("list")[0].select("li")

        val postList = mutableListOf<PostItem>()

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

        return postList
    }
}