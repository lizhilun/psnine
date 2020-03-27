package demo.lizl.com.psnine.mvp.activity

import androidx.recyclerview.widget.LinearLayoutManager
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.adapter.ReplyPostListAdapter
import demo.lizl.com.psnine.bean.ReplyPostItem
import demo.lizl.com.psnine.constant.AppConstant
import demo.lizl.com.psnine.mvp.contract.CupTipsActivityContract
import demo.lizl.com.psnine.mvp.presenter.CupTipsActivityPresenter
import demo.lizl.com.psnine.util.GlideUtil
import demo.lizl.com.psnine.util.UiUtil
import kotlinx.android.synthetic.main.activity_cup_tips.*

class CupTipsActivity : BaseActivity<CupTipsActivityPresenter>(), CupTipsActivityContract.View
{

    private lateinit var replayPostListAdapter: ReplyPostListAdapter

    override fun getLayoutResId() = R.layout.activity_cup_tips

    override fun initPresenter() = CupTipsActivityPresenter(this)

    override fun initView()
    {
        val cupTipsUrl = intent?.getStringExtra(AppConstant.BUNDLE_DATA_STRING).orEmpty()

        presenter.setCupTipsUrl(cupTipsUrl)
        presenter.refreshTipsList()

        refresh_layout.setEnableLoadMore(false)
        refresh_layout.setRefreshHeader(UiUtil.getDefaultRefreshHeader(this))
        refresh_layout.setEnableRefresh(true)
        refresh_layout.setOnRefreshListener { presenter.refreshTipsList() }

        replayPostListAdapter = ReplyPostListAdapter()
        rv_tips_list.layoutManager = LinearLayoutManager(this)
        rv_tips_list.adapter = replayPostListAdapter

        ic_back.setOnClickListener { finish() }
    }

    override fun onCupInfoRefresh(cupName: String, cupDescription: String, cupCover: String)
    {
        tv_cup_name.text = cupName
        tv_cup_description.text = cupDescription
        GlideUtil.displayImage(this, cupCover, iv_cup_cover)
    }

    override fun onCupTipPostListRefresh(postList: MutableList<ReplyPostItem>)
    {
        replayPostListAdapter.setNewData(postList)
    }
}