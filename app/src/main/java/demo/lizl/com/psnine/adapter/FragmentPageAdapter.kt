package demo.lizl.com.psnine.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class FragmentPageAdapter(var mList: List<Fragment>, fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
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