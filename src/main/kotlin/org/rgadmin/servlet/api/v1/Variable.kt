package org.rgadmin.servlet.api.v1

interface Variable {
    val name: String
}

data class StreamVariable(override val name: String, val rows: Collection<Collection<Any?>>): Variable;

data class TableVariable(override val name: String, val columns: List<String>, val rows: Collection<Collection<Any?>>): Variable;

data class StringVariable(override val name: String, val value: String): Variable;

data class UnknownVariable(override val name: String, val value: String, val javaclass: String): Variable;

data class IntVariable(override val name: String, val value: Int): Variable;

data class ListVariable(override val name: String, val items: Collection<*>): Variable;
