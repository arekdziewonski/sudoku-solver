package sudoku

import org.neo4j.configuration.GraphDatabaseSettings.DEFAULT_DATABASE_NAME
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder
import java.io.File

class Grid(dbDirectory: File) {
    val managementService = DatabaseManagementServiceBuilder(dbDirectory).build()
    val graphDb = managementService.database(DEFAULT_DATABASE_NAME)
}
