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
import org.jsoup.nodes.Element

class HomeFragmentPresenter(context: Context, iView: IHomeFragmentView) : BasePresenter(context, iView)
{

    private val requestUrl = AppConfig.BASE_REQUEST_URL

    fun getIView() = iView as IHomeFragmentView

    fun refreshPostList()
    {
        GlobalScope.launch {
            try
            {
                val doc = Jsoup.connect(requestUrl).get()

                val element = doc.getElementsByClass("list")[0].childNodes()

                val postList = mutableListOf<PostItem>()

                for (ement in element)
                {
                    if (ement is Element)
                    {
                        val imageEment = ement.getElementsByTag("img")
                        val imageUrl = imageEment.attr("src")
                        val imageWidth = imageEment.attr("width")
                        val imageHeight = imageEment.attr("height")
                        val text = ement.getElementsByClass("title font16").text()
                        val userId = ement.getElementsByClass("psnnode").text()
                        val timeInfo = ement.getElementsByClass("meta").text()
                        val postDetailUrl = ement.getElementsByClass("title font16").select("a").attr("href")

                        val timeInfoList = timeInfo.split(" ")
                        val time = if (timeInfoList.size == 3)
                        {
                            timeInfoList[1]
                        }
                        else if (timeInfoList.size == 4)
                        {
                            timeInfoList[1] + " " + timeInfoList[2]
                        }
                        else
                        {
                            ""
                        }

                        val postItem = PostItem(imageUrl, text, userId, time, postDetailUrl)
                        postList.add(postItem)
                    }
                }

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
}