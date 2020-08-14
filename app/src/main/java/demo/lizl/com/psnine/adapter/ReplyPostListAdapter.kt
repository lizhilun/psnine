package demo.lizl.com.psnine.adapter

import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.model.ReplyPostModel
import demo.lizl.com.psnine.databinding.ItemReplyPostBinding
import demo.lizl.com.psnine.mvvm.activity.UserDetailActivity
import demo.lizl.com.psnine.util.ActivityUtil

class ReplyPostListAdapter : BaseQuickAdapter<ReplyPostModel, BaseViewHolder>(R.layout.item_reply_post)
{

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int)
    {
        DataBindingUtil.bind<ItemReplyPostBinding>(viewHolder.itemView)
    }

    override fun convert(helper: BaseViewHolder, model: ReplyPostModel)
    {
        DataBindingUtil.getBinding<ItemReplyPostBinding>(helper.itemView)?.apply {
            replyPostItem = model
            subReplyAdapter = SubReplyListAdapter(model.subReplyPostList)
            executePendingBindings()
            ivPostIcon.setOnClickListener { ActivityUtil.turnToActivity(UserDetailActivity::class.java, model.postWriterId) }
        }
    }
}