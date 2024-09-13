package com.example.dorm.query

class QueryResult<T : Any>(val query: Query<T>, val result : List<T>) {
    // public

    fun getResultList() : List<T> {
        return result
    }

    fun getSingleResult() : T? {
        return if (result.isNotEmpty()) result[0] else null
    }
}