package demo.lizl.com.psnine.adapter

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.bean.ReplyPostItem
import demo.lizl.com.psnine.util.GlideUtil
import kotlinx.android.synthetic.main.item_reply_post.view.*

class ReplyPostListAdapter(val context: Context, private val postList: List<ReplyPostItem>) : RecyclerView.Adapter<ReplyPostListAdapter.ViewHolder>()
{

    private var layoutInflater: LayoutInflater? = null

    init
    {
        layoutInflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(layoutInflater!!.inflate(R.layout.item_reply_post, parent, false))
    }

    override fun getItemCount() = postList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.onBindViewHolder(postList[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        fun onBindViewHolder(postItem: ReplyPostItem)
        {
            GlideUtil.displayImage(context, postItem.imageUrl, itemView.iv_post_icon)
            itemView.tv_post_title.text = postItem.postContent
            itemView.tv_post_writer.text = postItem.postWriterId
            itemView.tv_post_time.text = postItem.postTime

            if (postItem.subReplyPostList.size > 0)
            {
                val subReplyListAdapter = SubReplyListAdapter(context, postItem.subReplyPostList)
                itemView.rv_sub_reply_poet_list.layoutManager = LinearLayoutManager(context)
                itemView.rv_sub_reply_poet_list.adapter = subReplyListAdapter
            }
        }
    }
}