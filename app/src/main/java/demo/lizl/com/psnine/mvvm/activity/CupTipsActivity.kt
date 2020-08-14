package demo.lizl.com.psnine.mvvm.activity

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.UiApplication
import demo.lizl.com.psnine.adapter.ReplyPostListAdapter
import demo.lizl.com.psnine.constant.AppConstant
import demo.lizl.com.psnine.databinding.ActivityCupTipsBinding
import demo.lizl.com.psnine.mvvm.base.BaseActivity
import demo.lizl.com.psnine.mvvm.viewmodel.CupTipViewModel

class CupTipsActivity : BaseActivity<ActivityCupTipsBinding>(R.layout.activity_cup_tips)
{
    override fun initView()
    {
        val cupTipsUrl = intent?.getStringExtra(AppConstant.BUNDLE_DATA_STRING).orEmpty()

        val viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(UiApplication.instance).create(CupTipViewModel::class.java)

        viewModel.bindCupTipsUrl(cupTipsUrl)

        val replyPostListAdapter = ReplyPostListAdapter()

        dataBinding.refreshLayout.setOnRefreshListener { viewModel.refreshTipsList() }
        dataBinding.icBack.setOnClickListener { onBackPressed() }
        dataBinding.replyPostListAdapter = replyPostListAdapter

        viewModel.refreshTipsList()

        viewModel.getCupInfoLivaData().observe(this, Observer { dataBinding.cupInfoItem = it })

        viewModel.getCupTipLiveData().observe(this, Observer {
            dataBinding.refreshLayout.finishRefresh()
            replyPostListAdapter.setNewData(it.toMutableList())
        })
    }
}