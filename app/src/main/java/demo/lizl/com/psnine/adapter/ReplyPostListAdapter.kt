package demo.lizl.com.psnine.adapter

import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.bean.ReplyPostItem
import demo.lizl.com.psnine.databinding.ItemReplyPostBinding

class ReplyPostListAdapter() : BaseQuickAdapter<ReplyPostItem, BaseViewHolder>(R.layout.item_reply_post)
{

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int)
    {
        DataBindingUtil.bind<ItemReplyPostBinding>(viewHolder.itemView)
    }

    override fun convert(helper: BaseViewHolder, item: ReplyPostItem)
    {
        DataBindingUtil.getBinding<ItemReplyPostBinding>(helper.itemView)?.apply {
            replyPostItem = item
            subReplyAdapter = SubReplyListAdapter(item.subReplyPostList)
            executePendingBindings()
        }
    }
}