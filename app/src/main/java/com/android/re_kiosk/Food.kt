package com.android.re_kiosk

class Food(name: String, description: String, price: Double, category: String): Menu(name, description) {
    var price: Double
    var category: String

    init {
        this.price = price
        this.category = category
    }

    override fun displayInfo() {
        println("ID: $idx, 카테고리: $category, 가격: $price, 이름: $name, 설명: [ $description ]")
    }
}