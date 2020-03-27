package sudoku

import java.nio.file.Paths

fun main(args: Array<String>) {
    val currentDir = Paths.get("").toAbsolutePath().toFile()
    Grid(currentDir)
    println("Sudoku solver")
}
