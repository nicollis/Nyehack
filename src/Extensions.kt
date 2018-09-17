val String.numVowels
    get() = count { "aeiouy".contains(it) }

fun String.addEnthusiasm(amount: Int = 1) = this + "!".repeat(amount)

fun <T> T.easyPrint(): T {
    println(this)
    return this
}

fun main(args: Array<String>) {
    "Madrigal has left the building".addEnthusiasm().easyPrint()
    42.easyPrint()

    "How many vowels?".numVowels.easyPrint()

    val coinArray = listOf(Coin(2), Coin(3))
}