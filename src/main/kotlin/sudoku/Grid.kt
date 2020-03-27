package sudoku

import org.neo4j.configuration.GraphDatabaseSettings.DEFAULT_DATABASE_NAME
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder
import org.neo4j.graphdb.RelationshipType
import java.io.File


class Grid(dbDirectory: File) {
    companion object RelType {

        // points to the next cell in the sequence
        val nextRelType: RelationshipType = RelationshipType.withName("next")
    }

    val managementService = DatabaseManagementServiceBuilder(dbDirectory).build()
    val graphDb = managementService.database(DEFAULT_DATABASE_NAME)

    init {
        val tx = graphDb.beginTx()
        tx.use {
            var previousCell = tx.createNode()
            for (i in 1..80) {
                val newCell = tx.createNode()
                previousCell.createRelationshipTo(newCell, nextRelType)
                previousCell = newCell
            }
            tx.commit()
        }
    }
}
