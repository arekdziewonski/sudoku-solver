package sudoku

import java.io.FileInputStream
import java.nio.file.Paths

fun main(args: Array<String>) {
    val currentDir = Paths.get("").toAbsolutePath().toFile()

    val content = FileReader(FileInputStream(args[0])).contentAsSingleLine()
    val grid = Grid(currentDir)
    grid.setContent(content)

    println("Sudoku solver")
}
