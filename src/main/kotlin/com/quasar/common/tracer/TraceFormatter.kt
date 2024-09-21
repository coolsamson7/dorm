package com.quasar.common.tracer

import java.text.SimpleDateFormat

/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

abstract class Format() {
    abstract fun format(entry: TraceEntry, builder: StringBuilder)
}

class FormatString(val string: String) : Format() {
    // implement

    override fun format(entry: TraceEntry, builder: StringBuilder) {
        builder.append(string)
    }
}

class FormatMessage() : Format() {
    // implement

    override fun format(entry: TraceEntry, builder: StringBuilder) {
        builder.append(entry.message)
    }
}

class FormatLevel() : Format() {
    // implement

    override fun format(entry: TraceEntry, builder: StringBuilder) {
        builder.append(entry.level)
    }
}

class FormatPath() : Format() {
    // implement

    override fun format(entry: TraceEntry, builder: StringBuilder) {
        builder.append(entry.path)
    }
}

class FormatTimestamp() : Format() {
    // instance data

    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS")

    // implement

    override fun format(entry: TraceEntry, builder: StringBuilder) {
        builder.append(format.format(entry.timestamp))
    }
}

class FormatThread() : Format() {
    // implement

    override fun format(entry: TraceEntry, builder: StringBuilder) {
        builder.append(Thread.currentThread().name)
    }
}

class TraceFormatter(layout: String) {
    // instance data

    private val formatter =  parse(layout)

    // init


    // private

    private fun parse(layout: String) : Array<Format>{
        val result = ArrayList<Format>()

        var start = 0
        var pos = start

        var parameter = false

        while ( pos < layout.length) {
            if ( parameter ) {
                val element =  layout[pos]

                when ( element ) {
                    'p' -> FormatPath()
                    'l' -> FormatLevel()
                    'd' -> FormatTimestamp()
                    't' -> FormatThread()
                    'm' -> FormatMessage()
                        else -> {
                            throw Error("unknown placeholder ${element}")
                        }
                }

                start = pos +1

                parameter = false
            }

            else if ( layout[pos] == '%') {
                if ( pos - start > 0) {
                    result.add(FormatString( layout.substring(start, pos)))
                }
                parameter = true
            }

            pos++
        } // while

        if ( pos - start > 0) {
            result.add(FormatString( layout.substring(start, pos)))
        }

        return result.toTypedArray()
    }

    // public

    fun format(entry: TraceEntry) : String {
        val builder = StringBuilder()

        for ( format in this.formatter)
            format(entry)

        return builder.toString()
    }
}