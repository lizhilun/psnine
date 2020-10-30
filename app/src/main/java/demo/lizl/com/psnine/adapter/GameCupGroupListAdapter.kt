package demo.lizl.com.psnine.adapter

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.custom.other.CustomDiffUtil
import demo.lizl.com.psnine.databinding.ItemGameCupGroupBinding
import demo.lizl.com.psnine.model.GameCupGroupModel

class GameCupGroupListAdapter : BaseQuickAdapter<GameCupGroupModel, BaseViewHolder>(R.layout.item_game_cup_group)
{
    companion object
    {
        private const val KEY_LIST_CHANGED = "KEY_LIST_CHANGED"
    }

    init
    {
        setDiffCallback(CustomDiffUtil({ oldItem, newItem -> oldItem.gameCupCoverUrl == newItem.gameCupCoverUrl }, { oldItem, newItem -> oldItem == newItem },
                { oldItem, newItem ->
                    val bundle = Bundle()
                    if (oldItem.gameCupList != newItem.gameCupList)
                    {
                        bundle.putBoolean(KEY_LIST_CHANGED, true)
                    }
                    bundle
                }))
    }

    override fun convert(helper: BaseViewHolder, item: GameCupGroupModel)
    {
        helper.getBinding<ItemGameCupGroupBinding>()?.apply {
            gameCupGroupItem = item
            gameCupListAdapter = GameCupListAdapter(item.gameCupList)
            executePendingBindings()
        }
    }

    override fun convert(helper: BaseViewHolder, item: GameCupGroupModel, payloads: List<Any>)
    {
        if (payloads.isEmpty())
        {
            convert(helper, item)
            return
        }
        val bundle = payloads.first()
        if (bundle is Bundle)
        {
            val listChanged = bundle.getBoolean(KEY_LIST_CHANGED, false)
            if (listChanged)
            {
                helper.getBinding<ItemGameCupGroupBinding>()?.apply {
                    if (rvGameCupList.adapter is GameCupListAdapter)
                    {
                        (rvGameCupList.adapter as GameCupListAdapter).setDiffNewData(item.gameCupList.toMutableList())
                    }
                }
            }
        }
    }

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int)
    {
        DataBindingUtil.bind<ItemGameCupGroupBinding>(viewHolder.itemView)
    }
}