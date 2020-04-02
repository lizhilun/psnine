package demo.lizl.com.psnine.adapter

import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.bean.PostItem
import demo.lizl.com.psnine.custom.other.CustomDiffUtil
import demo.lizl.com.psnine.databinding.ItemPostBinding

class PostListAdapter : BaseQuickAdapter<PostItem, BaseViewHolder>(R.layout.item_post), LoadMoreModule
{

    private var onPostItemClickListener: ((PostItem) -> Unit)? = null
    private var onPostAvatarClickListener: ((PostItem) -> Unit)? = null

    init
    {
        setDiffCallback(CustomDiffUtil({ oldItem, newItem -> oldItem.postDetailUrl == newItem.postDetailUrl }, { oldItem, newItem -> oldItem == newItem }))
    }

    override fun convert(helper: BaseViewHolder, item: PostItem)
    {
        DataBindingUtil.getBinding<ItemPostBinding>(helper.itemView)?.apply {
            postItem = item
            ivPostIcon.setOnClickListener { onPostAvatarClickListener?.invoke(item) }
            root.setOnClickListener { onPostItemClickListener?.invoke(item) }
            executePendingBindings()
        }
    }

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int)
    {
        DataBindingUtil.bind<ItemPostBinding>(viewHolder.itemView)
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