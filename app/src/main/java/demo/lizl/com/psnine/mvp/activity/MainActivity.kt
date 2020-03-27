package demo.lizl.com.psnine.mvp.activity

import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.adapter.FragmentPageAdapter
import demo.lizl.com.psnine.mvp.contract.EmptyContract
import demo.lizl.com.psnine.mvp.fragment.GameFragment
import demo.lizl.com.psnine.mvp.fragment.HomeFragment
import demo.lizl.com.psnine.mvp.fragment.UserFragment
import demo.lizl.com.psnine.mvp.presenter.EmptyPresenter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<EmptyPresenter>(), EmptyContract.View
{

    override fun getLayoutResId() = R.layout.activity_main

    override fun initPresenter() = EmptyPresenter()

    override fun initView()
    {
        val fragmentList = listOf<Fragment>(HomeFragment(), GameFragment(), UserFragment())
        val fragmentPagerAdapter = FragmentPageAdapter(fragmentList, supportFragmentManager)
        vp_fragment_container.adapter = fragmentPagerAdapter
        vp_fragment_container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tl_bottom))
        vp_fragment_container.offscreenPageLimit = 3
        tl_bottom.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(vp_fragment_container))
    }
}
