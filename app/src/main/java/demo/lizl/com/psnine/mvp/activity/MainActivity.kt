package demo.lizl.com.psnine.mvp.activity

import android.Manifest
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.adapter.FragmentPageAdapter
import demo.lizl.com.psnine.customview.dialog.BaseDialog
import demo.lizl.com.psnine.customview.dialog.DialogOperationConfirm
import demo.lizl.com.psnine.mvp.contract.EmptyContract
import demo.lizl.com.psnine.mvp.fragment.GameFragment
import demo.lizl.com.psnine.mvp.fragment.HomeFragment
import demo.lizl.com.psnine.mvp.fragment.UserFragment
import demo.lizl.com.psnine.mvp.presenter.EmptyPresenter
import demo.lizl.com.psnine.util.UiUtil
import kotlinx.android.synthetic.main.activity_main.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions

@RuntimePermissions class MainActivity : BaseActivity<EmptyPresenter>(), EmptyContract.View
{

    private lateinit var fragmentList: List<Fragment>
    private var dialogOperationConfirm: DialogOperationConfirm? = null

    override fun getLayoutResId(): Int
    {
        return R.layout.activity_main
    }

    override fun initPresenter() = EmptyPresenter()

    override fun initView()
    {
        checkReadStoragePermissionWithPermissionCheck()
        initViewPage()
    }

    private fun initViewPage()
    {
        val homeFragment = HomeFragment()
        val gameFragment = GameFragment()
        val userFragment = UserFragment()

        fragmentList = listOf(homeFragment, gameFragment, userFragment)
        val fragmentPagerAdapter = FragmentPageAdapter(fragmentList, supportFragmentManager)
        vp_fragment_container.adapter = fragmentPagerAdapter
        vp_fragment_container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tl_bottom))
        vp_fragment_container.offscreenPageLimit = 3
        tl_bottom.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(vp_fragment_container))
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) fun checkReadStoragePermission()
    {
        Log.d(TAG, "checkReadStoragePermission")
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE) fun onPermissionDenied()
    {
        Log.d(TAG, "onPermissionDenied")

        dialogOperationConfirm =
            DialogOperationConfirm(this, getString(R.string.notify_failed_to_get_permission), getString(R.string.notify_permission_be_refused))
        dialogOperationConfirm?.setOnConfirmButtonClickListener(object : BaseDialog.OnConfirmButtonClickListener
        {
            override fun onConfirmButtonClick()
            {
                checkReadStoragePermissionWithPermissionCheck()
            }
        })
        dialogOperationConfirm?.show()
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE) fun onPermissionNeverAskAgain()
    {
        Log.d(TAG, "onPermissionNeverAskAgain")

        dialogOperationConfirm = DialogOperationConfirm(
            this, getString(R.string.notify_failed_to_get_permission), getString(R.string.notify_permission_be_refused_and_never_ask_again)
        )
        dialogOperationConfirm?.setOnConfirmButtonClickListener(object : BaseDialog.OnConfirmButtonClickListener
        {
            override fun onConfirmButtonClick()
            {
                UiUtil.goToAppDetailPage()
            }
        })
        dialogOperationConfirm?.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray)
    {
        Log.d(TAG, "onRequestPermissionsResult")

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // NOTE: delegate the permission handling to generated function
        onRequestPermissionsResult(requestCode, grantResults)
    }
}
