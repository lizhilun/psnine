package demo.lizl.com.psnine.custom.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.adapter.GameCupListAdapter
import demo.lizl.com.psnine.bean.GameCupItem
import demo.lizl.com.psnine.mvp.activity.CupTipsActivity
import demo.lizl.com.psnine.util.ActivityUtil
import demo.lizl.com.psnine.util.GlideUtil
import kotlinx.android.synthetic.main.layout_game_list_view.view.*

class GameCupListView(context: Context, attrs: AttributeSet?, defStyle: Int) : FrameLayout(context, attrs, defStyle)
{

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init
    {
        val convertView = LayoutInflater.from(context).inflate(R.layout.layout_game_list_view, null, false)
        addView(convertView)
    }

    private lateinit var gameCupName: String
    private lateinit var gameCupCoverUrl: String
    private lateinit var gameCupCount: String
    private lateinit var gameCupList: List<GameCupItem>

    fun setViewInfo(gameCupName: String, gameCupCoverUrl: String, gameCupCount: String, gameCupList: List<GameCupItem>)
    {
        this.gameCupName = gameCupName
        this.gameCupCoverUrl = gameCupCoverUrl
        this.gameCupCount = gameCupCount
        this.gameCupList = gameCupList
    }

    fun bindGameCupInfo()
    {
        GlideUtil.displayImage(iv_game_cup_cover, gameCupCoverUrl)

        tv_game_cup_name.text = gameCupName
        tv_game_cup_count.text = gameCupCount

        val gameCupListAdapter = GameCupListAdapter(gameCupList)
        rv_game_cup_list.layoutManager = LinearLayoutManager(context)
        rv_game_cup_list.adapter = gameCupListAdapter

        gameCupListAdapter.setOnCupItemClickListener {
            ActivityUtil.turnToActivity(CupTipsActivity::class.java, it.cupTipsUrl)
        }
    }
}