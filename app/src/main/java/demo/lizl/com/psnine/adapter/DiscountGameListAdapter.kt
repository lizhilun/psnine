package demo.lizl.com.psnine.adapter

import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.model.DiscountGameItem
import demo.lizl.com.psnine.util.GlideUtil
import kotlinx.android.synthetic.main.item_discount_game.view.*

class DiscountGameListAdapter : BaseAdapter<DiscountGameItem, DiscountGameListAdapter.ViewHolder>()
{

    private var onDiscountGameItemClickListener: OnDiscountGameItemClickListener? = null

    override fun createCustomViewHolder(parent: ViewGroup, position: Int): ViewHolder
    {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_discount_game, parent, false))
    }

    override fun bindCustomViewHolder(holder: ViewHolder, discountGameItem: DiscountGameItem, position: Int)
    {
        holder.onBindViewHolder(discountGameItem)
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView)
    {
        fun onBindViewHolder(discountGameItem: DiscountGameItem)
        {
            GlideUtil.displayImage(context, discountGameItem.gameCoverUrl, itemView.iv_game_cover)
            itemView.tv_discount_rate.text = discountGameItem.discountRate
            itemView.tv_game_name.text = discountGameItem.gameName
            itemView.tv_platform.text = discountGameItem.gamePlatform
            itemView.tv_discount_time.text = discountGameItem.discountTime
            itemView.tv_original_price.text = discountGameItem.originalPrice
            itemView.tv_not_member_price.text = discountGameItem.notMemberPrice
            itemView.tv_member_price.text = discountGameItem.memberPrice
            itemView.tv_is_lowest.visibility = if (discountGameItem.isLowest) View.VISIBLE else View.GONE

            itemView.tv_original_price.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG

            itemView.tv_platform.setBackgroundColor(
                    when (discountGameItem.gamePlatform)
                    {
                        "PS3" -> ContextCompat.getColor(context, R.color.color_bg_label_PS3_game)
                        "PSV" -> ContextCompat.getColor(context, R.color.color_bg_label_PSV_game)
                        "PS4" -> ContextCompat.getColor(context, R.color.color_bg_label_PS4_game)
                        else -> ContextCompat.getColor(context, R.color.color_bg_label_PS4_game)
                    }
            )

            itemView.setOnClickListener { onDiscountGameItemClickListener?.onDiscountGameItemClick(discountGameItem) }
        }
    }

    interface OnDiscountGameItemClickListener
    {
        fun onDiscountGameItemClick(discountGameItem: DiscountGameItem)
    }

    fun setOnDiscountGameItemClickListener(onDiscountGameItemClickListener: OnDiscountGameItemClickListener)
    {
        this.onDiscountGameItemClickListener = onDiscountGameItemClickListener
    }
}