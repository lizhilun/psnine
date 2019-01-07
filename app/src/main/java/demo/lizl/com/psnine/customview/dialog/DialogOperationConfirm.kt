package demo.lizl.com.psnine.customview.dialog

import android.content.Context
import demo.lizl.com.psnine.R
import kotlinx.android.synthetic.main.dialog_operation_confirm.*

/**
 * 用于操作确认的Dialog
 */
class DialogOperationConfirm(context: Context, title: String, private val notify: String) : BaseDialog(context, title)
{

    override fun getDialogContentViewResId(): Int
    {
        return R.layout.dialog_operation_confirm
    }

    override fun initView()
    {
        tv_notify.text = notify
    }

    override fun onConfirmButtonClick()
    {

    }

}