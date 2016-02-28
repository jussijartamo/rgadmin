package org.rgadmin.servlet.api.v1

import com.google.gson.GsonBuilder
import jdk.nashorn.internal.runtime.ECMAException
import org.apache.commons.io.IOUtils
import org.slf4j.LoggerFactory
import javax.script.ScriptException
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Return values:
 * 200 OK
 * 400 If problem with input including but not limited to invalid SQL
 * 500 If problem with database or connection
 */
abstract class ApiV1Servlet : HttpServlet() {
    private val LOG = LoggerFactory.getLogger(ApiV1Servlet::class.java)
    val gson = GsonBuilder().setPrettyPrinting().create()

    abstract fun executeScript(script: String, connection: Connection?): Result;

    override fun doPost(req: HttpServletRequest, res: HttpServletResponse) {
        val path = req.pathInfo ?: "/"
        when(path) {
            "/execute" -> {
                val connectionPartString: String? = try {IOUtils.toString(req.getPart("connection").inputStream)} catch(t:Throwable){null}
                val connection: Connection? = try {
                    gson.fromJson(connectionPartString,Connection::class.java)
                }catch(t: Throwable) {
                    LOG.error("Failed to execute!", t);
                    error400("Unable to read connection! ${t.message}, with connection JSON $connectionPartString")(res)
                    return;
                }
                val body: String = try {IOUtils.toString(req.getPart("script").inputStream)} catch(t: Throwable) {
                    LOG.error("Failed to execute!", t);
                    error400("Unable to read script! ${t.message}")(res)
                    return;
                }
                try {
                    ok(executeScript(body, connection))(res)
                } catch(e: ECMAException) {
                    handleScriptExecutionException(res, e.lineNumber, body, e)
                } catch(e: ScriptException) {
                    handleScriptExecutionException(res, e.lineNumber, body, e)
                } catch(t: Throwable) {
                    LOG.error("Unknown execution exception!", t);
                    error400("Unknown execution exception! ${t.message}")(res)
                }
            }
            else -> {
                error400("Unknown API call $path")(res)
            }
        }
    }
    private fun handleScriptExecutionException(res: HttpServletResponse, lineNumber: Int, body: String, e: Exception) {
        LOG.error("Script execution exception!", e);
        res.status = 400
        res.setHeader("Content-Type","application/json; charset=utf-8")
        res.writer.println(gson.toJson(ScriptErrorResponse(e.message ?: "", body.lines(), lineNumber)))

    }

    override fun doGet(req: HttpServletRequest, res: HttpServletResponse) {
        val path = req.pathInfo ?: "/"
        error400("Unknown API call $path")(res)
    }

    fun ok(body: Result): (res: HttpServletResponse) -> Unit {
        return {
            it.status = 200
            it.setHeader("Content-Type","application/json; charset=utf-8")
            it.writer.println(gson.toJson(body))
        };
    }

    fun error400(message: String): (res: HttpServletResponse) -> Unit {
        return {
        it.status = 400
        it.setHeader("Content-Type","application/json; charset=utf-8")
        it.writer.println(gson.toJson(Response(message)))
        };
    }

}