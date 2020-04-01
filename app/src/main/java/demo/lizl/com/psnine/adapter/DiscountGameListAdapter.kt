package demo.lizl.com.psnine.adapter

import android.graphics.Paint
import android.view.View
import androidx.core.view.isVisible
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.bean.DiscountGameItem
import demo.lizl.com.psnine.custom.other.CustomDiffUtil
import demo.lizl.com.psnine.util.GameUtil
import demo.lizl.com.psnine.util.GlideUtil
import kotlinx.android.synthetic.main.item_discount_game.view.*

class DiscountGameListAdapter : BaseQuickAdapter<DiscountGameItem, DiscountGameListAdapter.ViewHolder>(R.layout.item_discount_game)
{

    private var onDiscountGameItemClickListener: ((DiscountGameItem) -> Unit)? = null

    init
    {
        setDiffCallback(CustomDiffUtil({ oldItem, newItem -> oldItem.psnGameId == newItem.psnGameId }, { oldItem, newItem -> oldItem == newItem }))
    }

    override fun convert(helper: ViewHolder, item: DiscountGameItem)
    {
        helper.bindViewHolder(item)
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView)
    {
        fun bindViewHolder(discountGameItem: DiscountGameItem)
        {
            GlideUtil.displayImage(itemView.iv_game_cover, discountGameItem.gameCoverUrl)

            itemView.tv_discount_rate.text = discountGameItem.discountRate
            itemView.tv_game_name.text = discountGameItem.gameName
            itemView.tv_platform.text = discountGameItem.gamePlatform
            itemView.tv_discount_time.text = discountGameItem.discountTime
            itemView.tv_original_price.text = discountGameItem.originalPrice
            itemView.tv_not_member_price.text = discountGameItem.notMemberPrice
            itemView.tv_member_price.text = discountGameItem.memberPrice
            itemView.tv_is_lowest.isVisible = discountGameItem.isLowest
            itemView.tv_original_price.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
            itemView.tv_platform.setBackgroundColor(GameUtil.getPlatformColor(discountGameItem.gamePlatform))

            itemView.setOnClickListener { onDiscountGameItemClickListener?.invoke(discountGameItem) }
        }
    }

    fun setOnDiscountGameItemClickListener(onDiscountGameItemClickListener: (discountGameItem: DiscountGameItem) -> Unit)
    {
        this.onDiscountGameItemClickListener = onDiscountGameItemClickListener
    }
}