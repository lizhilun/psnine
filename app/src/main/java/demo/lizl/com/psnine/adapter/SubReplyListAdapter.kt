package demo.lizl.com.psnine.adapter

import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.BackgroundColorSpan
import android.text.style.ClickableSpan
import android.view.View
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.UiApplication
import demo.lizl.com.psnine.bean.ReplyPostItem
import demo.lizl.com.psnine.custom.other.CustomLinkMovementMethod
import kotlinx.android.synthetic.main.item_sub_reply_post_item.view.*

class SubReplyListAdapter(postList: List<ReplyPostItem>) :
        BaseQuickAdapter<ReplyPostItem, SubReplyListAdapter.ViewHolder>(R.layout.item_sub_reply_post_item, postList.toMutableList())
{

    companion object
    {
        val writerIdText = ContextCompat.getColor(UiApplication.instance, R.color.color_sub_reply_post_writer_id_text)
        val writerIdTextBg = ContextCompat.getColor(UiApplication.instance, R.color.color_sub_reply_post_writer_id_bg)
        val atIdText = ContextCompat.getColor(UiApplication.instance, R.color.color_sub_reply_post_at_user_id_text)
    }

    override fun convert(helper: ViewHolder, item: ReplyPostItem)
    {
        helper.bindViewHolder(item)
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView)
    {
        fun bindViewHolder(postItem: ReplyPostItem)
        {
            val postContent = " ${postItem.postWriterId}  ${postItem.atUserId} ${postItem.postContent}"

            val spannable = SpannableString(postContent)

            spannable.setSpan(object : BackgroundColorSpan(writerIdTextBg)
            {}, 0, postItem.postWriterId.length + 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)

            spannable.setSpan(object : WriterIDSpan()
            {
                override fun onClick(p0: View)
                {

                }
            }, 0, postItem.postWriterId.length + 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)


            spannable.setSpan(object : AtUserIDSpan()
            {
                override fun onClick(p0: View)
                {

                }
            }, postItem.postWriterId.length + 3, postItem.postWriterId.length + 3 + postItem.atUserId!!.length + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)

            itemView.tv_replay_content.text = spannable

            itemView.tv_replay_content.setOnTouchListener(CustomLinkMovementMethod.instance)
        }
    }

    abstract class WriterIDSpan : ClickableSpan()
    {
        override fun updateDrawState(ds: TextPaint)
        {
            ds.color = writerIdText
        }
    }

    abstract class AtUserIDSpan : ClickableSpan()
    {
        override fun updateDrawState(ds: TextPaint)
        {
            ds.color = atIdText
        }
    }
}