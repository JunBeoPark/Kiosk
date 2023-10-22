package com.android.re_kiosk

open class Menu(name: String, description: String) {
    var idx: Int
    var name: String
    var description: String

    init {
        this.idx = getNextIdx()
        this.name = name
        this.description = description
    }

    open fun displayInfo() {
        println("ID: $idx, 이름: $name, 설명: [ $description ]")
    }

    companion object {
        private var maxIdx = 1

        private fun getNextIdx(): Int {
            return maxIdx++
        }
    }
}