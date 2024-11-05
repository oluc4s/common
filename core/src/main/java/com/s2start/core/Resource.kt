package com.s2start.core


data class ModelResult<out T>(val status: Status, val data: T?, val throwable: Throwable?) {

    enum class Status {
        SUCCESS,
        LOADING,
        ERROR
    }

    var isSuccess:Boolean = throwable != null
    val value:Any? = data

    companion object {
        fun <T> success(data: T): ModelResult<T> {
            return ModelResult(Status.SUCCESS, data, null)
        }

        fun <T> loading(): ModelResult<T> {
            return ModelResult(Status.LOADING, null, null)
        }

        fun <T> error(throwable: Throwable): ModelResult<T> {
            return ModelResult(Status.ERROR, null, throwable)
        }

        fun <T> ModelResult<T>.onSuccess(result:(T) -> Unit):ModelResult<T> {
            if(status == Status.SUCCESS){ this.data?.let(result) }
            return this
        }

        fun <T> ModelResult<T>.onFailure(result:(Throwable) -> Unit):ModelResult<T> {
            if(status == Status.ERROR){   this.throwable?.let { result(it) } }
            return this
        }

        fun <T> ModelResult<T>.onLoading(result:() -> Unit):ModelResult<T> {
            if(status == Status.LOADING){ result() }
            return this
        }
    }
}

fun ModelResult<*>.get(onSuccess: (Any) -> Unit, onFailure: (Throwable) -> Unit){
    this.data?.let {
        onSuccess(it)
    }
    this.throwable?.let {
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