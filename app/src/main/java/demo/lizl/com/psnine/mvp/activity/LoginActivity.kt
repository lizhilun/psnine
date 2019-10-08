package demo.lizl.com.psnine.mvp.activity

import android.text.TextUtils
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.config.AppConfig
import demo.lizl.com.psnine.event.LoginEvent
import demo.lizl.com.psnine.mvp.contract.EmptyContract
import demo.lizl.com.psnine.mvp.presenter.EmptyPresenter
import demo.lizl.com.psnine.util.Constant
import kotlinx.android.synthetic.main.activity_login.*
import org.greenrobot.eventbus.EventBus

class LoginActivity : BaseActivity<EmptyPresenter>(), EmptyContract.View
{
    override fun getLayoutResId(): Int
    {
        return R.layout.activity_login
    }

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
                    EventBus.getDefault().post(LoginEvent(Constant.LOGIN_RESULT_SUCCESS))
                    finish()
                }

                return super.shouldOverrideUrlLoading(view, url)
            }
        }
    }
}