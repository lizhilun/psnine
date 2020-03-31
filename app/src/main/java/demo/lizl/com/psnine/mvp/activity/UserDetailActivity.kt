package demo.lizl.com.psnine.mvp.activity

import android.os.Bundle
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.constant.AppConstant
import demo.lizl.com.psnine.mvp.fragment.UserFragment
import demo.lizl.com.psnine.mvp.presenter.EmptyPresenter

class UserDetailActivity : BaseActivity<EmptyPresenter>()
{
    override fun getLayoutResId() = R.layout.activity_user_detail

    override fun initPresenter() = EmptyPresenter()

    override fun initView()
    {
        val psnId = intent?.getStringExtra(AppConstant.BUNDLE_DATA_STRING).orEmpty()
        showUserFragment(psnId)
    }

    private fun showUserFragment(psnId: String)
    {
        val userFragment = UserFragment()
        val bundle = Bundle()
        bundle.putString(AppConstant.BUNDLE_DATA_STRING, psnId)
        userFragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.container, userFragment).commit()
        supportFragmentManager.beginTransaction().show(userFragment).commit()
    }
}