package org.sirius.dorm

import org.sirius.common.tracer.TraceLevel
import org.sirius.common.tracer.Tracer
import org.sirius.common.tracer.trace.ConsoleTrace
import org.junit.jupiter.api.Test

/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */
class TracerTest {
    @Test
    fun testTracer() {
        val tracer = Tracer(ConsoleTrace(), "%d{yyyy-MM-dd HH:mm:ss,SSS} %l{-6s} [%p{-10s}] %m")

        tracer
            .setTraceLevel("", TraceLevel.OFF)
            .setTraceLevel("com", TraceLevel.LOW)
            .setTraceLevel("com.quasar", TraceLevel.MEDIUM)

        assert(!tracer.isTraced("", TraceLevel.FULL))
        assert(tracer.isTraced("com", TraceLevel.LOW))
        assert(tracer.isTraced("com.foo.bar", TraceLevel.LOW))

        Tracer.trace("com.quasar", TraceLevel.LOW, "hello world")
        Tracer.trace("com.quasar", TraceLevel.MEDIUM, "hello world")

    }
}