package demo.lizl.com.psnine.adapter

import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.bean.GameCupItem
import demo.lizl.com.psnine.util.GameUtil
import demo.lizl.com.psnine.util.GlideUtil
import kotlinx.android.synthetic.main.item_game_cup.view.*

class GameCupListAdapter(gameCupList: List<GameCupItem>) :
        BaseQuickAdapter<GameCupItem, GameCupListAdapter.ViewHolder>(R.layout.item_game_cup, gameCupList.toMutableList())
{
    private var onCupItemClickListener: ((GameCupItem) -> Unit)? = null

    override fun convert(helper: ViewHolder, item: GameCupItem)
    {
        helper.bindViewHolder(item)
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView)
    {
        fun bindViewHolder(gameCupItem: GameCupItem)
        {
            GlideUtil.displayImage(context, gameCupItem.cupImageUrl, itemView.iv_cup)
            if (TextUtils.isEmpty(gameCupItem.cupGetTime))
            {
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent))
                itemView.tv_cup_time_get_time.isVisible = false
            }
            else
            {
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.color_game_cup_get_bg))
                itemView.tv_cup_time_get_time.isVisible = true
            }
            itemView.tv_tips.text = gameCupItem.tipInfo
            itemView.tv_tips.isVisible = gameCupItem.tipInfo.isNotBlank()
            itemView.iv_cup_bg.setBackgroundColor(GameUtil.getGameCupBgColor(gameCupItem.cupType))
            itemView.tv_cup_name.text = gameCupItem.cupName
            itemView.tv_cup_description.text = gameCupItem.cupDes
            itemView.tv_cup_time_get_time.text = gameCupItem.cupGetTime.replace(" ", "\n")

            itemView.setOnClickListener { onCupItemClickListener?.invoke(gameCupItem) }
        }
    }

    fun setOnCupItemClickListener(onCupItemClickListener: (GameCupItem) -> Unit)
    {
        this.onCupItemClickListener = onCupItemClickListener
    }
}