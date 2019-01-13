package demo.lizl.com.psnine.customview.dialog

import android.content.Context
import demo.lizl.com.psnine.R
import kotlinx.android.synthetic.main.dialog_loading.*

class DialogLoading(context: Context) : BaseDialog(context, context.getString(R.string.in_loading), false)
{
    override fun getDialogContentViewResId(): Int
    {
        return R.layout.dialog_loading
    }

    override fun initView()
    {
        setCanceledOnTouchOutside(false)
        loading_view.smoothToShow()
    }

    override fun onConfirmButtonClick()
    {

    }

}