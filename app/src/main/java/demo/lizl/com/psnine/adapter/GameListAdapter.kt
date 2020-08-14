package demo.lizl.com.psnine.adapter

import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.model.GameInfoModel
import demo.lizl.com.psnine.custom.other.CustomDiffUtil
import demo.lizl.com.psnine.databinding.ItemGameBinding

class GameListAdapter : BaseQuickAdapter<GameInfoModel, BaseViewHolder>(R.layout.item_game)
{
    init
    {
        setDiffCallback(CustomDiffUtil({ oldItem, newItem -> oldItem.gameDetailUrl == newItem.gameDetailUrl }, { oldItem, newItem -> oldItem == newItem }))
    }

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int)
    {
        DataBindingUtil.bind<ItemGameBinding>(viewHolder.itemView)
    }

    override fun convert(helper: BaseViewHolder, model: GameInfoModel)
    {
        DataBindingUtil.getBinding<ItemGameBinding>(helper.itemView)?.apply {
            gameInfoItem = model
            executePendingBindings()
        }
    }

    fun setGameItemClickListener(gameItemClickListener: (GameInfoModel) -> Unit)
    {
        setOnItemClickListener { _, _, position ->
            val model = getItemOrNull(position) ?: return@setOnItemClickListener
            gameItemClickListener.invoke(model)
        }
    }

}