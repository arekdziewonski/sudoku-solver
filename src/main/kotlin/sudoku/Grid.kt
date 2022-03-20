package sudoku

import org.neo4j.configuration.GraphDatabaseSettings.DEFAULT_DATABASE_NAME
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder
import org.neo4j.graphdb.RelationshipType
import java.io.File

class Grid(dbDirectory: File) {
    companion object RelType {

        // points to the next cell in the sequence
        val nextRelType: RelationshipType = RelationshipType.withName("next")

        // value in the cell
        val CELL_VALUE = "value"

        // row the cell is in
        val CELL_ROW = "row"

        // column the cell is in
        val CELL_COL = "col"

        // box the cell is in
        val CELL_BOX = "box"
    }

    val managementService = DatabaseManagementServiceBuilder(dbDirectory).build()
    val graphDb = managementService.database(DEFAULT_DATABASE_NAME)
    var firstCellId: Long = 0

    init {
        val tx = graphDb.beginTx()
        tx.use {
            var previousCell = tx.createNode()
            firstCellId = previousCell.getId()
            for (i in 1..80) {
                val newCell = tx.createNode()
                val row = i / 9
                val col = i % 9
                newCell.setProperty(CELL_ROW, row)
                newCell.setProperty(CELL_COL, col)
                newCell.setProperty(CELL_BOX, cellBox(row, col))

                previousCell.createRelationshipTo(newCell, nextRelType)
                previousCell = newCell
            }
            tx.commit()
        }
    }

    private fun cellBox(row: Int, col: Int) = 3 * (row / 3) + col / 3

    fun setContent(content: String) {
        val tx = graphDb.beginTx()
        tx.use {
            val firstCell = tx.getNodeById(firstCellId)
            val nodes = tx.traversalDescription().breadthFirst().relationships(nextRelType).traverse(firstCell).nodes()
            for ((cell, value) in nodes.zip(content.toList())) {
                if (value != '.') {
                    cell.setProperty(CELL_VALUE, Character.getNumericValue(value))
                }
            }
            tx.commit()
        }
    }
}
