package demo.lizl.com.psnine.custom.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.isVisible
import demo.lizl.com.psnine.R
import kotlinx.android.synthetic.main.layout_base_dialog.*

open class BaseDialog(context: Context, private val layoutResId: Int, private val hasBottomButton: Boolean = false) : Dialog(context, R.style.GlobalDialogStyle)
{
    protected val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.layout_base_dialog, null)
        setContentView(view)

        val contentView = layoutInflater.inflate(layoutResId, null)
        fl_content_view.addView(contentView)

        group_bottom_view.isVisible = hasBottomButton
        tv_title.text = getTitleText()

        tv_confirm.setOnClickListener {
            dismiss()
            onConfirmButtonClick()
        }

        tv_cancel.setOnClickListener { dismiss() }

        initView()
    }

    override fun onStart()
    {
        super.onStart()

        // 设置Dialog宽度
        val params = window?.attributes ?: return
        val display = context.resources.displayMetrics
        val min = display.heightPixels.coerceAtMost(display.widthPixels)
        params.width = (min * 0.8).toInt()
        window?.attributes = params
    }

    open fun initView()
    {

    }

    open fun getTitleText() = ""

    open fun onConfirmButtonClick()
    {

    }
}