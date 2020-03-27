package demo.lizl.com.psnine.adapter

import androidx.core.content.ContextCompat
import android.text.TextUtils
import android.view.View
import androidx.core.view.isVisible
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.bean.GameInfoItem
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

                val reachedColor = ContextCompat.getColor(context, when
                {
                    completionRate < 25 -> R.color.color_game_completion_rate_low
                    completionRate < 50 -> R.color.color_game_completion_rate_just_so_so
                    completionRate < 75 -> R.color.color_game_completion_rate_ok
                    else                -> R.color.color_game_completion_rate_good
                })
                val barHeight = context.resources.getDimensionPixelOffset(R.dimen.game_item_completion_rate_bar_height)
                itemView.npb_completion_rate.isVisible = true
                itemView.npb_completion_rate.unreachedBarHeight = barHeight.toFloat()
                itemView.npb_completion_rate.reachedBarHeight = barHeight.toFloat()
                itemView.npb_completion_rate.max = 100
                itemView.npb_completion_rate.reachedBarColor = reachedColor
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
                val prefectRateText: String
                val prefectRateTextColor: Int
                when
                {
                    perfectRate > 60L ->
                    {
                        prefectRateTextColor = ContextCompat.getColor(context, R.color.color_perfect_rate_extremely_easy)
                        prefectRateText = context.getString(R.string.extremely_easy)
                    }
                    perfectRate > 40L ->
                    {
                        prefectRateTextColor = ContextCompat.getColor(context, R.color.color_perfect_rate_easy)
                        prefectRateText = context.getString(R.string.easy)
                    }
                    perfectRate > 25L ->
                    {
                        prefectRateTextColor = ContextCompat.getColor(context, R.color.color_perfect_rate_normal)
                        prefectRateText = context.getString(R.string.normal)
                    }
                    perfectRate > 15L ->
                    {
                        prefectRateTextColor = ContextCompat.getColor(context, R.color.color_perfect_rate_troublesome)
                        prefectRateText = context.getString(R.string.troublesome)
                    }
                    perfectRate > 5L  ->
                    {
                        prefectRateTextColor = ContextCompat.getColor(context, R.color.color_perfect_rate_hard)
                        prefectRateText = context.getString(R.string.hard)
                    }
                    perfectRate > 0L  ->
                    {
                        prefectRateTextColor = ContextCompat.getColor(context, R.color.color_perfect_rate_extremely_hard)
                        prefectRateText = context.getString(R.string.extremely_hard)
                    }
                    else              ->
                    {
                        prefectRateTextColor = ContextCompat.getColor(context, R.color.color_perfect_rate_impossible)
                        prefectRateText = context.getString(R.string.impossible)
                    }
                }
                itemView.tv_perfect_difficult.setTextColor(prefectRateTextColor)
                itemView.tv_perfect_difficult.text = prefectRateText
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