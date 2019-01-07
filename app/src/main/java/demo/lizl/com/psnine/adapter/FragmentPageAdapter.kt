package demo.lizl.com.psnine.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class FragmentPageAdapter(var mList: List<Fragment>, fm: FragmentManager?) : FragmentPagerAdapter(fm)
{
    override fun getItem(position: Int): Fragment
    {
        return mList[position]
    }

    override fun getCount(): Int
    {
        return mList.size
    }
}