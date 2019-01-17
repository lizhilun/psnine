package demo.lizl.com.psnine.adapter

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup

class ViewPagerAdapter(private val viewList: List<View>) : PagerAdapter()
{
    private val pageTitleList = mutableListOf<String>()

    fun setPageTitleList(pageTitleList: List<String>)
    {
        this.pageTitleList.clear()
        this.pageTitleList.addAll(pageTitleList)
    }

    override fun getCount() = viewList.size

    override fun isViewFromObject(view: View, `object`: Any) = view === `object`

    override fun instantiateItem(container: ViewGroup, position: Int): Any
    {
        val view = viewList[position]
        if (view.parent != null)
        {
            container.removeView(view)
        }
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any)
    {
        container.removeView(`object` as View)
    }

    override fun getItemPosition(`object`: Any): Int
    {
        return if (!viewList.contains(`object`))
        {
            PagerAdapter.POSITION_NONE
        }
        else viewList.indexOf(`object`)
    }

    override fun getPageTitle(position: Int): CharSequence?
    {
        if (position < pageTitleList.size)
        {
            return pageTitleList[position]
        }
        return super.getPageTitle(position)
    }
}