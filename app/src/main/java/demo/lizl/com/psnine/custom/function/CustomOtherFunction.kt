package demo.lizl.com.psnine.custom.function

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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

fun GlobalScope.ui(runnable: () -> Unit)
{
    GlobalScope.launch(Dispatchers.Main) {
        runnable.invoke()
    }
}