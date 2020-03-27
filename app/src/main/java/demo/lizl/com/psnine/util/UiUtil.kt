package demo.lizl.com.psnine.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import android.text.InputFilter
import android.text.Spanned
import com.scwang.smartrefresh.header.DeliveryHeader
import com.scwang.smartrefresh.layout.api.RefreshFooter
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import demo.lizl.com.psnine.UiApplication


class UiUtil
{
    companion object
    {
        private val screenSize = intArrayOf(0, 0)

        private val noWrapInputFilter = InputFilter { source: CharSequence, _: Int, _: Int, _: Spanned, _: Int, _: Int ->
            if (source.toString().contentEquals("\n"))
            {
                return@InputFilter ""
            } else
            {
                return@InputFilter null
            }
        }

        /**
         * 获取RefreshLayout默认header
         */
        fun getDefaultRefreshHeader(context: Context): RefreshHeader
        {
            val defaultHeader = DeliveryHeader(context)
            defaultHeader.setBackgroundColor(ContextCompat.getColor(context, demo.lizl.com.psnine.R.color.colorDivideView))
            return defaultHeader
        }

        /**
         * 获取RefreshLayout默认header
         */
        fun getDefaultRefreshFooter(context: Context): RefreshFooter
        {
            return BallPulseFooter(context)
        }

        /**
         * 获取过滤换行的InputFilter
         */
        fun getNoWrapInputFilter(): InputFilter
        {
            return noWrapInputFilter
        }

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
}