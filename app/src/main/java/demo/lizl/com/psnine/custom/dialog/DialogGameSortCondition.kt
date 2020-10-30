package demo.lizl.com.psnine.custom.dialog

import android.content.Context
import demo.lizl.com.psnine.R
import kotlinx.android.synthetic.main.dialog_game_sort_condition.*

class DialogGameSortCondition(context: Context, private val onConfirmButtonClickListener: (String, String) -> Unit) :
    BaseDialog(context, R.layout.dialog_game_sort_condition, true)
{
    override fun getTitleText() = context.getString(R.string.sort_condition)

    override fun initView()
    {
        val platformTypeList = context.resources.getStringArray(R.array.game_platform_list).asList()
        sp_game_platform.setBackgroundResource(R.drawable.shape_spinner)
        sp_game_platform.attachDataSource(platformTypeList)

        val sortConditionList = context.resources.getStringArray(R.array.game_sort_condition).asList()
        sp_sort_condition.setBackgroundResource(R.drawable.shape_spinner)
        sp_sort_condition.attachDataSource(sortConditionList)
    }

    override fun onConfirmButtonClick()
    {
        onConfirmButtonClickListener.invoke(sp_game_platform.text.toString(), sp_sort_condition.text.toString())
    }
}