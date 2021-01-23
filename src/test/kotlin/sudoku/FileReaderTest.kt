package sudoku

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

internal class FileReaderTest {
    @Test
    internal fun readsFileContent() {
        val inputStream = this::class.java.getResourceAsStream("/input_file/correct.txt")
        val content = FileReader(inputStream).contentAsSingleLine()

        assertEquals("53..7....6..195....98....6.8...6...34..8.3..17...2...6.6....28....419..5....8..79", content)
    }

    @Test
    internal fun throwsOnTruncatedFile() {
        val inputStream = this::class.java.getResourceAsStream("/input_file/truncated.txt")

        val exception = assertThrows(RuntimeException::class.java) { FileReader(inputStream).contentAsSingleLine() }
        assertEquals("Grid must contain 9 rows", exception.message)
    }
}
