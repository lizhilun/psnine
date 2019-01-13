package demo.lizl.com.psnine.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.model.PostItem
import demo.lizl.com.psnine.util.GlideUtil
import kotlinx.android.synthetic.main.item_post.view.*

class PostListAdapter: BaseAdapter<PostItem, PostListAdapter.ViewHolder>()
{

    private var onPostItemClickListener: OnPostItemClickListener? = null
    private var onPostAvatarClickListener: OnPostAvatarClickListener? = null

    override fun createCustomViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_post, parent, false))
    }

    override fun bindCustomViewHolder(holder: ViewHolder, postItem: PostItem, position: Int)
    {
        holder.onBindViewHolder(postItem)
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView)
    {
        fun onBindViewHolder(postItem: PostItem)
        {
            GlideUtil.displayImage(context, postItem.imageUrl, itemView.iv_post_icon)
            itemView.tv_post_title.text = postItem.postTitle
            itemView.tv_post_writer.text = postItem.postWriterId
            itemView.tv_last_update_time.text = postItem.lateUpdateTime

            itemView.iv_post_icon.setOnClickListener { onPostAvatarClickListener?.onPostAvatarClick(postItem) }
            itemView.setOnClickListener { onPostItemClickListener?.onPostItemClick(postItem) }
        }
    }

    interface OnPostItemClickListener
    {
        fun onPostItemClick(postItem: PostItem)
    }

    interface OnPostAvatarClickListener
    {
        fun onPostAvatarClick(postItem: PostItem)
    }

    fun setOnPostItemClickListener(onPostItemClickListener: OnPostItemClickListener)
    {
        this.onPostItemClickListener = onPostItemClickListener;
    }

    fun setOnPostAvatarClickListener(onPostAvatarClickListener: OnPostAvatarClickListener)
    {
        this.onPostAvatarClickListener = onPostAvatarClickListener
    }
}