package demo.lizl.com.psnine.util

import android.util.Log
import androidx.core.content.ContextCompat
import demo.lizl.com.psnine.R
import demo.lizl.com.psnine.UiApplication
import demo.lizl.com.psnine.constant.AppConstant
import demo.lizl.com.psnine.custom.function.deleteChinese
import demo.lizl.com.psnine.custom.function.deleteStr

object GameUtil
{
    private val TAG = "GameUtil"

    @JvmStatic
    fun getPrefectRateColor(perfectRate: Float): Int
    {
        return ContextCompat.getColor(UiApplication.instance, when
        {
            perfectRate > 60 -> R.color.color_perfect_rate_extremely_easy
            perfectRate > 40 -> R.color.color_perfect_rate_easy
            perfectRate > 25 -> R.color.color_perfect_rate_normal
            perfectRate > 15 -> R.color.color_perfect_rate_troublesome
            perfectRate > 5  -> R.color.color_perfect_rate_hard
            perfectRate > 0  -> R.color.color_perfect_rate_extremely_hard
            else             -> R.color.color_perfect_rate_impossible
        })
    }

    @JvmStatic
    fun getPrefectRateDescription(perfectRate: Float): String
    {
        return UiApplication.instance.getString(when
        {
            perfectRate > 60 -> R.string.extremely_easy
            perfectRate > 40 -> R.string.easy
            perfectRate > 25 -> R.string.normal
            perfectRate > 15 -> R.string.troublesome
            perfectRate > 5  -> R.string.hard
            perfectRate > 0  -> R.string.extremely_hard
            else             -> R.string.impossible
        })
    }

    @JvmStatic
    fun getCompletionRateColor(completionRate: Float): Int
    {
        return ContextCompat.getColor(UiApplication.instance, when
        {
            completionRate < 25 -> R.color.color_game_completion_rate_low
            completionRate < 50 -> R.color.color_game_completion_rate_just_so_so
            completionRate < 75 -> R.color.color_game_completion_rate_ok
            else                -> R.color.color_game_completion_rate_good
        })
    }

    @JvmStatic
    fun getPlatformColor(platform: String): Int
    {
        return ContextCompat.getColor(UiApplication.instance, when (platform)
        {
            "PS3" -> R.color.color_bg_label_PS3_game
            "PSV" -> R.color.color_bg_label_PSV_game
            "PS4" -> R.color.color_bg_label_PS4_game
            else  -> R.color.color_bg_label_PS4_game
        })
    }

    @JvmStatic
    fun getGameCupBgColor(cupType: Int): Int
    {
        return ContextCompat.getColor(UiApplication.instance, when (cupType)
        {
            AppConstant.GAME_CUP_TYPE_PLATINUM -> R.color.color_game_cup_bg_platinum
            AppConstant.GAME_CUP_TYPE_GOLD     -> R.color.color_game_cup_bg_gold
            AppConstant.GAME_CUP_TYPE_SILVER   -> R.color.color_game_cup_bg_silver
            AppConstant.GAME_CUP_TYPE_BRONZE   -> R.color.color_game_cup_bg_bronze
            else                               -> R.color.color_game_cup_bg_bronze
        })
    }

    @JvmStatic
    fun rateToInt(rate: String?): Int
    {
        return rateToFloat(rate).toInt()
    }

    @JvmStatic
    fun rateToFloat(rate: String?): Float
    {
        try
        {
            if (rate.isNullOrBlank()) return 0F
            return rate.deleteChinese().deleteStr("%").toFloat()
        }
        catch (e: Exception)
        {
            Log.e(TAG, "rateToFloat error:", e)
        }
        return 0F
    }

    @JvmStatic
    fun spaceToNewLine(oriStr: String?): String
    {
        return oriStr?.replace(" ", "\n").orEmpty()
    }
}