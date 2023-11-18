package models

class BakedGoods(
    var productId: Int,
    var productName: String,
    var productDesc: String,
    var productPrice: Double,
    var category: String,
    var refrigeratedOrNot: Boolean = false
)
