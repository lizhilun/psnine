package demo.lizl.com.psnine.mvp.activity

import android.text.TextUtils
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import com.jeremyliao.liveeventbus.LiveEventBus
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.config.AppConfig
import demo.lizl.com.psnine.mvp.contract.EmptyContract
import demo.lizl.com.psnine.mvp.presenter.EmptyPresenter
import demo.lizl.com.psnine.util.EventConstant
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity<EmptyPresenter>(), EmptyContract.View
{
    override fun getLayoutResId() = R.layout.activity_login

    override fun initPresenter() = EmptyPresenter()

    override fun initView()
    {
        val loginUrl = AppConfig.BASE_REQUEST_URL + "sign/in"
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

                if (url!! == AppConfig.BASE_REQUEST_URL || url == AppConfig.BASE_REQUEST_URL + "psnid/" + AppConfig.CUR_PSN_ID)
                {
                    LiveEventBus.get(EventConstant.EVENT_LOGIN_RESULT).post(true)
                    finish()
                }

                return super.shouldOverrideUrlLoading(view, url)
            }
        }
    }

    override fun onBackPressed()
    {
        LiveEventBus.get(EventConstant.EVENT_LOGIN_RESULT).post(false)
        super.onBackPressed()
    }
}