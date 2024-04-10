import Command.*

// main is the entry point when you run this environment
fun main() {
    val uiAutomation = UiAutomationSystem(name = "ZEBRA", automationUtils = QwertyImplementation(10))
    uiAutomation.submitHighScore()
}

/**
 * Commands that the Automation can perform.
 */
enum class Command {
    UP, DOWN, SELECT, LEFT, RIGHT
}

/**
 * The interface for the library you are developing.
 */
interface AutomationUtils {
    fun getCommands(input: String): List<Command>
}

class HardCodedImplementation : AutomationUtils {
    override fun getCommands(input: String): List<Command> {
        println(input[0].alphabetIndex)
        return listOf(DOWN, SELECT, DOWN, SELECT, DOWN, SELECT)
    }

    /**
     * helper function mentioned in spec
     * @returns index of character in alphabet. e.g. A → 0, B → 1, C → 2.
     */
    private val Char.alphabetIndex: Int get() = this - 'A'
}

class OneDImplementation: AutomationUtils {
    override fun getCommands(input: String): List<Command> {
        val result = mutableListOf<Command>()
        var index = 0

        input.forEach { c ->
            val target = c - 'A'
            val diff = target - index

            if (diff > 0) {
                (0 until diff).forEach {
                    result.add(DOWN)
                }
            } else if (diff < 0) {
                (0 until -diff).forEach {
                    result.add(UP)
                }
            }
            result.add(SELECT)
            index = target
        }

        return result
    }
}

class TwoByThirteenImplementation: AutomationUtils {
    override fun getCommands(input: String): List<Command> {
        val result = mutableListOf<Command>()
        var xIndex = 0
        var yIndex = 0

        input.forEach { c ->
            val target = c - 'A'
            val xTarget = target % 13
            val yTarget = target / 13

            val xDiff = xTarget - xIndex
            val yDiff = yTarget - yIndex

            if (xDiff > 0) {
                (0 until xDiff).forEach {
                    result.add(RIGHT)
                }
            } else if (xDiff < 0) {
                (0 until -xDiff).forEach {
                    result.add(LEFT)
                }
            }

            if (yDiff > 0) {
                result.add(DOWN)
            } else if (yDiff < 0) {
                result.add(UP)
            }
            result.add(SELECT)
            xIndex = xTarget
            yIndex = yTarget
        }

        return result
    }
}

class SixByFiveImplementation: AutomationUtils {
    override fun getCommands(input: String): List<Command> {
        val result = mutableListOf<Command>()
        var xIndex = 0
        var yIndex = 0

        input.forEach { c ->
            val target = c - 'A'
            val xTarget = target % 5
            val yTarget = target / 5

            val xDiff = xTarget - xIndex
            val yDiff = yTarget - yIndex

            if (xDiff > 0) {
                (0 until xDiff).forEach {
                    result.add(RIGHT)
                }
            } else if (xDiff < 0) {
                (0 until -xDiff).forEach {
                    result.add(LEFT)
                }
            }

            if (yDiff > 0) {
                (0 until yDiff).forEach {
                    result.add(DOWN)
                }
            } else if (yDiff < 0) {
                (0 until -yDiff).forEach {
                    result.add(UP)
                }
            }
            result.add(SELECT)
            xIndex = xTarget
            yIndex = yTarget
        }

        return result
    }
}

class GeneralImplementation(private val numColumns: Int, private val keyMapping: IntArray): AutomationUtils {
    override fun getCommands(input: String): List<Command> {
        val result = mutableListOf<Command>()
        var xIndex = 0
        var yIndex = 0

        input.forEach { c ->
            // val target = keyMapping[c]!!
            val target = keyMapping[c.code]!!
            val xTarget = target % numColumns
            val yTarget = target / numColumns

            val xDiff = xTarget - xIndex
            val yDiff = yTarget - yIndex

            if (xDiff > 0) {
                (0 until xDiff).forEach {
                    result.add(RIGHT)
                }
            } else if (xDiff < 0) {
                (0 until -xDiff).forEach {
                    result.add(LEFT)
                }
            }

            if (yDiff > 0) {
                (0 until yDiff).forEach {
                    result.add(DOWN)
                }
            } else if (yDiff < 0) {
                (0 until -yDiff).forEach {
                    result.add(UP)
                }
            }
            result.add(SELECT)
            xIndex = xTarget
            yIndex = yTarget
        }

        return result
    }
}

class QwertyImplementation(val numColumns: Int): AutomationUtils {
    override fun getCommands(input: String): List<Command> {
        val keyMapping = IntArray(256)
        // val keyMapping = mutableMapOf<Char, Int>()
        "QWERTYUIOPASDFGHJKL;ZXCVBNM<>?".forEachIndexed { i, c ->
            keyMapping[c.code] = i
        }

        return GeneralImplementation(numColumns, keyMapping).getCommands(input)
    }
}

// val keyMapping = IntArray(256)
// "QWERTYUIOPASDFGHJKLZXCVBNM".forEachIndexed { i, c ->

/**
 * This is what consumes the library you are developing.
 * This class should be edited by the interviewer only.
 */
class UiAutomationSystem(private val name: String, private val automationUtils: AutomationUtils) {

    /*
     * Used for validating exercise implementations.
     * This method is used to print out a list of answers to be validated by another script.
     */
    fun submitHighScore() {
        val commands = automationUtils.getCommands(name)
        val strings: List<String> = commands.map { it.name.substring(0,1).lowercase() }
        println("$name:${strings.joinToString(",")}")
    }
}