package demo.lizl.com.psnine.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.model.PostItem
import demo.lizl.com.psnine.util.GlideUtil
import kotlinx.android.synthetic.main.item_post.view.*

class PostListAdapter(val context: Context, private val postList: List<PostItem>) : RecyclerView.Adapter<PostListAdapter.ViewHolder>()
{

    private var layoutInflater: LayoutInflater? = null

    private var onPostItemClickListener: OnPostItemClickListener? = null

    init
    {
        layoutInflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(layoutInflater!!.inflate(R.layout.item_post, parent, false))
    }

    override fun getItemCount() = postList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.onBindViewHolder(postList[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        fun onBindViewHolder(postItem: PostItem)
        {
            GlideUtil.displayImage(context, postItem.imageUrl, itemView.iv_post_icon)
            itemView.tv_post_title.text = postItem.postTitle
            itemView.tv_post_writer.text = postItem.postWriterId
            itemView.tv_last_update_time.text = postItem.lateUpdateTime

            itemView.setOnClickListener { onPostItemClickListener?.onPostItemClick(postItem) }
        }
    }

    interface OnPostItemClickListener
    {
        fun onPostItemClick(postItem: PostItem)
    }

    fun setOnPostItemClickListener(onPostItemClickListener: OnPostItemClickListener)
    {
        this.onPostItemClickListener = onPostItemClickListener;
    }
}