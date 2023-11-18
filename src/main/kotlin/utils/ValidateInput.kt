package utils

import utils.ScannerInput.readNextInt
import java.util.*

/**
 * A utility class providing functions for validating user input related to categories and priorities.
 */
object ValidateInput {

    /**
     * Reads and validates a category from user input.
     *
     * @param prompt The prompt message to display to the user.
     * @return The validated category string.
     */
    @JvmStatic
    fun readValidCategory(prompt: String?): String {
        print(prompt)
        var input = Scanner(System.`in`).nextLine()
        do {
            if (CategoryUtility.isValidCategory(input))
                return input
            else {
                print("Invalid category $input.  Please try again: ")
                input = Scanner(System.`in`).nextLine()
            }
        } while (true)
    }

    /**
     * Reads and validates a priority value from user input.
     *
     * @param prompt The prompt message to display to the user.
     * @return The validated priority value (an integer in the range 1 to 5).
     */
    @JvmStatic
    fun readValidPriority(prompt: String?): Int {
        var input = readNextInt(prompt)
        do {
            if (Utilities.validRange(input, 1, 5))
                return input
            else {
                print("Invalid priority $input.")
                input = readNextInt(prompt)
            }
        } while (true)
    }
}
