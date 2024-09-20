package com.quasar.common.tracer

import java.text.SimpleDateFormat
import java.util.*

/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */
abstract class Trace {
    // private

    private fun formatDate(date: Date): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS").format(date)
    }

    // protected

    protected fun format(entry: TraceEntry) : String {
        return "${formatDate(entry.timestamp)} ${entry.level} [${entry.path}] ${entry.message}"
    }

    // abstract

    abstract fun trace(entry: TraceEntry)
}