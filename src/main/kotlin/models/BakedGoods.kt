package models

class BakedGoods(
    var productId: Int,
    var productName: String,
    var productDesc: String,
    var productPrice: Double,
    var productCategory: String,
    var refrigeratedOrNot: Boolean = false
){
    constructor() : this(0,"","", 0.0, "", false)
}
