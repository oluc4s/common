package com.s2start.core


data class ModelResult<out T>(val status: Status, val data: T?, val exception: Exception?) {

    enum class Status {
        SUCCESS,
        LOADING,
        ERROR
    }

    companion object {
        fun <T> success(data: T): ModelResult<T> {
            return ModelResult(Status.SUCCESS, data, null)
        }

        fun <T> loading(): ModelResult<T> {
            return ModelResult(Status.LOADING, null, null)
        }

        fun <T> error(exception: Exception): ModelResult<T> {
            return ModelResult(Status.ERROR, null, exception)
        }

        fun <T> ModelResult<T>.onSuccess(result:(T) -> Unit):ModelResult<T> {
            if(status == Status.SUCCESS){ this.data?.let(result) }
            return this
        }

        fun <T> ModelResult<T>.onFailure(result:(Exception) -> Unit):ModelResult<T> {
            if(status == Status.ERROR){   this.exception?.let { result(it) } }
            return this
        }

        fun <T> ModelResult<T>.onLoading(result:() -> Unit):ModelResult<T> {
            if(status == Status.LOADING){ result() }
            return this
        }
    }
}

fun ModelResult<*>.get(onSuccess: (Any) -> Unit, onFailure: (Exception) -> Unit){
    this.data?.let {
        onSuccess(it)
    }
    this.exception?.let {
        onFailure(it)
    }
}

suspend fun <T> getResult(call: suspend () -> ModelResult<T?>): ModelResult<T> {
    try {
        val response = call()
        return if(response.data != null){
            ModelResult.success(response.data)
        }else{
            ModelResult.error(java.lang.Exception("null object"))
        }
    } catch (e: Exception) {
        return error(e)
    }
}