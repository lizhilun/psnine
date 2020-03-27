package demo.lizl.com.psnine.adapter

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.bean.ReplyPostItem
import demo.lizl.com.psnine.util.GlideUtil
import kotlinx.android.synthetic.main.item_reply_post.view.*

class ReplyPostListAdapter() : BaseQuickAdapter<ReplyPostItem, ReplyPostListAdapter.ViewHolder>(R.layout.item_reply_post)
{

    override fun convert(helper: ViewHolder, item: ReplyPostItem)
    {
        helper.bindViewHolder(item)
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView)
    {
        fun bindViewHolder(postItem: ReplyPostItem)
        {
            GlideUtil.displayImage(context, postItem.imageUrl, itemView.iv_post_icon)
            itemView.tv_post_title.text = postItem.postContent
            itemView.tv_post_writer.text = postItem.postWriterId
            itemView.tv_post_time.text = postItem.postTime

            if (postItem.subReplyPostList.size > 0)
            {
                val subReplyListAdapter = SubReplyListAdapter(postItem.subReplyPostList)
                itemView.rv_sub_reply_poet_list.layoutManager = LinearLayoutManager(context)
                itemView.rv_sub_reply_poet_list.adapter = subReplyListAdapter
            }
        }
    }
}