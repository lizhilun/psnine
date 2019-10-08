package demo.lizl.com.psnine.mvp.activity

import android.os.Bundle
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.mvp.contract.EmptyContract
import demo.lizl.com.psnine.mvp.fragment.UserFragment
import demo.lizl.com.psnine.mvp.presenter.EmptyPresenter
import demo.lizl.com.psnine.util.Constant

class UserDetailActivity : BaseActivity<EmptyPresenter>(), EmptyContract.View
{
    override fun getLayoutResId(): Int
    {
        return R.layout.activity_user_detail
    }

    override fun initPresenter() = EmptyPresenter()

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