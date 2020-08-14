package demo.lizl.com.psnine.adapter

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.model.GameCupGroupModel
import demo.lizl.com.psnine.custom.other.CustomDiffUtil
import demo.lizl.com.psnine.databinding.ItemGameCupGroupBinding

class GameCupGroupListAdapter : BaseQuickAdapter<GameCupGroupModel, BaseViewHolder>(R.layout.item_game_cup_group)
{
    companion object
    {
        private const val KEY_LIST_CHANGED = "KEY_LIST_CHANGED"
    }

    override fun convert(helper: BaseViewHolder, model: GameCupGroupModel)
    {
        helper.getBinding<ItemGameCupGroupBinding>()?.apply {
            gameCupGroupItem = model
            gameCupListAdapter = GameCupListAdapter(model.gameCupList)
            executePendingBindings()
        }
    }

    override fun convert(helper: BaseViewHolder, model: GameCupGroupModel, payloads: List<Any>)
    {
        if (payloads.isEmpty())
        {
            convert(helper, model)
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
                        (rvGameCupList.adapter as GameCupListAdapter).setDiffNewData(model.gameCupList.toMutableList())
                    }
                }
            }
        }
    }

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int)
    {
        DataBindingUtil.bind<ItemGameCupGroupBinding>(viewHolder.itemView)
    }

    init
    {
        setDiffCallback(CustomDiffUtil({ oldItem, newItem -> oldItem.gameCupCoverUrl == newItem.gameCupCoverUrl },
            { oldItem, newItem -> oldItem == newItem },
            { oldItem, newItem ->
                val bundle = Bundle()
                if (oldItem.gameCupList != newItem.gameCupList)
                {
                    bundle.putBoolean(KEY_LIST_CHANGED, true)
                }
                bundle
            })
        )
    }
}