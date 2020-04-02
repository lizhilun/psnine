package demo.lizl.com.psnine.adapter

import android.graphics.Paint
import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.bean.DiscountGameItem
import demo.lizl.com.psnine.custom.other.CustomDiffUtil
import demo.lizl.com.psnine.databinding.ItemDiscountGameBinding

class DiscountGameListAdapter : BaseQuickAdapter<DiscountGameItem, BaseViewHolder>(R.layout.item_discount_game)
{

    private var onDiscountGameItemClickListener: ((DiscountGameItem) -> Unit)? = null

    init
    {
        setDiffCallback(CustomDiffUtil({ oldItem, newItem -> oldItem.psnGameId == newItem.psnGameId }, { oldItem, newItem -> oldItem == newItem }))
    }

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int)
    {
        DataBindingUtil.bind<ItemDiscountGameBinding>(viewHolder.itemView)
    }

    override fun convert(helper: BaseViewHolder, item: DiscountGameItem)
    {
        helper.getBinding<ItemDiscountGameBinding>()?.apply {
            discountItem = item
            executePendingBindings()
            tvOriginalPrice.paint?.flags = Paint.STRIKE_THRU_TEXT_FLAG
            root.setOnClickListener { onDiscountGameItemClickListener?.invoke(item) }
        }
    }

    fun setOnDiscountGameItemClickListener(onDiscountGameItemClickListener: (discountGameItem: DiscountGameItem) -> Unit)
    {
        this.onDiscountGameItemClickListener = onDiscountGameItemClickListener
    }
}