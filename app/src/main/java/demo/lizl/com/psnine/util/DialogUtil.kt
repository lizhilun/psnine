package demo.lizl.com.psnine.util

import android.app.Dialog
import android.content.Context
import demo.lizl.com.psnine.custom.dialog.DialogGameSortCondition
import demo.lizl.com.psnine.custom.dialog.DialogLoading
import demo.lizl.com.psnine.custom.dialog.DialogOperationConfirm
import demo.lizl.com.psnine.custom.function.ui
import kotlinx.coroutines.GlobalScope

object DialogUtil
{
    private var dialog: Dialog? = null

    fun showOperationConfirmDialog(context: Context, title: String, notify: String, onConfirmBtnClickListener: () -> Unit)
    {
        GlobalScope.ui { showDialog(DialogOperationConfirm(context, title, notify, onConfirmBtnClickListener)) }
    }

    fun showLoadingDialog(context: Context)
    {
        GlobalScope.ui { showDialog(DialogLoading(context)) }
    }

    fun showGameSortConditionDialog(context: Context, onConfirmButtonClickListener: (String, String) -> Unit)
    {
        GlobalScope.ui { showDialog(DialogGameSortCondition(context, onConfirmButtonClickListener)) }
    }

    private fun showDialog(dialog: Dialog)
    {
        this.dialog?.dismiss()
        this.dialog = dialog
        this.dialog?.show()
    }

    fun dismissDialog()
    {
        dialog?.dismiss()
    }
}