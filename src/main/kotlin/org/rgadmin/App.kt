package org.rgadmin

import org.rgadmin.server.RgServer
import org.rgadmin.servlet.RgServlet
import java.net.InetSocketAddress
import java.net.ServerSocket

fun main(args: Array<String>) {
    RgServer(8080, RgServlet()).join();
}