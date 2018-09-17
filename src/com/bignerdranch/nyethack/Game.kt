package com.bignerdranch.nyethack

import kotlin.system.exitProcess

fun main(args: Array<String>) {
    Game.play()
}

object Game {
    private var gameRunning = true
    private val player = Player("Madrigal", 89, true, false)
    private var currentRoom: Room = TownSquare()

    private var worldMap = listOf(
            listOf(currentRoom, Room("Tavern"), Room("Back Room")),
            listOf(Room("Long Cooridor"), Room("Generic Room"))
    )

    init {
        println("Welcome, adventurer.")
        player.castFireball()
    }

    fun play() {
        while (gameRunning) {
            println(currentRoom.description())
            println(currentRoom.load())

            printPlayerStatus(player)

            print("> Enter your command: ")
            println(GameInput(readLine()).processCommand())
            println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
        }
    }

    private fun printPlayerStatus(player: Player) {
        println("(Aura: ${player.auraColor()}) " +
                "(Blessed: ${if (player.isBlessed) "YES" else "NO"})")
        println("${player.name} ${player.formatHealthStatus()}")
    }

    private class GameInput(arg: String?) {
        private val input = arg ?: ""
        val command = input.split(" ")[0]
        val argument = input.split(" ").getOrElse(1) { "" }

        fun processCommand() = when (command.toLowerCase()){
            "fight" -> fight()
            "move" -> move(argument)
            "map" -> map()
            "ring" -> ring(argument, currentRoom)
            "exit" -> quit()
            "quit" -> quit()
            else -> commandNotFound()
        }

        private fun commandNotFound() = "I'm not quite sure what you're trying to do!"
    }

    private fun fight() = currentRoom.monster?.let {
        while(player.healthPoints > 0 && it.healthPoints > 0) {
            slay(it)
            Thread.sleep(1000)
        }
        "Combat complete."
    } ?: "There's nothing here to fight, calm yourself down"

    private fun slay(monster: Monster) {
        println("${monster.name} did ${monster.attack(player)} damage!")
        println("${player.name} did ${player.attack(monster)} damage!")

        if (player.healthPoints <= 0) {
            println(">>>>> You have been defeatred... that was a little emberrising, better luck to the next who comes <<<<<")
            exitProcess(0)
        }

        if (monster.healthPoints <= 0) {
            println(">>>>> ${monster.name} has been defeated! <<<<<")
            currentRoom.monster = null
        }
    }

    private fun ring(toRing: String ,room: Room) = when (toRing.toLowerCase()) {
                "bell" ->
                    (room as? TownSquare)?.ringBell() ?: "Can't seem to find a bell to ring"
                else -> "You can't ring that"
            }

    private fun move(directionInput: String) =
            try {
                val direction = Direction.valueOf(directionInput.toUpperCase())
                val newPosition = direction.updateCoordinate(player.currentPosition)
                if (!newPosition.isInBounds) {
                    throw IllegalStateException("$direction is out of bounds")
                }

                val newRoom = worldMap[newPosition.y][newPosition.x]
                player.currentPosition = newPosition
                currentRoom = newRoom
                "Ok, you move $direction to the ${newRoom.name}.\n${newRoom.load()}"
            } catch (e: Exception) {
                "There seems to be some kind of force field preventing us from walking $directionInput though this wall... might look another way"
            }

    private fun map() = worldMap.joinToString("\n") {
            it.joinToString(" ") {
                if (it == currentRoom) "X " else "O "
            }
        }.trimEnd()

    private  fun quit(): String {
        gameRunning = false
        return "The battle has been well fought and glorious, ${player.name}. Until next time."
    }
}
