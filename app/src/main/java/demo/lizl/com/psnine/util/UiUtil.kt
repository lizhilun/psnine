package demo.lizl.com.psnine.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.text.InputFilter
import android.text.Spanned
import com.scwang.smartrefresh.header.DeliveryHeader
import com.scwang.smartrefresh.layout.api.RefreshFooter
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import demo.lizl.com.psnine.R
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
            }
            else
            {
                return@InputFilter null
            }
        }

        /**
         * 跳转到APP详情界面（用于获取权限）
         */
        fun goToAppDetailPage()
        {
            val intent = Intent()
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
            intent.data = Uri.fromParts("package", UiApplication.instance.packageName, null)
            UiApplication.instance.startActivity(intent)
        }

        /**
         * 获取屏幕宽度
         */
        fun getScreenWidth(): Int
        {
            if (screenSize[0] == 0)
            {
                getScreenSize()
            }
            return screenSize[0]
        }

        /**
         * 获取屏幕高度
         */
        fun getScreenHeight(): Int
        {
            if (screenSize[1] == 0)
            {
                getScreenSize()
            }
            return screenSize[1]
        }

        /**
         * 获取屏幕尺寸
         */
        private fun getScreenSize()
        {
            val dm = UiApplication.instance.resources.displayMetrics
            screenSize[0] = dm.widthPixels
            screenSize[1] = dm.heightPixels
        }

        /**
         * 获取RefreshLayout默认header
         */
        fun getDefaultRefreshHeader(context: Context): RefreshHeader
        {
            val defaultHeader = DeliveryHeader(context)
            defaultHeader.setBackgroundColor(ContextCompat.getColor(context, R.color.colorDivideView))
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
    }
}