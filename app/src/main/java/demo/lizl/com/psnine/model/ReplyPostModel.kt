package demo.lizl.com.psnine.model

class ReplyPostModel(val imageUrl: String, val postContent: String, val postWriterId: String, val postTime: String, val writerUrl: String)
{
    val subReplyPostList = mutableListOf<ReplyPostModel>()

    var atUserId: String? = null

    var atUserUrl: String? = null
}