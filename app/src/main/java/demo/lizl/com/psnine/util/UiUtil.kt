package demo.lizl.com.psnine.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.InputFilter
import android.text.Spanned
import androidx.core.content.ContextCompat
import com.scwang.smartrefresh.header.DeliveryHeader
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import demo.lizl.com.psnine.R


object UiUtil
{
    private val noWrapInputFilter = InputFilter { source: CharSequence, _: Int, _: Int, _: Spanned, _: Int, _: Int ->
        if (source.toString().contentEquals("\n"))
        {
            return@InputFilter ""
        }
        else
        {
            return@InputFilter null
        }
    }

    /**
     * 获取RefreshLayout默认header
     */
    fun getDefaultRefreshHeader(context: Context) = DeliveryHeader(context).apply {
        setBackgroundColor(ContextCompat.getColor(context, R.color.colorDivideView))
    }

    /**
     * 获取RefreshLayout默认header
     */
    fun getDefaultRefreshFooter(context: Context) = BallPulseFooter(context)

    /**
     * 获取过滤换行的InputFilter
     */
    fun getNoWrapInputFilter() = noWrapInputFilter

    /**
     * 跳转到浏览器
     */
    fun turnToWebBrowser(context: Context, requestUrl: String)
    {
        val intent = Intent()
        intent.action = "android.intent.action.VIEW"
        intent.data = Uri.parse(requestUrl)
        context.startActivity(intent)
    }
}