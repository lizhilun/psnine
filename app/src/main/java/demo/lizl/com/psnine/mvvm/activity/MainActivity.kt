package demo.lizl.com.psnine.mvvm.activity

import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.adapter.FragmentPageAdapter
import demo.lizl.com.psnine.mvvm.fragment.GameFragment
import demo.lizl.com.psnine.mvvm.fragment.HomeFragment
import demo.lizl.com.psnine.mvvm.fragment.UserFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity()
{
    override fun getLayoutResId() = R.layout.activity_main

    override fun initView()
    {
        val fragmentList = listOf<Fragment>(HomeFragment(), GameFragment(), UserFragment())
        vp_fragment_container.adapter = FragmentPageAdapter(fragmentList, supportFragmentManager)
        vp_fragment_container.offscreenPageLimit = 3
        vp_fragment_container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tl_bottom))
        tl_bottom.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(vp_fragment_container))
    }
}
