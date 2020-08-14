package demo.lizl.com.psnine.adapter

import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.model.PostModel
import demo.lizl.com.psnine.custom.other.CustomDiffUtil
import demo.lizl.com.psnine.databinding.ItemPostBinding

class PostListAdapter : BaseQuickAdapter<PostModel, BaseViewHolder>(R.layout.item_post), LoadMoreModule
{

    private var onPostAvatarClickListener: ((PostModel) -> Unit)? = null

    init
    {
        setDiffCallback(CustomDiffUtil({ oldItem, newItem -> oldItem.postDetailUrl == newItem.postDetailUrl }, { oldItem, newItem -> oldItem == newItem }))
    }

    override fun convert(helper: BaseViewHolder, model: PostModel)
    {
        DataBindingUtil.getBinding<ItemPostBinding>(helper.itemView)?.apply {
            postItem = model
            ivPostIcon.setOnClickListener { onPostAvatarClickListener?.invoke(model) }
            executePendingBindings()
        }
    }

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int)
    {
        DataBindingUtil.bind<ItemPostBinding>(viewHolder.itemView)
    }

    fun setOnPostItemClickListener(onPostItemClickListener: ((PostModel) -> Unit))
    {
        setOnItemClickListener { _, _, position ->
            val model = getItemOrNull(position) ?: return@setOnItemClickListener
            onPostItemClickListener.invoke(model)
        }
    }

    fun setOnPostAvatarClickListener(onPostAvatarClickListener: ((PostModel) -> Unit))
    {
        this.onPostAvatarClickListener = onPostAvatarClickListener
    }
}