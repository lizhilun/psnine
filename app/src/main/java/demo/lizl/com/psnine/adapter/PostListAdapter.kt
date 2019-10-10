package demo.lizl.com.psnine.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.bean.PostItem
import demo.lizl.com.psnine.util.GlideUtil
import kotlinx.android.synthetic.main.item_post.view.*

class PostListAdapter : BaseAdapter<PostItem, PostListAdapter.ViewHolder>()
{

    private var onPostItemClickListener: ((PostItem) -> Unit)? = null
    private var onPostAvatarClickListener: ((PostItem) -> Unit)? = null

    override fun createCustomViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_post, parent, false))
    }

    override fun bindCustomViewHolder(holder: ViewHolder, postItem: PostItem, position: Int)
    {
        holder.onBindViewHolder(postItem)
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView)
    {
        fun onBindViewHolder(postItem: PostItem)
        {
            GlideUtil.displayImage(getContext(), postItem.imageUrl, itemView.iv_post_icon)
            itemView.tv_post_title.text = postItem.postTitle
            itemView.tv_post_writer.text = postItem.postWriterId
            itemView.tv_last_update_time.text = postItem.lateUpdateTime

            itemView.iv_post_icon.setOnClickListener { onPostAvatarClickListener?.invoke(postItem) }
            itemView.setOnClickListener { onPostItemClickListener?.invoke(postItem) }
        }
    }

    fun setOnPostItemClickListener(onPostItemClickListener: ((PostItem) -> Unit))
    {
        this.onPostItemClickListener = onPostItemClickListener;
    }

    fun setOnPostAvatarClickListener(onPostAvatarClickListener: ((PostItem) -> Unit))
    {
        this.onPostAvatarClickListener = onPostAvatarClickListener
    }
}