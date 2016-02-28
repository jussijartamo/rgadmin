package org.rgadmin.servlet

import org.rgadmin.servlet.api.v1.ApiV1Servlet
import org.rgadmin.servlet.api.v1.Result
import java.util.*

class CaptureServlet : ApiV1Servlet() {

    var lastScript = ""

    override fun executeScript(script: String): Result {
        lastScript = script
        Thread.sleep(1000L)
        return Result(Collections.emptyList())
    }

}