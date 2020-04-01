package demo.lizl.com.psnine.mvvm.activity

import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import com.jeremyliao.liveeventbus.LiveEventBus
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.config.AppConfig
import demo.lizl.com.psnine.constant.EventConstant
import demo.lizl.com.psnine.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity<ActivityLoginBinding>()
{
    override fun getLayoutResId() = R.layout.activity_login

    override fun initView()
    {
        dataBinding.loginUrl = AppConfig.BASE_REQUEST_URL + "sign/in"

        val wSetting = dataBinding.wvView.settings
        wSetting.javaScriptEnabled = true

        dataBinding.wvView.webViewClient = object : WebViewClient()
        {
            override fun onPageFinished(view: WebView, url: String)
            {
                Log.d(TAG, "onPageFinished:$url")
                super.onPageFinished(view, url)
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean
            {
                Log.d(TAG, "shouldOverrideUrlLoading:$url")

                if (url == AppConfig.BASE_REQUEST_URL || url == AppConfig.BASE_REQUEST_URL + "psnid/" + AppConfig.CUR_PSN_ID)
                {
                    LiveEventBus.get(EventConstant.EVENT_LOGIN_RESULT).post(true)
                    onBackPressed()
                }

                return super.shouldOverrideUrlLoading(view, url)
            }
        }
    }
}