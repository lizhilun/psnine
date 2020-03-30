package demo.lizl.com.psnine.mvp.activity

import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.config.AppConfig
import demo.lizl.com.psnine.constant.AppConstant
import demo.lizl.com.psnine.mvp.contract.EmptyContract
import demo.lizl.com.psnine.mvp.presenter.EmptyPresenter
import demo.lizl.com.psnine.util.ActivityUtil
import kotlinx.android.synthetic.main.activity_post_detail.*

class PostDetailActivity : BaseActivity<EmptyPresenter>(), EmptyContract.View
{

    override fun getLayoutResId() = R.layout.activity_post_detail

    override fun initPresenter() = EmptyPresenter()

    override fun initView()
    {
        val postUrl = intent?.getStringExtra(AppConstant.BUNDLE_DATA_STRING).orEmpty()

        refresh_layout.setEnableLoadMore(false)
        refresh_layout.setEnableRefresh(true)
        refresh_layout.setOnRefreshListener { wv_view.reload() }

        wv_view.loadUrl(postUrl)

        val wSetting = wv_view.settings
        wSetting.javaScriptEnabled = true

        wv_view.webViewClient = object : WebViewClient()
        {
            override fun onPageFinished(view: WebView, url: String)
            {
                Log.d(TAG, "onPageFinished:$url")

                if (url.contains("/topic/"))
                {
                    val funStr1 = "javascript:function getClass(parent,sClass) { var aEle=parent.getElementsByTagName('div'); var aResult=[]; var i=0; for(i<0;i<aEle.length;i++) { if(aEle[i].className==sClass) { aResult.push(aEle[i]); } }; return aResult; } ";

                    wv_view.loadUrl(funStr1)

                    val funStr2 = "javascript:function hideOther() {getClass(document,'box pd10 mt20')[0].style.display='none';getClass(document,'header')[0].style.display='none'}"

                    wv_view.loadUrl(funStr2)

                    wv_view.loadUrl("javascript:hideOther();")
                }
                super.onPageFinished(view, url)
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean
            {
                Log.d(TAG, "shouldOverrideUrlLoading:$url")

                if (url.isBlank()) return true

                if (url.contains(AppConfig.BASE_REQUEST_URL + "psngame/"))
                {
                    ActivityUtil.turnToActivity(GameDetailActivity::class.java, url)
                    return true
                }
                else if (url.contains(AppConfig.BASE_REQUEST_URL + "psnid/"))
                {
                    val psnId = url.substring(url.lastIndexOf("/") + 1)
                    ActivityUtil.turnToActivity(UserDetailActivity::class.java, psnId)
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