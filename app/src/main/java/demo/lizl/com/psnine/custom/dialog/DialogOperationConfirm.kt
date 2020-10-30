package demo.lizl.com.psnine.custom.dialog

import android.content.Context
import demo.lizl.com.psnine.R
import kotlinx.android.synthetic.main.dialog_operation_confirm.*

/**
 * 用于操作确认的Dialog
 */
class DialogOperationConfirm(context: Context, private val title: String, private val notify: String, private val onConfirmButtonClickListener: () -> Unit) :
    BaseDialog(context, R.layout.dialog_operation_confirm, true)
{
    override fun getTitleText() = title

    override fun initView()
    {
        tv_notify.text = notify
    }

    override fun onConfirmButtonClick()
    {
        onConfirmButtonClickListener.invoke()
    }
}