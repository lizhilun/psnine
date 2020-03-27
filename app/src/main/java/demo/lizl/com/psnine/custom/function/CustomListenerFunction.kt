package demo.lizl.com.psnine.custom.function

import androidx.viewpager.widget.ViewPager

fun ViewPager.addOnPageChangeListener(onPageSelectedListener: (position: Int) -> Unit)
{
    this.addOnPageChangeListener(object : ViewPager.OnPageChangeListener
    {
        override fun onPageScrollStateChanged(p0: Int)
        {
        }

        override fun onPageScrolled(p0: Int, p1: Float, p2: Int)
        {
        }

        override fun onPageSelected(position: Int)
        {
            onPageSelectedListener.invoke(position)
        }
    })
}

