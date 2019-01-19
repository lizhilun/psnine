package demo.lizl.com.psnine.model

class DiscountGameItem(val gameName: String, val gameCoverUrl: String, val discountRate: String,
                       val discountTime : String, val gamePlatform : String, val isLowest : Boolean,
                       val originalPrice: String, val notMemberPrice: String, val memberPrice: String,
                       val psnGameId : String)
{}