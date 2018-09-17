package com.bignerdranch.nyethack

fun main(args: Array<String>) {
    runSimulation()
}

private fun runSimulation() {
    val greetingFunction = configureGreetingFunction()
    println(greetingFunction("Guyal"))
}

private fun configureGreetingFunction(): (String) -> String {
    val structureType = "hospitals"
    var numBuildings = 5
    return { playerName: String ->
        val currentYear = 2018
        numBuildings += 1
        println("Adding $numBuildings $structureType")
        "Welcome to SimVillage, $playerName! (copyright $currentYear)"
    }
}

inline fun runSimulation(playerName: String,
                         costPrinter: (Int) -> Unit,
                         greetingFunction: (String, Int) -> String) {
    val numBuilding = (1..3).shuffled().last() // Randomly selects 1, 2, or 3
    costPrinter(numBuilding)
    println(greetingFunction(playerName, numBuilding))
}

private fun printConstructionCost(numBuilding: Int) {
    val cost = 500
    println("construction cost: ${cost * numBuilding}")
}