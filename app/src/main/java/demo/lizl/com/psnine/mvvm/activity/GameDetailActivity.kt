package demo.lizl.com.psnine.mvvm.activity

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.adapter.GameCupGroupListAdapter
import demo.lizl.com.psnine.adapter.InfoGridAdapter
import demo.lizl.com.psnine.constant.AppConstant
import demo.lizl.com.psnine.databinding.ActivityGameDetailBinding
import demo.lizl.com.psnine.model.GameCupGroupModel
import demo.lizl.com.psnine.mvvm.base.BaseActivity
import demo.lizl.com.psnine.mvvm.viewmodel.GameDetailViewModel

class GameDetailActivity : BaseActivity<ActivityGameDetailBinding>(R.layout.activity_game_detail)
{
    private val gameCupGroupListAdapter = GameCupGroupListAdapter()

    private lateinit var viewModel: GameDetailViewModel

    private var isFilterGet = false

    override fun initView()
    {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(GameDetailViewModel::class.java)

        val gameDetailUrl = intent?.getStringExtra(AppConstant.BUNDLE_DATA_STRING).orEmpty()

        viewModel.bindGameDetailUrl(gameDetailUrl)

        dataBinding.gameCupGroupListAdapter = gameCupGroupListAdapter

        dataBinding.icBack.setOnClickListener { onBackPressed() }
        dataBinding.icMore.setOnClickListener {
            isFilterGet = !isFilterGet
            updateGameCups()
        }

        dataBinding.refreshLayout.setOnRefreshListener { viewModel.refreshGameDetailInfo() }

        viewModel.refreshGameDetailInfo()

        viewModel.getGameInfoLiveData().observe(this, Observer { dataBinding.gameInfo = it })

        viewModel.getGameCupInfoLiveData().observe(this, Observer {
            dataBinding.cupInfoAdapter = InfoGridAdapter(it)
            dataBinding.rvGameInfo.layoutManager = GridLayoutManager(this, it.size)
        })

        viewModel.getGameCupGroupLiveData().observe(this, Observer {
            dataBinding.refreshLayout.finishRefresh()
            updateGameCups()
        })
    }

    private fun updateGameCups()
    {
        if (!isFilterGet)
        {
            gameCupGroupListAdapter.setDiffNewData(viewModel.getGameCupGroupLiveData().value)
            return
        }
        val gameCupGroupList = mutableListOf<GameCupGroupModel>()
        viewModel.getGameCupGroupLiveData().value?.forEach { gameCupGroupItem ->
            val cupList = gameCupGroupItem.gameCupList.filter { it.cupGetTime.isEmpty() }
            if (cupList.isEmpty()) return@forEach
            gameCupGroupList.add(gameCupGroupItem.copy(gameCupList = cupList))
        }
        gameCupGroupListAdapter.setDiffNewData(gameCupGroupList)
    }
}