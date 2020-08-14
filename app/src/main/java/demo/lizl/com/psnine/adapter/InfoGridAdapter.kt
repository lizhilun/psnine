package demo.lizl.com.psnine.adapter

import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.model.InfoModel
import demo.lizl.com.psnine.databinding.ItemInfoBinding

class InfoGridAdapter(infoList: MutableList<InfoModel>) : BaseQuickAdapter<InfoModel, BaseViewHolder>(R.layout.item_info, infoList)
{
    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int)
    {
        DataBindingUtil.bind<ItemInfoBinding>(viewHolder.itemView)
    }

    override fun convert(helper: BaseViewHolder, model: InfoModel)
    {
        DataBindingUtil.getBinding<ItemInfoBinding>(helper.itemView)?.apply {
            infoItem = model
            executePendingBindings()
        }
    }
}