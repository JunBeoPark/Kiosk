package com.android.re_kiosk

import java.time.LocalDateTime

val menus: MutableList<Menu> = ArrayList()
val foods: MutableList<Food> = ArrayList()
val orders: MutableList<Order> = ArrayList()
var money: Double = 0.0
var now = LocalDateTime.now()
var start = LocalDateTime.of(now.year, now.month, now.dayOfMonth, 1, 10, 0)
var end = LocalDateTime.of(now.year, now.month, now.dayOfMonth, 1, 45, 0)