package demo.lizl.com.psnine.activity

import android.text.TextUtils
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.config.AppConfig
import demo.lizl.com.psnine.iview.IPostDetailActivityView
import demo.lizl.com.psnine.presenter.PostDetailActivityPresenter
import demo.lizl.com.psnine.util.Constant
import demo.lizl.com.psnine.util.UiUtil
import kotlinx.android.synthetic.main.activity_post_detail.*

class PostDetailActivity : BaseActivity<PostDetailActivityPresenter>(), IPostDetailActivityView
{

    override fun getLayoutResId(): Int
    {
        return R.layout.activity_post_detail
    }

    override fun initPresenter()
    {
        presenter = PostDetailActivityPresenter(this, this)
    }

    override fun initView()
    {
        val bundle = intent.extras!!
        val postUrl = bundle.getString(Constant.BUNDLE_DATA_STRING, "")

        refresh_layout.setEnableLoadMore(false)
        refresh_layout.setRefreshHeader(UiUtil.getDefaultRefreshHeader(this))
        refresh_layout.setEnableRefresh(true)
        refresh_layout.setOnRefreshListener { wv_view.reload() }

        wv_view.loadUrl(postUrl)

        val wSetting = wv_view.settings
        wSetting.javaScriptEnabled = true

        wv_view.webViewClient = object : WebViewClient()
        {
            override fun onPageFinished(view: WebView?, url: String?)
            {
                Log.d(TAG, "onPageFinished:$url")

                if (url != null && url.contains("/topic/"))
                {
                    val funStr1 = "javascript:function getClass(parent,sClass) { var aEle=parent.getElementsByTagName('div'); var aResult=[]; var i=0; for(i<0;i<aEle.length;i++) { if(aEle[i].className==sClass) { aResult.push(aEle[i]); } }; return aResult; } ";

                    wv_view.loadUrl(funStr1)

                    val funStr2 = "javascript:function hideOther() {getClass(document,'box pd10 mt20')[0].style.display='none';getClass(document,'header')[0].style.display='none'}"

                    wv_view.loadUrl(funStr2)

                    wv_view.loadUrl("javascript:hideOther();")
                }
                super.onPageFinished(view, url)
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean
            {
                Log.d(TAG, "shouldOverrideUrlLoading:$url")

                if (TextUtils.isEmpty(url))
                {
                    return true
                }

                if (url!!.contains(AppConfig.BASE_REQUEST_URL + "psngame/"))
                {
                    turnToGameDetailActivity(url)
                    return true
                }
                else if (url.contains(AppConfig.BASE_REQUEST_URL + "psnid/"))
                {
                    val psnId = url.substring(url.lastIndexOf("/") + 1)
                    turnToUserDetailActivity(psnId)
                    return true
                }

                return super.shouldOverrideUrlLoading(view, url)
            }
        }
    }

    override fun onBackPressed()
    {
        if (wv_view.canGoBack())
        {
            wv_view.goBack()
            return
        }
        super.onBackPressed()
    }
}