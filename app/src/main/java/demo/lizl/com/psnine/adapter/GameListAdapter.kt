package demo.lizl.com.psnine.adapter

import android.text.TextUtils
import android.view.View
import androidx.core.view.isVisible
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.bean.GameInfoItem
import demo.lizl.com.psnine.util.GameUtil
import demo.lizl.com.psnine.util.GlideUtil
import kotlinx.android.synthetic.main.item_game.view.*

class GameListAdapter : BaseQuickAdapter<GameInfoItem, GameListAdapter.ViewHolder>(R.layout.item_game)
{
    private var gameItemClickListener: ((GameInfoItem) -> Unit)? = null

    override fun convert(helper: ViewHolder, item: GameInfoItem)
    {
        helper.bindViewHolder(item)
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView)
    {
        fun bindViewHolder(gameInfoItem: GameInfoItem)
        {
            GlideUtil.displayImage(context, gameInfoItem.coverUrl, itemView.iv_game_icon)

            itemView.tv_game_name.text = gameInfoItem.gameName
            itemView.tv_last_update_time.text = gameInfoItem.lastUpdateTime
            itemView.tv_completion_rate.text = gameInfoItem.completionRate
            itemView.tv_label_ps3.isVisible = gameInfoItem.isPS3Game
            itemView.tv_label_psv.isVisible = gameInfoItem.isPSVGame
            itemView.tv_label_ps4.isVisible = gameInfoItem.isPS4Game

            if (TextUtils.isEmpty(gameInfoItem.completionRate))
            {
                itemView.npb_completion_rate.isVisible = false
            }
            else
            {
                val completionRate = gameInfoItem.completionRate!!.replace("%", "").toInt()

                val barHeight = context.resources.getDimensionPixelOffset(R.dimen.game_item_completion_rate_bar_height)
                itemView.npb_completion_rate.isVisible = true
                itemView.npb_completion_rate.unreachedBarHeight = barHeight.toFloat()
                itemView.npb_completion_rate.reachedBarHeight = barHeight.toFloat()
                itemView.npb_completion_rate.max = 100
                itemView.npb_completion_rate.reachedBarColor = GameUtil.getCompletionRateColor(completionRate.toFloat())
                itemView.npb_completion_rate.progress = completionRate
            }

            if (TextUtils.isEmpty(gameInfoItem.perfectRate))
            {
                itemView.tv_perfect_rate.isVisible = false
                itemView.tv_perfect_difficult.isVisible = false
            }
            else
            {
                itemView.tv_perfect_rate.isVisible = true
                itemView.tv_perfect_difficult.isVisible = true
                itemView.tv_perfect_rate.text = gameInfoItem.perfectRate

                val perfectRate = gameInfoItem.perfectRate!!.substring(0, gameInfoItem.perfectRate!!.indexOf("%")).toFloat()
                itemView.tv_perfect_difficult.setTextColor(GameUtil.getPrefectRateColor(perfectRate))
                itemView.tv_perfect_difficult.text = GameUtil.getPrefectRateDescription(perfectRate)
            }

            itemView.tv_cup_info.isVisible = gameInfoItem.gameCupInfo.orEmpty().isNotBlank()
            itemView.tv_cup_info.text = gameInfoItem.gameCupInfo

            itemView.setOnClickListener { gameItemClickListener?.invoke(gameInfoItem) }
        }
    }

    fun setGameItemClickListener(gameItemClickListener: (GameInfoItem) -> Unit)
    {
        this.gameItemClickListener = gameItemClickListener
    }

}