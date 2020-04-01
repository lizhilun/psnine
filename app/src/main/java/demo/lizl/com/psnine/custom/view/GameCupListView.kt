package demo.lizl.com.psnine.custom.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.adapter.GameCupListAdapter
import demo.lizl.com.psnine.bean.GameCupGroupItem
import demo.lizl.com.psnine.mvvm.activity.CupTipsActivity
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

    private lateinit var gameCupGroupItem: GameCupGroupItem

    fun bindGameCupGroupItem(gameCupGroupItem: GameCupGroupItem)
    {
        this.gameCupGroupItem = gameCupGroupItem
    }

    fun bindGameCupInfo()
    {
        GlideUtil.displayImage(iv_game_cup_cover, gameCupGroupItem.gameCupCoverUrl)

        tv_game_cup_name.text = gameCupGroupItem.gameCupName
        tv_game_cup_count.text = gameCupGroupItem.gameCupCount

        val gameCupListAdapter = GameCupListAdapter(gameCupGroupItem.gameCupList)
        rv_game_cup_list.layoutManager = LinearLayoutManager(context)
        rv_game_cup_list.adapter = gameCupListAdapter

        gameCupListAdapter.setOnCupItemClickListener {
            ActivityUtil.turnToActivity(CupTipsActivity::class.java, it.cupTipsUrl)
        }
    }
}