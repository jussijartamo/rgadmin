package org.rgadmin.servlet

import org.rgadmin.servlet.api.v1.ApiV1Servlet
import org.rgadmin.servlet.api.v1.Connection
import org.rgadmin.servlet.api.v1.Result
import java.util.*

class CaptureServlet : ApiV1Servlet() {

    var lastScript = ""

    override fun executeScript(script: String, connection: Connection?): Result {
        lastScript = script
        Thread.sleep(1000L)
        return Result(Collections.emptyList())
    }

}