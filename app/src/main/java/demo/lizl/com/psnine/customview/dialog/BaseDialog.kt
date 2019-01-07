package demo.lizl.com.psnine.customview.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import demo.lizl.com.psnine.R
import kotlinx.android.synthetic.main.layout_base_dialog.*


abstract class BaseDialog(context: Context, val title: String) : Dialog(context, R.style.GlobalDialogStyle)
{
    protected val TAG = javaClass.simpleName

    private var onConfirmButtonClickListener: OnConfirmButtonClickListener? = null
    private var onCancelButtonClickListener: OnCancelButtonClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.layout_base_dialog, null)
        setContentView(view)

        val contentView = layoutInflater.inflate(getDialogContentViewResId(), null)
        fl_content_view.addView(contentView)

        tv_title.text = title

        tv_confirm.setOnClickListener {
            dismiss()
            onConfirmButtonClick()
            onConfirmButtonClickListener?.onConfirmButtonClick()
        }

        tv_cancel.setOnClickListener {
            dismiss()
            onCancelButtonClickListener?.onCancelButtonClick()
        }

        initView()
    }

    override fun onStart()
    {
        super.onStart()

        // 设置Dialog宽度
        val params = window.attributes
        val display = context.resources.displayMetrics
        var min = display.heightPixels
        // 宽度设置为宽高最小值的80%（兼容横屏）
        if (min > display.widthPixels) min = display.widthPixels
        params.width = (min * 0.8).toInt()
        window.attributes = params
    }

    abstract fun getDialogContentViewResId(): Int

    abstract fun initView()

    abstract fun onConfirmButtonClick()

    interface OnConfirmButtonClickListener
    {
        fun onConfirmButtonClick()
    }

    interface OnCancelButtonClickListener
    {
        fun onCancelButtonClick()
    }

    fun setOnConfirmButtonClickListener(onConfirmButtonClickListener: OnConfirmButtonClickListener)
    {
        this.onConfirmButtonClickListener = onConfirmButtonClickListener
    }

    fun setOnCancelButtonClickListener(onCancelButtonClickListener: OnCancelButtonClickListener)
    {
        this.onCancelButtonClickListener = onCancelButtonClickListener
    }
}