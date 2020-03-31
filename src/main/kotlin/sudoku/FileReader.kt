package sudoku

import java.io.InputStream
import java.io.InputStreamReader

class FileReader(val inputStream: InputStream) {

    fun contentAsSingleLine(): String {
        val reader = InputStreamReader(inputStream)
        return reader.useLines { lines ->
            val lineList = lines.toList()
            if (lineList.size != 9) {
                throw RuntimeException("Grid must contain 9 rows")
            }
            if (lineList.any { l -> l.length != 9 }) {
                throw RuntimeException("Each row must contain 9 cells")
            }
            val content = lineList.reduce { acc, s -> acc + s }
            if (!"[\\d|\\.]{81}".toRegex().matches(content)) {
                throw RuntimeException("Each cell must contain a digit or a dot")
            }
            content
        }
    }
}