package demo.lizl.com.psnine.adapter

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.bean.PostItem
import demo.lizl.com.psnine.util.GlideUtil
import kotlinx.android.synthetic.main.item_post.view.*

class PostListAdapter : BaseQuickAdapter<PostItem, PostListAdapter.ViewHolder>(R.layout.item_post)
{

    private var onPostItemClickListener: ((PostItem) -> Unit)? = null
    private var onPostAvatarClickListener: ((PostItem) -> Unit)? = null

    override fun convert(helper: ViewHolder, item: PostItem)
    {
        helper.bindViewHolder(item)
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView)
    {
        fun bindViewHolder(postItem: PostItem)
        {
            GlideUtil.displayImage(context, postItem.imageUrl, itemView.iv_post_icon)
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