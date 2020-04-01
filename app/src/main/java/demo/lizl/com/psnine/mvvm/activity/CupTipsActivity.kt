package demo.lizl.com.psnine.mvvm.activity

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.UiApplication
import demo.lizl.com.psnine.adapter.ReplyPostListAdapter
import demo.lizl.com.psnine.constant.AppConstant
import demo.lizl.com.psnine.mvvm.viewmodel.CupTipViewModel
import demo.lizl.com.psnine.util.GlideUtil
import kotlinx.android.synthetic.main.activity_cup_tips.*

class CupTipsActivity : BaseActivity()
{

    private lateinit var replayPostListAdapter: ReplyPostListAdapter

    override fun getLayoutResId() = R.layout.activity_cup_tips

    override fun initView()
    {
        val cupTipsUrl = intent?.getStringExtra(AppConstant.BUNDLE_DATA_STRING).orEmpty()

        val viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(UiApplication.instance).create(CupTipViewModel::class.java)

        viewModel.bindCupTipsUrl(cupTipsUrl)

        refresh_layout.setEnableRefresh(true)
        refresh_layout.setOnRefreshListener { viewModel.refreshTipsList() }

        replayPostListAdapter = ReplyPostListAdapter()
        rv_tips_list.layoutManager = LinearLayoutManager(this)
        rv_tips_list.adapter = replayPostListAdapter

        ic_back.setOnClickListener { finish() }

        viewModel.refreshTipsList()

        viewModel.getCupInfoLivaData().observe(this, Observer {
            tv_cup_name.text = it.cupName
            tv_cup_description.text = it.cupDescription
            GlideUtil.displayImage(iv_cup_cover, it.cupCover)
        })

        viewModel.getCupTipLiveData().observe(this, Observer {
            replayPostListAdapter.setNewData(it.toMutableList())
        })
    }
}