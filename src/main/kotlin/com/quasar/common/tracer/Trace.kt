package com.quasar.common.tracer
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */
abstract class Trace {
    // protected

    abstract fun trace(entry: TraceEntry)
}