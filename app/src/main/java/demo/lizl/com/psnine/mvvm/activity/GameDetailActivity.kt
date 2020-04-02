package demo.lizl.com.psnine.mvvm.activity

import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.UiApplication
import demo.lizl.com.psnine.adapter.GameCupGroupListAdapter
import demo.lizl.com.psnine.constant.AppConstant
import demo.lizl.com.psnine.databinding.ActivityGameDetailBinding
import demo.lizl.com.psnine.mvvm.base.BaseActivity
import demo.lizl.com.psnine.mvvm.viewmodel.GameDetailViewModel

class GameDetailActivity : BaseActivity<ActivityGameDetailBinding>()
{
    override fun getLayoutResId() = R.layout.activity_game_detail

    override fun initView()
    {
        val gameDetailUrl = intent?.getStringExtra(AppConstant.BUNDLE_DATA_STRING).orEmpty()

        val viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(UiApplication.instance).create(GameDetailViewModel::class.java)

        viewModel.bindGameDetailUrl(gameDetailUrl)

        val gameCupGroupListAdapter = GameCupGroupListAdapter()
        dataBinding.gameCupGroupListAdapter = gameCupGroupListAdapter
        dataBinding.onBackBtnClickListener = View.OnClickListener { onBackPressed() }

        dataBinding.refreshLayout.setOnRefreshListener { viewModel.refreshGameDetailInfo() }

        viewModel.refreshGameDetailInfo()

        viewModel.getGameInfoLiveData().observe(this, Observer { dataBinding.gameInfo = it })

        viewModel.getGameCupInfoLiveData().observe(this, Observer { dataBinding.gameCupInfo = it })

        viewModel.getGameCupGroupLiveData().observe(this, Observer {
            dataBinding.refreshLayout.finishRefresh()
            gameCupGroupListAdapter.setNewData(it.toMutableList())
        })
    }
}