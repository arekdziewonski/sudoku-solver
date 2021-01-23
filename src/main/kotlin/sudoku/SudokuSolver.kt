package sudoku

import java.io.FileInputStream
import java.nio.file.Paths
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.size != 1) {
        println("usage: SudokuSolver <file-with-initial-grid>")
        exitProcess(1)
    }
    val currentDir = Paths.get("").toAbsolutePath().toFile()

    val content = FileReader(FileInputStream(args[0])).contentAsSingleLine()
    val grid = Grid(currentDir)
    grid.setContent(content)

    println("Sudoku solver")
}
