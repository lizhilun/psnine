package demo.lizl.com.psnine.activity

import android.text.TextUtils
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.iview.ILoginActivityView
import demo.lizl.com.psnine.presenter.LoginActivityPresenter
import demo.lizl.com.psnine.util.Constant
import demo.lizl.com.psnine.util.UiUtil
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), ILoginActivityView
{
    override fun getLayoutResId(): Int
    {
        return R.layout.activity_login
    }

    override fun initPresenter()
    {
        presenter = LoginActivityPresenter(this, this)
    }

    override fun initView()
    {
        val bundle = intent.extras!!
        val loginUrl = bundle.getString(Constant.BUNDLE_DATA_STRING, "")

        refresh_layout.setEnableLoadMore(false)
        refresh_layout.setRefreshHeader(UiUtil.getDefaultRefreshHeader(this))
        refresh_layout.setEnableRefresh(true)
        refresh_layout.setOnRefreshListener { wv_view.reload() }

        wv_view.loadUrl(loginUrl)

        val wSetting = wv_view.settings
        wSetting.javaScriptEnabled = true

        wv_view.webViewClient = object : WebViewClient()
        {
            override fun onPageFinished(view: WebView?, url: String?)
            {
                Log.d(TAG, "onPageFinished:$url")
                super.onPageFinished(view, url)
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean
            {
                Log.d(TAG, "shouldOverrideUrlLoading:$url")

                if (TextUtils.isEmpty(url))
                {
                    return true
                }

                return super.shouldOverrideUrlLoading(view, url)
            }
        }
    }
}