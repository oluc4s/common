package com.s2start.core

import retrofit2.Response

abstract class BaseDataSource {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): ModelResult<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return ModelResult.success(body)
            }
            return error("${response.code()} ${response.message()} ${response}")
        } catch (e: Exception) {
            return error(e)
        }
    }

    private fun <T> error(e: Exception): ModelResult<T> {
        return ModelResult.error(exception = e)
    }
}