package com.quasar.common.tracer.trace
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import com.quasar.common.tracer.Trace
import com.quasar.common.tracer.TraceEntry

class ConsoleTrace : Trace() {
    // override

    override fun trace(entry: TraceEntry) {
        println(entry.message) // TODO
    }
}