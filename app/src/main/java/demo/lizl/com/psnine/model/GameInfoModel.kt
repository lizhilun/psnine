package demo.lizl.com.psnine.model

data class GameInfoModel(val coverUrl: String, val gameName: String, val gameDetailUrl: String, var lastUpdateTime: String? = null)
{
    var gameCupInfo: String? = null

    var completionRate: String? = null

    var isPS4Game: Boolean = false

    var isPS3Game: Boolean = false

    var isPSVGame: Boolean = false

    var perfectRate: String? = null
}