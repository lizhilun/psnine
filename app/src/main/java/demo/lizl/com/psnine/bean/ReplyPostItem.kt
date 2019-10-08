package demo.lizl.com.psnine.bean

class ReplyPostItem(val imageUrl: String, val postContent: String, val postWriterId: String, val postTime: String, val writerUrl: String)
{
    val subReplyPostList = mutableListOf<ReplyPostItem>()

    var atUserId: String? = null

    var atUserUrl: String? = null
}