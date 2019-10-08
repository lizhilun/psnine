package demo.lizl.com.psnine.mvp.activity

import android.Manifest
import android.content.pm.PackageManager
import android.support.design.widget.TabLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.adapter.FragmentPageAdapter
import demo.lizl.com.psnine.customview.dialog.BaseDialog
import demo.lizl.com.psnine.customview.dialog.DialogOperationConfirm
import demo.lizl.com.psnine.mvp.fragment.GameFragment
import demo.lizl.com.psnine.mvp.fragment.HomeFragment
import demo.lizl.com.psnine.mvp.fragment.UserFragment
import demo.lizl.com.psnine.mvp.contract.EmptyContract
import demo.lizl.com.psnine.mvp.presenter.EmptyPresenter
import demo.lizl.com.psnine.util.ToastUtil
import demo.lizl.com.psnine.util.UiUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<EmptyPresenter>(), EmptyContract.View
{

    private lateinit var fragmentList: List<Fragment>
    private var dialogOperationConfirm: DialogOperationConfirm? = null

    private val REQUEST_CODE_READ_EX_PERMISSION = 23

    override fun getLayoutResId(): Int
    {
        return R.layout.activity_main
    }

    override fun initPresenter() = EmptyPresenter()

    override fun initView()
    {
        checkReadStoragePermission()
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

    /**
     * 检查内部存储读取权限
     */
    private fun checkReadStoragePermission(): Boolean
    {
        // 权限以获取直接返回
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            return true
        }
        // 权限被拒绝过但是用户没有设置权限弹窗不再弹出的情况下继续申请权限
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_READ_EX_PERMISSION
            )
        }
        // 权限被拒绝且不再允许弹出申请权限弹窗的情况下弹出跳转到APP详情确认弹窗（用于重新设置权限）
        else
        {
            dialogOperationConfirm = DialogOperationConfirm(this, getString(R.string.notify_failed_to_get_permission), getString(R.string.notify_permission_be_refused))
            dialogOperationConfirm?.setOnConfirmButtonClickListener(object : BaseDialog.OnConfirmButtonClickListener
            {
                override fun onConfirmButtonClick()
                {
                    UiUtil.goToAppDetailPage()
                }
            })
            dialogOperationConfirm?.show()
        }
        return false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED)
        {
            ToastUtil.showToast(R.string.notify_failed_to_get_permission)
        }
    }
}
