package demo.lizl.com.psnine.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.BackgroundColorSpan
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.UiApplication
import demo.lizl.com.psnine.customview.CustomLinkMovementMethod
import demo.lizl.com.psnine.bean.ReplyPostItem
import kotlinx.android.synthetic.main.item_sub_reply_post_item.view.*
import java.util.*

class SubReplyListAdapter(val context: Context, private val postList: List<ReplyPostItem>) : RecyclerView.Adapter<SubReplyListAdapter.ViewHolder>()
{

    private var layoutInflater: LayoutInflater? = null

    companion object
    {
        val writerIdText = ContextCompat.getColor(UiApplication.instance, R.color.color_sub_reply_post_writer_id_text)
        val writerIdTextBg = ContextCompat.getColor(UiApplication.instance, R.color.color_sub_reply_post_writer_id_bg)
        val atIdText = ContextCompat.getColor(UiApplication.instance, R.color.color_sub_reply_post_at_user_id_text)
    }

    init
    {
        layoutInflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(layoutInflater!!.inflate(R.layout.item_sub_reply_post_item, parent, false))
    }

    override fun getItemCount() = postList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.onBindViewHolder(postList[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        fun onBindViewHolder(postItem: ReplyPostItem)
        {
            val postContent = String.format(Locale.getDefault(), " %s  %s %s", postItem.postWriterId, postItem.atUserId, postItem.postContent)

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