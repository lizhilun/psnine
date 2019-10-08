package demo.lizl.com.psnine.adapter

import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.bean.GameInfoItem
import demo.lizl.com.psnine.util.GlideUtil
import kotlinx.android.synthetic.main.item_game.view.*

class GameListAdapter : BaseAdapter<GameInfoItem, GameListAdapter.ViewHolder>()
{
    private var gameItemClickListener: GameItemClickListener? = null

    override fun createCustomViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_game, parent, false))
    }

    override fun bindCustomViewHolder(holder: ViewHolder, gameInfoItem: GameInfoItem, position: Int)
    {
        holder.onBindViewHolder(gameInfoItem)
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView)
    {
        fun onBindViewHolder(gameInfoItem: GameInfoItem)
        {
            GlideUtil.displayImage(context, gameInfoItem.coverUrl, itemView.iv_game_icon)
            itemView.tv_game_name.text = gameInfoItem.gameName
            itemView.tv_last_update_time.text = gameInfoItem.lastUpdateTime
            itemView.tv_completion_rate.text = gameInfoItem.completionRate
            itemView.tv_label_ps3.visibility = if (gameInfoItem.isPS3Game) View.VISIBLE else View.GONE
            itemView.tv_label_psv.visibility = if (gameInfoItem.isPSVGame) View.VISIBLE else View.GONE
            itemView.tv_label_ps4.visibility = if (gameInfoItem.isPS4Game) View.VISIBLE else View.GONE

            if (TextUtils.isEmpty(gameInfoItem.completionRate))
            {
                itemView.npb_completion_rate.visibility = View.GONE
            }
            else
            {
                val completionRate = gameInfoItem.completionRate!!.replace("%", "").toInt()

                val reachedColor = when
                {
                    completionRate < 25 -> ContextCompat.getColor(context, R.color.color_game_completion_rate_low)
                    completionRate < 50 -> ContextCompat.getColor(context, R.color.color_game_completion_rate_just_so_so)
                    completionRate < 75 -> ContextCompat.getColor(context, R.color.color_game_completion_rate_ok)
                    else -> ContextCompat.getColor(context, R.color.color_game_completion_rate_good)
                }
                val barHeight = context.resources.getDimensionPixelOffset(R.dimen.game_item_completion_rate_bar_height)
                itemView.npb_completion_rate.visibility = View.VISIBLE
                itemView.npb_completion_rate.unreachedBarHeight = barHeight.toFloat()
                itemView.npb_completion_rate.reachedBarHeight = barHeight.toFloat()
                itemView.npb_completion_rate.max = 100
                itemView.npb_completion_rate.reachedBarColor = reachedColor
                itemView.npb_completion_rate.progress = completionRate
            }

            if (TextUtils.isEmpty(gameInfoItem.perfectRate))
            {
                itemView.tv_perfect_rate.visibility = View.GONE
                itemView.tv_perfect_difficult.visibility = View.GONE
            }
            else
            {
                itemView.tv_perfect_rate.visibility = View.VISIBLE
                itemView.tv_perfect_difficult.visibility = View.VISIBLE
                itemView.tv_perfect_rate.text = gameInfoItem.perfectRate

                val perfectRate = gameInfoItem.perfectRate!!.substring(0, gameInfoItem.perfectRate!!.indexOf("%")).toFloat()
                val prefectRateText: String
                val preferctReateTextColor: Int
                when
                {
                    perfectRate > 60L ->
                    {
                        preferctReateTextColor = ContextCompat.getColor(context, R.color.color_perfect_rate_extremely_easy)
                        prefectRateText = context.getString(R.string.extremely_easy)
                    }
                    perfectRate > 40L ->
                    {
                        preferctReateTextColor = ContextCompat.getColor(context, R.color.color_perfect_rate_easy)
                        prefectRateText = context.getString(R.string.easy)
                    }
                    perfectRate > 25L ->
                    {
                        preferctReateTextColor = ContextCompat.getColor(context, R.color.color_perfect_rate_normal)
                        prefectRateText = context.getString(R.string.normal)
                    }
                    perfectRate > 15L ->
                    {
                        preferctReateTextColor = ContextCompat.getColor(context, R.color.color_perfect_rate_troublesome)
                        prefectRateText = context.getString(R.string.troublesome)
                    }
                    perfectRate > 5L ->
                    {
                        preferctReateTextColor = ContextCompat.getColor(context, R.color.color_perfect_rate_hard)
                        prefectRateText = context.getString(R.string.hard)
                    }
                    perfectRate > 0L ->
                    {
                        preferctReateTextColor = ContextCompat.getColor(context, R.color.color_perfect_rate_extremely_hard)
                        prefectRateText = context.getString(R.string.extremely_hard)
                    }
                    else ->
                    {
                        preferctReateTextColor = ContextCompat.getColor(context, R.color.color_perfect_rate_impossible)
                        prefectRateText = context.getString(R.string.impossible)
                    }
                }
                itemView.tv_perfect_difficult.setTextColor(preferctReateTextColor)
                itemView.tv_perfect_difficult.text = prefectRateText
            }

            if (TextUtils.isEmpty(gameInfoItem.gameCupInfo))
            {
                itemView.tv_cup_info.visibility = View.GONE
            }
            else
            {
                itemView.tv_cup_info.visibility = View.VISIBLE
                itemView.tv_cup_info.text = gameInfoItem.gameCupInfo
            }

            itemView.setOnClickListener { gameItemClickListener?.onGameItemClick(gameInfoItem) }
        }
    }

    interface GameItemClickListener
    {
        fun onGameItemClick(gameInfoItem: GameInfoItem)
    }

    fun setGameItemClickListener(gameItemClickListener: GameItemClickListener)
    {
        this.gameItemClickListener = gameItemClickListener
    }

}