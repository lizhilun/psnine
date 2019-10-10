package demo.lizl.com.psnine.mvp.activity

import androidx.recyclerview.widget.LinearLayoutManager
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.adapter.ReplyPostListAdapter
import demo.lizl.com.psnine.bean.ReplyPostItem
import demo.lizl.com.psnine.mvp.contract.CupTipsActivityContract
import demo.lizl.com.psnine.mvp.presenter.CupTipsActivityPresenter
import demo.lizl.com.psnine.util.Constant
import demo.lizl.com.psnine.util.GlideUtil
import demo.lizl.com.psnine.util.UiUtil
import kotlinx.android.synthetic.main.activity_cup_tips.*

class CupTipsActivity : BaseActivity<CupTipsActivityPresenter>(), CupTipsActivityContract.View
{

    override fun getLayoutResId(): Int
    {
        return R.layout.activity_cup_tips
    }

    override fun initPresenter() = CupTipsActivityPresenter(this)

    override fun initView()
    {
        val bundle = intent.extras!!
        val cupTipsUrl = bundle.getString(Constant.BUNDLE_DATA_STRING, "")

        presenter.setCupTipsUrl(cupTipsUrl)
        presenter.refreshTipsList()

        refresh_layout.setEnableLoadMore(false)
        refresh_layout.setRefreshHeader(UiUtil.getDefaultRefreshHeader(this))
        refresh_layout.setEnableRefresh(true)
        refresh_layout.setOnRefreshListener { presenter.refreshTipsList() }

        ic_back.setOnClickListener { finish() }
    }

    override fun onCupInfoRefresh(cupName: String, cupDescription: String, cupCover: String)
    {
        tv_cup_name.text = cupName
        tv_cup_description.text = cupDescription
        GlideUtil.displayImage(this, cupCover, iv_cup_cover)
    }

    override fun onCupTipPostListRefresh(postList: List<ReplyPostItem>)
    {
        val replayPostListAdapter = ReplyPostListAdapter(this, postList)
        rv_tips_list.layoutManager = LinearLayoutManager(this)
        rv_tips_list.adapter = replayPostListAdapter
    }
}