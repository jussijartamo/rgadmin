package org.rgadmin

import org.junit.Assert
import org.junit.Test
import org.rgadmin.nashorn.NashornExecutor
import java.util.function.Consumer
import javax.script.ScriptContext
import javax.script.ScriptEngineManager

class NashornTest {

    @Test
    fun variables() {
        val script = """
        "use strict";

        var someArr = [1, "2"];
        var someText = "jeZ";
        var i = 54;
        var result = 4 * 4;
        """

        val vars = NashornExecutor().script {
        }.execute(script)

        println(vars)

        Assert.assertTrue(vars.size == 4)
    }

    @Test
    fun evaluateScriptTest() {
        val script = """
        "use strict";

        print('Hello World!');
        """

        NashornExecutor().script {
        }.execute(script)

    }

}