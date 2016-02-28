package org.rgadmin.collection

import jdk.nashorn.api.scripting.AbstractJSObject
import jdk.nashorn.api.scripting.JSObject
import org.dalesbred.result.ResultTable
import java.util.stream.Stream
import java.util.stream.StreamSupport

class Table(val columnNames: List<String>, l: Stream<Collection<Any?>>): Stream<Collection<Any?>> by l;

class ListWithColumnNameAccess(a: AbstractJSObject, l: List<Any?>): List<Any?> by l, JSObject by a;

class ColumnAccessDecorator(private val __columnNameToIndex: Map<String, Int>, private val __row: List<Any?>) : AbstractJSObject() {
    override fun getMember(name: String): Any? {
        val maybeIndex = __columnNameToIndex.get(name)
        if(maybeIndex != null) {
            return __row.get(maybeIndex)
        } else {
            return null;
        }
    }
}

fun resultTableToTable(table: ResultTable): Stream<Collection<Any?>> {
    val columnNameToIndex: Map<String, Int> = table.columnNames.mapIndexed { i, s -> Pair(s,i) }.toMap()
    val v: List<Collection<Any?>> = table.rows.map({
        val row: List<Any?> = it.asList()
        val c: Collection<Any?> = ListWithColumnNameAccess(ColumnAccessDecorator(columnNameToIndex,row), row)
        c
    });
    val q: Stream<Collection<Any?>> = StreamSupport.stream(java.lang.Iterable<Collection<Any?>> { v.iterator() }.spliterator(), false)

    return Table(table.columnNames,q);
}
