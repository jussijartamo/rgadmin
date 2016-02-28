package org.rgadmin.servlet.api.v1

data class Connection(
        val host: String,
        val port: Int,
        val database: String,
        val username: String,
        val password: String);