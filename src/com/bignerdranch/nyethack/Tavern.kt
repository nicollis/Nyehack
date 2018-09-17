package com.bignerdranch.nyethack

import java.io.File

const val tavernName = "Taernyl's Folly"

val patronList = mutableListOf("Eli", "Mordoc", "Sophie")
val lastName = listOf("Ironfoot", "Fernsworth", "Baggins")
val uniquePatrons = mutableSetOf<String>()
val menuList = File("data/tavern-menu-items.txt").readText().split("\n")
val patronGold = mutableMapOf<String, Double>()

fun main(args: Array<String>) {
    if (patronList.contains("Eli"))
        println("The tavern master says: Eli's in the back playing cards.")
    else
        println("The tavern master says: Eli isn't here.")

    if (patronList.containsAll(listOf("Sophie", "Mordoc")))
        println("The tavern master says: Yea, they're seated by the stew kettle.")
    else
        println("The tavern master says: Nay, they departed hours ago.")


    (0..9).forEach {
        val first = patronList.shuffled().first()
        val last = lastName.shuffled().first()
        uniquePatrons += "$first $last"
    }

    uniquePatrons.forEach {
        patronGold[it] = 6.0
    }

    var orderCount = 0
    while(orderCount <= 9) {
        if (uniquePatrons.isEmpty()) { return }
        placeOrder(uniquePatrons.shuffled().first(),
                menuList.shuffled().first())
        orderCount++
    }

    displayPatronBalances()
}

private fun displayPatronBalances() {
    patronGold.forEach { patron, balance ->
        println("$patron, balance: ${"%.2f".format(balance)}")
    }
}

private fun performPurchase(price: Double, patronName: String): Boolean {
    val totalPurse = patronGold.getValue(patronName)
    return if (totalPurse >= price) {
        patronGold[patronName] = totalPurse - price
        true
    } else {
        println("Tavern Bouncer throws $patronName, out onto the mean streets of NyetHack for loitering")
        uniquePatrons.remove(patronName)
        patronGold.remove(patronName)
        false
    }
}

private fun placeOrder(patronName: String, menuData: String) {
    val indexOfApostrophe = tavernName.indexOf('\'')
    val tavernMaster = tavernName.substring(0 until indexOfApostrophe)
    println("$patronName speaks with $tavernMaster about their order.")

    val (type, name, price) = menuData.split(',')
    val message = "$patronName buys a $name ($type) for $price."
    println(message)

    if (!performPurchase(price.toDouble(), patronName)) { return }

    val phrase = if (name == "Dragon's Breath") {
        "$patronName exclaims ${toDragonSpeak("Ah, delicious $name!")}"
    } else {
        "$patronName says: Thanks for the $name."
    }
    println(phrase)
}

private fun toDragonSpeak(phrase: String) =
        phrase.replace(Regex("[aeiou]")) {
            when (it.value) {
                "a" -> "4"
                "e" -> "3"
                "i" -> "1"
                "o" -> "0"
                "u" -> "|_|"
                else -> it.value
            }
        }