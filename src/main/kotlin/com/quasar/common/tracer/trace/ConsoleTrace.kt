package com.quasar.common.tracer.trace
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import com.quasar.common.tracer.Trace
import com.quasar.common.tracer.TraceEntry
import com.quasar.common.tracer.TraceFormatter

class ConsoleTrace : Trace() {
    // override

    override fun trace(entry: TraceEntry, format: TraceFormatter) {
        println(format.format(entry))
    }
}