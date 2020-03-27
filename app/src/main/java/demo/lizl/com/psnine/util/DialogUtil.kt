package demo.lizl.com.psnine.util

import android.app.Dialog
import android.content.Context
import demo.lizl.com.psnine.custom.dialog.DialogGameSortCondition
import demo.lizl.com.psnine.custom.dialog.DialogLoading
import demo.lizl.com.psnine.custom.dialog.DialogOperationConfirm

object DialogUtil
{
    private var dialog: Dialog? = null

    fun showOperationConfirmDialog(context: Context, title: String, notify: String, onConfirmBtnClickListener: () -> Unit)
    {
        dialog?.dismiss()
        dialog = DialogOperationConfirm(context, title, notify, onConfirmBtnClickListener)
        dialog?.show()
    }

    fun showLoadingDialog(context: Context)
    {
        dialog?.dismiss()
        dialog = DialogLoading(context)
        dialog?.show()
    }

    fun showGameSortConditionDialog(context: Context, onConfirmButtonClickListener: (String, String) -> Unit)
    {
        dialog?.dismiss()
        dialog = DialogGameSortCondition(context, onConfirmButtonClickListener)
        dialog?.show()
    }

    fun dismissDialog()
    {
        dialog?.dismiss()
    }
}