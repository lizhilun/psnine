package demo.lizl.com.psnine.util

import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import com.blankj.utilcode.util.Utils
import demo.lizl.com.psnine.config.AppConfig
import demo.lizl.com.psnine.constant.AppConstant
import demo.lizl.com.psnine.model.ResultModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

object UserInfoUpdateUtil
{
    private val TAG = "UserInfoUpdateUtil"

    private val webView by lazy {
        WebView(Utils.getApp()).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
        }
    }

    fun updateUserInfo(psnId: String, updateUrl: String, resultCallBack: (ResultModel) -> Unit)
    {
        webView.removeJavascriptInterface("java_obj")
        webView.addJavascriptInterface(InJavaScriptLocalObj(resultCallBack), "java_obj")

        webView.webViewClient = object : WebViewClient()
        {
            override fun onPageFinished(view: WebView?, url: String?)
            {
                Log.d(TAG, "onPageFinished:$url")

                if (url == "${AppConfig.BASE_REQUEST_URL}psnid/$psnId") return

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

                if (url == "${AppConfig.BASE_REQUEST_URL}psnid/$psnId")
                {
                    resultCallBack.invoke(ResultModel(AppConstant.RESULT_SUCCESS))
                }

                return super.shouldOverrideUrlLoading(view, url)
            }
        }

        webView.loadUrl(updateUrl)
    }

    class InJavaScriptLocalObj(private val resultCallBack: (ResultModel) -> Unit)
    {
        @JavascriptInterface
        fun showSource(html: String)
        {
            GlobalScope.launch {

                val doc = Jsoup.parse(html)
                val title = doc.getElementsByTag("title")[0].text()

                Log.d(TAG, "showSource:$title")

                resultCallBack.invoke(ResultModel(AppConstant.RESULT_FAILED, title))
            }
        }

        @JavascriptInterface
        fun showDescription(str: String)
        {

        }
    }
}