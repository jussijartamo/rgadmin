package org.rgadmin

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.rgadmin.nashorn.NashornExecutor
import org.rgadmin.server.ServerTestBase
import org.rgadmin.servlet.api.v1.Result
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.function.Consumer
import java.util.regex.Pattern

class UIToServerTest : ServerTestBase() {

    @Test fun es7Test() {
        val inputElement = driver().findElementById("codearea")
        val textinput = inputElement.findElement(By.tagName("textarea"))

        textinput.sendKeys(clean("""
        const i = (items) => {
            return "jee " + items;
        };
        print(i);
        """))

        textinput.sendKeys(Keys.chord(Keys.CONTROL, Keys.ENTER));

        waitUntilExecutionDone()
        val print = Mockito.mock(Consumer::class.java)

        NashornExecutor().script {
            it.put("print", print)
        }.execute(captureExecuteScript())

        //Mockito.verify(print, Mockito.atLeast(1)).accept(Mockito.anyObject())
    }

}

