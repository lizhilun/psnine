package demo.lizl.com.psnine.adapter

import android.graphics.Paint
import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.model.DiscountGameModel
import demo.lizl.com.psnine.custom.other.CustomDiffUtil
import demo.lizl.com.psnine.databinding.ItemDiscountGameBinding

class DiscountGameListAdapter : BaseQuickAdapter<DiscountGameModel, BaseViewHolder>(R.layout.item_discount_game)
{
    init
    {
        setDiffCallback(CustomDiffUtil({ oldItem, newItem -> oldItem.psnGameId == newItem.psnGameId }, { oldItem, newItem -> oldItem == newItem }))
    }

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int)
    {
        DataBindingUtil.bind<ItemDiscountGameBinding>(viewHolder.itemView)
    }

    override fun convert(helper: BaseViewHolder, model: DiscountGameModel)
    {
        helper.getBinding<ItemDiscountGameBinding>()?.apply {
            discountItem = model
            executePendingBindings()
            tvOriginalPrice.paint?.flags = Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    fun setOnDiscountGameItemClickListener(onDiscountGameItemClickListener: (discountGameModel: DiscountGameModel) -> Unit)
    {
        setOnItemClickListener { _, _, position ->
            val model = getItemOrNull(position) ?: return@setOnItemClickListener
            onDiscountGameItemClickListener.invoke(model)
        }
    }
}