package demo.lizl.com.psnine.adapter

import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.bean.GameCupGroupItem
import demo.lizl.com.psnine.databinding.ItemGameCupGroupBinding

class GameCupGroupListAdapter : BaseQuickAdapter<GameCupGroupItem, BaseViewHolder>(R.layout.item_game_cup_group)
{
    override fun convert(helper: BaseViewHolder, item: GameCupGroupItem)
    {
        helper.getBinding<ItemGameCupGroupBinding>()?.apply {
            gameCupGroupItem = item
            gameCupListAdapter = GameCupListAdapter(item.gameCupList)
            executePendingBindings()
        }
    }

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int)
    {
        DataBindingUtil.bind<ItemGameCupGroupBinding>(viewHolder.itemView)
    }
}