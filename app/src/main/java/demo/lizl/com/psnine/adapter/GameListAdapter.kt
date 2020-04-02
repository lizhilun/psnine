package demo.lizl.com.psnine.adapter

import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.bean.GameInfoItem
import demo.lizl.com.psnine.custom.other.CustomDiffUtil
import demo.lizl.com.psnine.databinding.ItemGameBinding

class GameListAdapter : BaseQuickAdapter<GameInfoItem, BaseViewHolder>(R.layout.item_game)
{
    private var gameItemClickListener: ((GameInfoItem) -> Unit)? = null

    init
    {
        setDiffCallback(CustomDiffUtil({ oldItem, newItem -> oldItem.gameDetailUrl == newItem.gameDetailUrl }, { oldItem, newItem -> oldItem == newItem }))
    }

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int)
    {
        DataBindingUtil.bind<ItemGameBinding>(viewHolder.itemView)
    }

    override fun convert(helper: BaseViewHolder, item: GameInfoItem)
    {
        DataBindingUtil.getBinding<ItemGameBinding>(helper.itemView)?.apply {
            gameInfoItem = item
            root.setOnClickListener { gameItemClickListener?.invoke(item) }
            executePendingBindings()
        }
    }

    fun setGameItemClickListener(gameItemClickListener: (GameInfoItem) -> Unit)
    {
        this.gameItemClickListener = gameItemClickListener
    }

}