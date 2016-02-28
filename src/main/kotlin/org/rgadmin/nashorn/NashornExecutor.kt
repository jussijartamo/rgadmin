package org.rgadmin.nashorn

import com.google.common.collect.Lists
import jdk.nashorn.api.scripting.ScriptObjectMirror
import org.rgadmin.collection.Table
import org.rgadmin.servlet.api.v1.*
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream
import javax.script.*

class NashornExecutor {

    class NashornScript(private val bindings: Bindings,
                        private val engine: ScriptEngine,
                        private val compilingEngine: Compilable) {

        fun execute(script: String): List<Variable> {
            val cscript = compilingEngine.compile(script)
            cscript.eval(bindings);
            val bind = engine.getBindings(ScriptContext.ENGINE_SCOPE);
            val allAttributes = bind.keys;
            val allVariables = allAttributes.filter { !"function".equals( engine.eval("typeof " + it))}
            return allVariables.map {
                val value = engine.get(it)
                if(value == null) {
                    StringVariable(it, "null");
                } else {
                    when(value) {
                        is Int -> IntVariable(it,value)
                        is String -> StringVariable(it, value)
                        is Table -> {
                            TableVariable(it, value.columnNames, f(value))
                        }
                        is Stream<*> -> {
                            StreamVariable(it, unk(value as Stream<Any?>))
                        }
                        is ScriptObjectMirror -> {
                            if(value.isArray) {
                                ListVariable(it, value.values)
                            } else {
                                UnknownVariable(it, "" + value, value.javaClass.name);
                            }
                        }
                        else -> {
                            UnknownVariable(it, "" + value, value.javaClass.name);
                        }
                    }
                }
            }
        }
    }

    fun script(bindings: (Bindings) -> Unit):  NashornScript {
        val engine = ScriptEngineManager().getEngineByName("nashorn");
        val compilingEngine = engine as Compilable;
        val b = engine.getBindings(ScriptContext.ENGINE_SCOPE);
        bindings.invoke(b)
        val s = NashornScript(b, engine, compilingEngine)
        return s
    }
}

fun unk(x: Stream<Any?>): List<Collection<Any?>>  {
    val l = ArrayList<Collection<Any?>>()
    x.forEach {
        when(it) {
            is Collection<Any?> -> {
                l.add(it)
            }
            is ScriptObjectMirror -> {
                if(it.isArray) {
                    l.add(it.values)
                }
            }
            is String -> {
                l.add(listOf(it))
            }
            is Int -> {
                l.add(listOf(it))
            }
        }
    }
    return l;
}

fun f(x: Stream<Collection<Any?>>): List<Collection<Any?>> {
    val l = ArrayList<Collection<Any?>>()
    x.forEach {
        l.add(it)
    }
    return l
}
