package org.rgadmin.servlet.api.v1

data class Response(val message: String)

data class ScriptErrorResponse(val message: String, val lines: List<String>, val lineNumber: Int)