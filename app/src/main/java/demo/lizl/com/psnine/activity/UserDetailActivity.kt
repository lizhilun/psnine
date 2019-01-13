package demo.lizl.com.psnine.activity

import android.os.Bundle
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.fragment.UserFragment
import demo.lizl.com.psnine.iview.IUserDetailActivityView
import demo.lizl.com.psnine.presenter.UserDetailActivityPresenter
import demo.lizl.com.psnine.util.Constant

class UserDetailActivity : BaseActivity<UserDetailActivityPresenter>(), IUserDetailActivityView
{
    override fun getLayoutResId(): Int
    {
        return R.layout.activity_user_detail
    }

    override fun initPresenter()
    {
        presenter = UserDetailActivityPresenter(this, this)
    }

    override fun initView()
    {
        val bundle = intent.extras!!
        val psnId = bundle.getString(Constant.BUNDLE_DATA_STRING, "")
        showUserFragment(psnId)
    }

    private fun showUserFragment(psnId: String)
    {
        val userFragment = UserFragment()
        val bundle = Bundle()
        bundle.putString(Constant.BUNDLE_DATA_STRING, psnId)
        userFragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.container, userFragment).commit()
        supportFragmentManager.beginTransaction().show(userFragment).commit()
    }
}