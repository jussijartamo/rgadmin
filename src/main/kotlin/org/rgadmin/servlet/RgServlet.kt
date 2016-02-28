package org.rgadmin.servlet

import jdk.nashorn.internal.runtime.ECMAException
import org.dalesbred.Database
import org.postgresql.ds.PGSimpleDataSource
import org.rgadmin.collection.resultTableToTable
import org.rgadmin.nashorn.NashornExecutor
import org.rgadmin.servlet.api.v1.ApiV1Servlet
import org.rgadmin.servlet.api.v1.Connection
import org.rgadmin.servlet.api.v1.Result
import java.util.function.Function
import java.util.stream.Stream

class RgServlet : ApiV1Servlet() {

    private val nashorn = NashornExecutor()

    override fun executeScript(script: String, connection: Connection?): Result {
        val datasource: PGSimpleDataSource? = if(connection != null){
            val ds = PGSimpleDataSource()
            ds.url = "jdbc:postgresql://${connection.host}:${connection.port}/${connection.database}?user=${connection.username}&password=${connection.password}"
            ds
        } else {
            null
        }
        try {
            return Result(nashorn.script {
                if (datasource != null) {
                    val db = Database.forDataSource(datasource)

                    val c = Function<String, Stream<Collection<Any?>>>() {
                        resultTableToTable(db.findTable(it))
                    }
                    it.put("find", c)
                } else {
                    val c = Function<String, Unit>() {
                        throw ECMAException("Cannot query database without database connection!", null)
                    }
                    it.put("find", c)
                }
            }.execute(script))
        } finally {
            if(datasource != null) {
                datasource.connection.close()
            }
        }
    }
}
