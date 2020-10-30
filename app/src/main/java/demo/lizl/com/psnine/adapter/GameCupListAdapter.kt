package demo.lizl.com.psnine.adapter

import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.custom.other.CustomDiffUtil
import demo.lizl.com.psnine.databinding.ItemGameCupBinding
import demo.lizl.com.psnine.model.GameCupModel
import demo.lizl.com.psnine.mvvm.activity.CupTipsActivity
import demo.lizl.com.psnine.util.ActivityUtil

class GameCupListAdapter(gameCupList: List<GameCupModel>) : BaseQuickAdapter<GameCupModel, BaseViewHolder>(R.layout.item_game_cup, gameCupList.toMutableList())
{

    init
    {
        setDiffCallback(CustomDiffUtil({ oldItem, newItem -> oldItem.cupImageUrl == newItem.cupImageUrl }, { oldItem, newItem -> oldItem == newItem }))
    }

    override fun convert(helper: BaseViewHolder, item: GameCupModel)
    {
        DataBindingUtil.getBinding<ItemGameCupBinding>(helper.itemView)?.apply {
            gameCupItem = item
            root.setOnClickListener { ActivityUtil.turnToActivity(CupTipsActivity::class.java, item.cupTipsUrl) }
            executePendingBindings()
        }
    }

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int)
    {
        DataBindingUtil.bind<ItemGameCupBinding>(viewHolder.itemView)
    }
}