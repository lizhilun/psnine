package demo.lizl.com.psnine.mvvm.activity

import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.adapter.FragmentPageAdapter
import demo.lizl.com.psnine.databinding.ActivityMainBinding
import demo.lizl.com.psnine.mvvm.fragment.GameFragment
import demo.lizl.com.psnine.mvvm.fragment.HomeFragment
import demo.lizl.com.psnine.mvvm.fragment.UserFragment

class MainActivity : BaseActivity<ActivityMainBinding>()
{
    override fun getLayoutResId() = R.layout.activity_main

    override fun initView()
    {
        val fragmentList = listOf<Fragment>(HomeFragment(), GameFragment(), UserFragment())
        dataBinding.fragmentPagerAdapter = FragmentPageAdapter(fragmentList, supportFragmentManager)
        dataBinding.vpFragmentContainer.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(dataBinding.tlBottom))
        dataBinding.tlBottom.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(dataBinding.vpFragmentContainer))
    }
}
