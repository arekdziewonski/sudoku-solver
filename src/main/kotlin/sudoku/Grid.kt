package sudoku

import org.neo4j.configuration.GraphDatabaseSettings.DEFAULT_DATABASE_NAME
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder
import org.neo4j.graphdb.Label
import org.neo4j.graphdb.Node
import org.neo4j.graphdb.RelationshipType
import org.neo4j.graphdb.Transaction
import java.nio.file.Path

class Grid(dbDirectory: Path) {
    companion object RelType {

        // we're going to have just one type of cells
        val label = Label.label("cell")

        // points to the next cell in the sequence
        val nextRelType: RelationshipType = RelationshipType.withName("next")

        // points to an adjacent cell (adjacent cells can't have the same value)
        val adjacentRelType: RelationshipType = RelationshipType.withName("adjacent")

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
        createIndexes()
        createCells()
        createRelationships()
    }

    private fun createIndexes() {
        val tx = graphDb.beginTx()
        tx.use {
            val schema = tx.schema()
            schema.indexFor(label).on(CELL_ROW).create()
            schema.indexFor(label).on(CELL_COL).create()
            schema.indexFor(label).on(CELL_BOX).create()
            tx.commit()
        }
    }

    private fun createCells() {
        val tx = graphDb.beginTx()
        tx.use {
            var previousCell: Node? = null

            for (i in 0..80) {
                val cell = tx.createNode(label)
                val row = i / 9
                val col = i % 9
                cell.setProperty(CELL_ROW, row)
                cell.setProperty(CELL_COL, col)
                cell.setProperty(CELL_BOX, cellBox(row, col))

                if (previousCell != null) {

                    // connect the previous cell to this one
                    previousCell.createRelationshipTo(cell, nextRelType)
                } else {

                    // remember the first cell in order to traverse the graph again
                    firstCellId = cell.getId()
                }

                previousCell = cell
            }
            tx.commit()
        }
    }

    private fun cellBox(row: Int, col: Int) = 3 * (row / 3) + col / 3

    private fun createRelationships() {
        val tx = graphDb.beginTx()
        tx.use {
            val firstCell = tx.getNodeById(firstCellId)
            val nodes = tx.traversalDescription().breadthFirst().relationships(nextRelType).traverse(firstCell).nodes()
            for (cell in nodes) {
                createAdjacentRelationship(tx, cell, CELL_ROW, cell.getProperty(CELL_ROW))
                createAdjacentRelationship(tx, cell, CELL_COL, cell.getProperty(CELL_COL))
                createAdjacentRelationship(tx, cell, CELL_BOX, cell.getProperty(CELL_BOX))
            }
            tx.commit()
        }
    }

    private fun createAdjacentRelationship(tx: Transaction, from: Node, property: String, value: Any) {
        val nodes = tx.findNodes(label, property, value)
        for (to in nodes) {
            from.createRelationshipTo(to, adjacentRelType)
        }
    }

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
