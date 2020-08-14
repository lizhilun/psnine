package demo.lizl.com.psnine.mvvm.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import demo.lizl.com.psnine.model.PostModel
import demo.lizl.com.psnine.config.AppConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class PostViewModel : ViewModel()
{
    private val TAG = "PostViewModel"

    private var curPostPage = 1

    private val postLiveData = MutableLiveData<MutableList<PostModel>>()

    fun getPostLiveData() = postLiveData

    fun refreshPostList()
    {
        GlobalScope.launch(Dispatchers.IO) {
            curPostPage = 1
            postLiveData.postValue(getPostItemListFromPostPage(curPostPage))
        }
    }

    fun loadMorePost()
    {
        GlobalScope.launch(Dispatchers.IO) {
            curPostPage++
            val postList = postLiveData.value ?: mutableListOf()
            postList.addAll(getPostItemListFromPostPage(curPostPage))
            postLiveData.postValue(postList)
        }
    }

    private fun getPostItemListFromPostPage(postPage: Int): MutableList<PostModel>
    {
        val postList = mutableListOf<PostModel>()

        try
        {
            val requestUrl = AppConfig.BASE_REQUEST_URL + "topic?page=$postPage"

            val doc = Jsoup.connect(requestUrl).get()

            doc.getElementsByClass("list").first()?.select("li")?.forEach { postElement ->

                val imageElement = postElement.getElementsByTag("img")
                val imageUrl = imageElement.attr("src")
                val postDetailElement = postElement.getElementsByClass("title font16").first() ?: return@forEach
                val text = postDetailElement.text()
                val postDetailUrl = postDetailElement.select("a").attr("href")
                val userId = postElement.getElementsByClass("psnnode").text()
                val timeInfo = postElement.getElementsByClass("meta").first()?.ownText().orEmpty()
                val postItem = PostModel(imageUrl, text, userId, timeInfo, postDetailUrl)
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