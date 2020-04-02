package demo.lizl.com.psnine.util

import android.webkit.WebView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.daimajia.numberprogressbar.NumberProgressBar

object DataBindUtil
{
    @JvmStatic
    @BindingAdapter("app:adapter")
    fun bindAdapter(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>?)
    {
        recyclerView.adapter = adapter
    }

    @JvmStatic
    @BindingAdapter("app:url")
    fun bindWebViewUrl(webView: WebView, url: String?)
    {
        webView.loadUrl(url)
    }

    @JvmStatic
    @BindingAdapter("app:fragmentPagerAdapter")
    fun bindFragmentPagerAdapter(viewPager: ViewPager, fragmentPagerAdapter: FragmentPagerAdapter?)
    {
        viewPager.adapter = fragmentPagerAdapter
    }

    @JvmStatic
    @BindingAdapter("app:offscreenPageLimit")
    fun bindFragmentPagerAdapter(viewPager: ViewPager, offscreenPageLimit: Int?)
    {
        viewPager.offscreenPageLimit = offscreenPageLimit ?: 1
    }

    @JvmStatic
    @BindingAdapter("app:strText")
    fun bindText(textView: TextView, strText: Any?)
    {
        textView.text = strText?.toString()
    }

    @JvmStatic
    @BindingAdapter("app:progress_current")
    fun bindProgress(numberProgressBar: NumberProgressBar, progress: Int?)
    {
        numberProgressBar.progress = progress ?: 0
    }

    @JvmStatic
    @BindingAdapter("app:progress_reached_color")
    fun bindReachedColor(numberProgressBar: NumberProgressBar, color: Int?)
    {
        color ?: return
        numberProgressBar.reachedBarColor = color
    }
}