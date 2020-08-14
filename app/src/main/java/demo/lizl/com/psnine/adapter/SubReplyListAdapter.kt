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
import demo.lizl.com.psnine.model.ReplyPostModel
import demo.lizl.com.psnine.custom.function.deleteStr
import demo.lizl.com.psnine.custom.other.CustomLinkMovementMethod
import demo.lizl.com.psnine.mvvm.activity.UserDetailActivity
import demo.lizl.com.psnine.util.ActivityUtil
import kotlinx.android.synthetic.main.item_sub_reply_post_item.view.*

class SubReplyListAdapter(postList: List<ReplyPostModel>) :
        BaseQuickAdapter<ReplyPostModel, SubReplyListAdapter.ViewHolder>(R.layout.item_sub_reply_post_item, postList.toMutableList())
{

    companion object
    {
        val writerIdText = ContextCompat.getColor(UiApplication.instance, R.color.color_sub_reply_post_writer_id_text)
        val writerIdTextBg = ContextCompat.getColor(UiApplication.instance, R.color.color_sub_reply_post_writer_id_bg)
        val atIdText = ContextCompat.getColor(UiApplication.instance, R.color.color_sub_reply_post_at_user_id_text)
    }

    override fun convert(helper: ViewHolder, model: ReplyPostModel)
    {
        helper.bindViewHolder(model)
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView)
    {
        fun bindViewHolder(postModel: ReplyPostModel)
        {
            val postContent = " ${postModel.postWriterId}  ${postModel.atUserId} ${postModel.postContent}"

            val spannable = SpannableString(postContent)

            spannable.setSpan(object : BackgroundColorSpan(writerIdTextBg)
            {}, 0, postModel.postWriterId.length + 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)

            spannable.setSpan(object : WriterIDSpan()
            {
                override fun onClick(p0: View)
                {
                    ActivityUtil.turnToActivity(UserDetailActivity::class.java, postModel.postWriterId)
                }
            }, 0, postModel.postWriterId.length + 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)


            if (postModel.atUserId != null)
            {
                spannable.setSpan(object : AtUserIDSpan()
                {
                    override fun onClick(p0: View)
                    {
                        ActivityUtil.turnToActivity(UserDetailActivity::class.java, postModel.atUserId!!.deleteStr("@"))
                    }
                }, postModel.postWriterId.length + 3, postModel.postWriterId.length + 3 + postModel.atUserId!!.length + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            }

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