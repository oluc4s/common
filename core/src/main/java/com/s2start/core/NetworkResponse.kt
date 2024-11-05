package com.s2start.core


data class NetworkResponse<out T>(val status: Status, val data: T?, val exception: Exception?) {

    enum class Status {
        SUCCESS,
        LOADING,
        ERROR
    }

    companion object {
        fun <T> success(data: T): NetworkResponse<T> {
            return NetworkResponse(Status.SUCCESS, data, null)
        }

        fun <T> loading(): NetworkResponse<T> {
            return NetworkResponse(Status.LOADING, null, null)
        }

        fun <T> error(exception: Exception): NetworkResponse<T> {
            return NetworkResponse(Status.ERROR, null, exception)
        }

        fun <T> NetworkResponse<T>.onSuccess(result:(T) -> Unit):NetworkResponse<T> {
            if(status == Status.SUCCESS){ this.data?.let(result) }
            return this
        }

        fun <T> NetworkResponse<T>.onFailure(result:(Exception) -> Unit):NetworkResponse<T> {
            if(status == Status.ERROR){   this.exception?.let { result(it) } }
            return this
        }

        fun <T> NetworkResponse<T>.onLoading(result:() -> Unit):NetworkResponse<T> {
            if(status == Status.LOADING){ result() }
            return this
        }
    }
}

fun NetworkResponse<*>.get(onSuccess: (Any) -> Unit, onFailure: (Exception) -> Unit){
    this.data?.let {
        onSuccess(it)
    }
    this.exception?.let {
        onFailure(it)
    }
}

fun <T> ModelResult<T?>.notNull(): ModelResult<T> = this.map{
    it ?: throw NullPointerException()
}

inline fun <T,R> ModelResult<T>.map(
    transform:(value:T) -> R
): ModelResult<R> = when {
    isSuccess -> result { transform(this.value as T) }
    else -> ModelResult.error(Throwable("error model"))
}

inline fun <T> result(block: () -> T): ModelResult<T> = runCatching {
    ModelResult.success(block())
}.getOrElse { error ->
    ModelResult.error(error)
}