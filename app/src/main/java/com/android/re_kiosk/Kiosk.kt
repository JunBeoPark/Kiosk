package com.android.re_kiosk

import android.annotation.SuppressLint
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.util.Timer
import java.util.TimerTask

val menus: MutableList<Menu> = ArrayList()
val foods: MutableList<Food> = ArrayList()
val orders: MutableList<Order> = ArrayList()
var money: Double = 0.0
var now = LocalDateTime.now()
var start = LocalDateTime.of(now.year, now.month, now.dayOfMonth, 1, 10, 0)
var end = LocalDateTime.of(now.year, now.month, now.dayOfMonth, 1, 45, 0)

@SuppressLint("NewApi")
suspend fun main() {
    init()

    while(true) {
        displayMenu()
        var selectNumber = getPureNumber()
        if(selectNumber == 0) {
            println("3초뒤에 종료합니다.")
            globalDelay(3000)
            return
        }


        var selectedFood = selectMenu(selectNumber)
        globalDelay(3000)
        selectedFood?.let { food ->
            addOrder(food)
        } ?: run {
            println("\n현재 잔액: $money \n")
        }

    }

}

fun init() {
    money = 100.0

    // 메뉴 추가
    menus.add(Menu("Burgers", "앵거스 비프 통살을 다져만든 버거"))
    menus.add(Menu("Forzen Custard", "매장에서 신선하게 만드는 아이스크림"))
    menus.add(Menu("Drinks", "매장에서 직접 만드는 음료"))
    menus.add(Menu("Beer", "뉴욕 브루클린 브루어리에서 양조한 맥주"))
    menus.add(Menu("Order", "장바구니를 확인 후 주문합니다."))
    menus.add(Menu("Cancel", "진행중인 주문을 취소합니다."))

    // 버거 종류 추가
    foods.add(Food("ShackBurger", "토마토, 양상추, 쉑소스가 토핑된 치즈버거", 6.9, "Burgers"))
    foods.add(Food("SmokeShack", "베이컨, 체리 페퍼에 쉑소스가 토핑된 치즈버거", 8.9, "Burgers"))
    foods.add(Food("Shroom Burger", "몬스터 치즈와 체다 치즈로 속을 채운 베지테리안 버거", 9.4, "Burgers"))
    foods.add(Food("Cheeseburger", "포테이토 번과 비프패티, 치즈가 토핑된 치즈버거", 6.9, "Burgers"))
    foods.add(Food("Hamburger", "비프패티를 기반으로 야채가 들어간 기본버거", 5.4, "Burgers"))

    // 아이스크림 종류 추가
    foods.add(Food("Plain Ice Cream", "바닐라 아이스크림", 12.1, "Forzen Custard"))
    foods.add(Food("Chocolate Ice Cream", "초콜릿 아이스크림", 10.2, "Forzen Custard"))
    foods.add(Food("Fruits Ice Cream", "과일 아이스크림", 15.14, "Forzen Custard"))
    foods.add(Food("Nuts Ice Cream", "아몬드 아이스크림", 15.14, "Forzen Custard"))
    foods.add(Food("Ice Milk", "저지방 아이스크림", 9.9, "Forzen Custard"))

    // 드링크 종류 추가
    foods.add(Food("Ade", "에이드", 7.5, "Drinks"))
    foods.add(Food("Americano", "아메리카노", 6.4, "Drinks"))
    foods.add(Food("Beverage", "음료수", 6.8, "Drinks"))
    foods.add(Food("Black Tea", "홍차", 7.7, "Drinks"))
    foods.add(Food("Barley Tea", "보리차", 8.9, "Drinks"))

    // 술 종류 추가
    foods.add(Food("Bokbunja", "복분자", 16.2, "Beer"))
    foods.add(Food("Bourbon", "버번위스키", 19.2, "Beer"))
    foods.add(Food("Cocktail", "칵테일", 15.4, "Beer"))
    foods.add(Food("Gin", "진", 25.2, "Beer"))
    foods.add(Food("Armand de Brignac", "아르망디 샴페인", 999.99, "Beer"))

}

fun getPureNumber(): Int {
    var userInput: String?
    var number: Int?

    while(true) {
        print("번호를 입력해주세요")
        userInput = readLine()
        number = userInput?.toIntOrNull()

        if(number != null) {
            return number
        } else {
            println("올바른 숫자를 입력해주세요")
        }
    }
}

suspend fun globalDelay(time: Long) {
    delay(time)
}


fun selectMenu(cateNumber: Int): Food? {
    var menu = menus[cateNumber-1]
    var categoryName = menu.name

    if(categoryName != "Order" && categoryName != "Cancel") { // SHAKESHACK MENU
        var filteredFoods = foods.filter { it.category == categoryName }
        displayShakeMenuDetail(categoryName)

        while(true) {
            var selectFoodNumber = getPureNumber()
            if(selectFoodNumber > filteredFoods.size || selectFoodNumber < 0) {
                println("올바른 숫자를 입력해주세요")
            } else if(selectFoodNumber == 0) {
                return null
            } else {
                return filteredFoods[selectFoodNumber-1]
            }
        }
    } else { // ORDER MENU
        when(categoryName) {
            "Order" -> {
                val totalOrderPrice = displayOrderMenuDetail(categoryName)
                if(totalOrderPrice < 0.0) {
                    println("주문 내역이 존재하지 않습니다.")
                    return null
                }
                println("1. 주문\t\t 2. 메뉴판")

                while(true) {
                    var selectOrderNumber = getPureNumber()
                    when(selectOrderNumber) {
                        1 -> {
                            var isMainatainance = isMainatainance()

                            if(isMainatainance.first) {
                                println("현재 시각은 ${isMainatainance.second.hour}시 ${isMainatainance.second.minute}분입니다.")
                                println("은행 점검 시간은 ${start.hour}시 ${start.minute}분 ~ ${end.hour}시 ${end.minute}분이므로 결제할 수 없습니다.")
                            } else if(money >= totalOrderPrice) { // 잔액 충분
                                orders.clear()
                                money -= totalOrderPrice
                                println("결제를 완료했습니다. ${isMainatainance.second.toString()}")
                            } else { // 잔액 부족
                                println("현재 잔액은 ${money}W 으로 ${totalOrderPrice - money}W이 부족해서 주문할 수 없습니다.")
                            }
                            return null
                        }
                        2 -> {
                            println("메뉴판으로 이동합니다.")
                            return null
                        }
                        else -> {
                            println("올바른 숫자를 입력해주세요")
                        }
                    }
                }
            }
            "Cancel" -> {
                orders.clear()
                println("메뉴판으로 이동합니다.")
                return null
            }
            else -> {
                return null
            }
        }
    }
}

fun displayMenu() {
    println("아래 메뉴판을 보시고 메뉴를 골라 입력해주세요.")
    println("[ SHAKESHACK MENU ]")

    val maxNameLength = menus.maxOfOrNull { it.name.length } ?: 0
    var menuSize = menus.size
    var count = 1
    for(idx in 1..menuSize) {
        val menu = menus[idx-1]
        val name = menu.name
        if(name == "Order") println("[ ORDER MENU ]")
        val desc = menu.description
        val padding = " ".repeat(maxNameLength - name.length)
        println("$idx. $name$padding | $desc")
        count++
    }
    println("0. 종료 | 프로그램 종료")
}

fun displayShakeMenuDetail(categoryName: String) {

    println("\n[ $categoryName MENU ]")

    var filteredFoods = foods.filter { it.category == categoryName }

    val maxNameLength = filteredFoods.maxOfOrNull { it.name.toString().length } ?: 0
    val maxPriceLength = filteredFoods.maxOfOrNull { it.price.toString().length } ?: 0
    var foodSize = filteredFoods.size
    for(i in 1..foodSize) {
        val food = filteredFoods[i-1]
        val name = food.name
        val price = food.price
        val desc = food.description
        val namePadding = " ".repeat(maxNameLength - name.length)
        val pricePadding = " ".repeat(maxPriceLength - price.toString().length)
        println("$i. $name$namePadding | W $price$pricePadding | $desc")
    }
    val backPadding = " ".repeat(maxNameLength - "0. back".length)
    println("0. back$backPadding | 뒤로가기")
}

fun displayOrderMenuDetail(categoryName: String): Double {
    var orderSize = orders.size
    if(orderSize > 0) {
        println("\n아래와 같이 주문 하시겠습니까?\n")

        println("[ Orders ]")
        for(i in 0 until orderSize) {
            orders[i].food.displayInfo()
        }

        println("[ Total ]")
        val totalOrderPrice = orders.fold(0.0) { accumulator, order ->
            accumulator + order.food.price
        }
        println("W $totalOrderPrice")
        return totalOrderPrice
    } else {
        return -1.0
    }
}

fun isMainatainance(): Pair<Boolean, LocalDateTime> {
    var now = LocalDateTime.now()

    return Pair(now.toLocalTime() >= start.toLocalTime() && now.toLocalTime() <= end.toLocalTime(), now)
}
fun addOrder(food: Food) {
    food.displayInfo()
    println("위 메뉴를 장바구니에 추가하시겠습니까?")
    println("1. 확인\t\t 2. 취소")

    while(true) {
        var selectOrderNumber = getPureNumber()
        when(selectOrderNumber) {
            1 -> {
                orders.add(Order(food))
                println("${food.name}를 장바구니에 추가했습니다.")
                return
            }
            2 -> {
                println("구매를 취소했습니다.")
                return
            }
            else -> {
                println("숫자를 정확히 입력해주세요")
            }
        }
    }
}