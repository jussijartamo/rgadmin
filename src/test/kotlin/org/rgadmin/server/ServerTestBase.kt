package org.rgadmin.server

import org.junit.Before
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.WebDriverWait
import org.rgadmin.server.RgServer
import org.rgadmin.servlet.CaptureServlet
import org.rgadmin.servlet.RgServlet
import org.rgadmin.servlet.api.v1.ApiV1Servlet
import org.rgadmin.servlet.api.v1.Result
import java.net.ServerSocket
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern


private val apiMock = CaptureServlet()

private val server = lazy {
    println("Starting server")
    val port = ServerSocket(0).localPort
    RgServer(port, apiMock);
}
private val driver = lazy {
    System.setProperty("webdriver.chrome.driver", "chromedriver.exe")
    val d = ChromeDriver()
    d.get("http://localhost:${server.value.port}")
    d.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
    d;
}
open class ServerTestBase {
    fun clean(str: String): String {
        return Pattern.compile("\\s+").matcher(str.replace("\r\n", " ").replace("\n", " ")).replaceAll(" ").trim()
    }

    fun driver() = driver.value

    fun wait() = WebDriverWait(driver(), 7)

    fun waitUntilExecutionDone() {
        wait().until {
            val panels = it.findElement(By.id("panels"))
            val className = panels.getAttribute("class") ?: ""
            !className.contains("blur")
        }
    }


    fun captureExecuteScript(): String {
        return apiMock.lastScript;
    }

    @Before
    fun makeSureServerIsUpAndRunning() {
        println("Server is running in port ${server.value.port}")
    }

}
