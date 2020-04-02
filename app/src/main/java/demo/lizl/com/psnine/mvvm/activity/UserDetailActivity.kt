package demo.lizl.com.psnine.mvvm.activity

import android.os.Bundle
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.constant.AppConstant
import demo.lizl.com.psnine.databinding.ActivityUserDetailBinding
import demo.lizl.com.psnine.mvvm.base.BaseActivity
import demo.lizl.com.psnine.mvvm.fragment.UserFragment

class UserDetailActivity : BaseActivity<ActivityUserDetailBinding>()
{
    override fun getLayoutResId() = R.layout.activity_user_detail

    override fun initView()
    {
        val psnId = intent?.getStringExtra(AppConstant.BUNDLE_DATA_STRING).orEmpty()
        val userFragment = UserFragment()
        userFragment.arguments = Bundle().apply { putString(AppConstant.BUNDLE_DATA_STRING, psnId) }
        supportFragmentManager.beginTransaction().replace(R.id.container, userFragment).commit()
        supportFragmentManager.beginTransaction().show(userFragment).commit()
    }
}