package com.android.re_kiosk

class Order(food: Food) {
    var idx: Int
    var food: Food

    init {
        this.idx = getNextIdx()
        this.food = food
    }

    companion object {
        private var maxIdx = 1

        private fun getNextIdx(): Int {
            return maxIdx++
        }
    }
}