package com.siili.sample.database

import com.siili.Models
import io.requery.Persistable
import io.requery.reactivex.KotlinReactiveEntityStore
import io.requery.sql.KotlinConfiguration
import io.requery.sql.KotlinEntityDataStore
import org.h2.jdbcx.JdbcDataSource
import javax.sql.DataSource

object RequeryDatabase {

    private fun initDatasource(): DataSource {
        val ds = JdbcDataSource()
        ds.setURL("jdbc:h2:mem:requery;DB_CLOSE_DELAY=-1")
        createTableAndInsertSampleData(ds)
        return ds
    }

    private fun createTableAndInsertSampleData(ds: JdbcDataSource) {
        val connection = ds.connection
        val stmt = connection.createStatement();
        stmt.execute("CREATE TABLE Employee(id IDENTITY PRIMARY KEY, first_name VARCHAR(255) NOT NULL, last_name VARCHAR(255) NOT NULL)")

        stmt.execute("INSERT INTO Employee(first_name, last_name) VALUES ('John', 'Smith')");
        stmt.execute("INSERT INTO Employee(first_name, last_name) VALUES ('Chad', 'Blessing')");
        stmt.execute("INSERT INTO Employee(first_name, last_name) VALUES ('Katy', 'Running')");

        stmt.close()
        connection.close()
    }

    private val configuration = KotlinConfiguration(dataSource = initDatasource(), model = Models.DEFAULT)

    val store = KotlinEntityDataStore<Persistable>(configuration)
    val reactiveStore = KotlinReactiveEntityStore(store)

}
