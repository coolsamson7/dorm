package com.quasar.dorm

import com.quasar.common.tracer.TraceLevel
import com.quasar.common.tracer.Tracer
import com.quasar.common.tracer.trace.ConsoleTrace
import org.junit.jupiter.api.Test

/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */
class TracerTest {
    @Test
    fun testTracer() {
        val tracer = Tracer(ConsoleTrace())

        tracer
            .setTraceLevel("", TraceLevel.OFF)
            .setTraceLevel("com", TraceLevel.LOW)
            .setTraceLevel("com.quasar", TraceLevel.MEDIUM)

        assert(!tracer.isTraced("", TraceLevel.FULL))
        assert(tracer.isTraced("com", TraceLevel.LOW))
        assert(tracer.isTraced("com.foo.bar", TraceLevel.LOW))

        Tracer.trace("com.quasar", TraceLevel.MEDIUM, "hello world")

    }
}