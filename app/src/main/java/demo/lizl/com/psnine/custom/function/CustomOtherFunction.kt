package demo.lizl.com.psnine.custom.function

fun String.deleteStr(str: String): String
{
    return replace(str, "")
}

fun String.deleteChinese(): String
{
    val stringBuffer = StringBuffer()
    this.toCharArray().forEach { if (it.toInt() < 128) stringBuffer.append(it) }
    return stringBuffer.toString()
}