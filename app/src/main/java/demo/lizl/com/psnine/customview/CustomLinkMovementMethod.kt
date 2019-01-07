package demo.lizl.com.psnine.customview

import android.text.Spannable
import android.text.style.ClickableSpan
import android.view.MotionEvent
import android.view.View
import android.widget.TextView

class CustomLinkMovementMethod : View.OnTouchListener
{
    companion object
    {
        val instance: CustomLinkMovementMethod by lazy { CustomLinkMovementMethod() }
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean
    {
        var ret = false
        val text = (v as TextView).text
        val stext = Spannable.Factory.getInstance().newSpannable(text);
        val widget = v
        val action = event.getAction()

        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN)
        {
            var x = event.x
            var y = event.y

            x -= widget.totalPaddingLeft
            y -= widget.totalPaddingTop

            x += widget.getScrollX()
            y += widget.getScrollY()

            val layout = widget.layout
            val line = layout.getLineForVertical(y.toInt())
            val off = layout.getOffsetForHorizontal(line, x)

            val link = stext.getSpans(off, off, ClickableSpan::class.java)

            if (link.isNotEmpty())
            {
                if (action == MotionEvent.ACTION_UP)
                {
                    link[0].onClick(widget)
                }
                ret = true
            }
        }
        return ret
    }
}