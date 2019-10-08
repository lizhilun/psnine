package demo.lizl.com.psnine.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.bean.GameCupItem
import demo.lizl.com.psnine.util.Constant
import demo.lizl.com.psnine.util.GlideUtil
import kotlinx.android.synthetic.main.item_game_cup.view.*

class GameCupListAdapter(val context: Context, private val gameCupList: List<GameCupItem>) : RecyclerView.Adapter<GameCupListAdapter.ViewHolder>()
{
    private var layoutInflater: LayoutInflater? = null
    private var colorByCupType: SparseIntArray = SparseIntArray()
    private val colorCupGet = ContextCompat.getColor(context, R.color.color_game_cup_get_bg)
    private val colorCupNotGet = ContextCompat.getColor(context, R.color.transparent)

    private var onCupItemClickListener: OnCupItemClickListener? = null

    init
    {
        layoutInflater = LayoutInflater.from(context)
        colorByCupType.append(
                Constant.GAME_CUP_TYPE_PLATINUM, ContextCompat.getColor(context, R.color.color_game_cup_bg_platinum)
        )
        colorByCupType.append(
                Constant.GAME_CUP_TYPE_GOLD, ContextCompat.getColor(context, R.color.color_game_cup_bg_gold)
        )
        colorByCupType.append(
                Constant.GAME_CUP_TYPE_SILVER, ContextCompat.getColor(context, R.color.color_game_cup_bg_silver)
        )
        colorByCupType.append(
                Constant.GAME_CUP_TYPE_BRONZE, ContextCompat.getColor(context, R.color.color_game_cup_bg_bronze)
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(layoutInflater!!.inflate(R.layout.item_game_cup, parent, false))
    }

    override fun getItemCount() = gameCupList.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int)
    {
        viewHolder.onBindViewHolder(gameCupList[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {

        fun onBindViewHolder(gameCupItem: GameCupItem)
        {

            GlideUtil.displayImage(context, gameCupItem.cupImageUrl, itemView.iv_cup)
            if (TextUtils.isEmpty(gameCupItem.cupGetTime))
            {
                itemView.setBackgroundColor(colorCupNotGet)
                itemView.tv_cup_time_get_time.visibility = View.GONE
            }
            else
            {
                itemView.setBackgroundColor(colorCupGet)
                itemView.tv_cup_time_get_time.visibility = View.VISIBLE
            }
            itemView.tv_tips.text = gameCupItem.tipInfo
            itemView.tv_tips.visibility = if (TextUtils.isEmpty(gameCupItem.tipInfo)) View.GONE else View.VISIBLE
            itemView.iv_cup_bg.setBackgroundColor(colorByCupType[gameCupItem.cupType])
            itemView.tv_cup_name.text = gameCupItem.cupName
            itemView.tv_cup_description.text = gameCupItem.cupDes
            itemView.tv_cup_time_get_time.text = gameCupItem.cupGetTime.replace(" ", "\n")

            itemView.setOnClickListener { onCupItemClickListener?.onCupItemClick(gameCupItem) }

        }
    }

    interface OnCupItemClickListener
    {
        fun onCupItemClick(gameCupItem: GameCupItem)
    }

    fun setOnCupItemClickListener(onCupItemClickListener: OnCupItemClickListener)
    {
        this.onCupItemClickListener = onCupItemClickListener
    }
}