package com.s2start.core


data class GenesisResult<out T>(val status: Status, val data: T?, val exception: Exception?) {

    enum class Status {
        SUCCESS,
        LOADING,
        ERROR
    }

    companion object {
        fun <T> success(data: T): GenesisResult<T> {
            return GenesisResult(Status.SUCCESS, data, null)
        }

        fun <T> loading(): GenesisResult<T> {
            return GenesisResult(Status.LOADING, null, null)
        }

        fun <T> error(exception: Exception): GenesisResult<T> {
            return GenesisResult(Status.ERROR, null, exception)
        }

        fun <T> GenesisResult<T>.onSuccess(result:(T) -> Unit):GenesisResult<T> {
            if(status == Status.SUCCESS){ this.data?.let(result) }
            return this
        }

        fun <T> GenesisResult<T>.onFailure(result:(Exception) -> Unit):GenesisResult<T> {
            if(status == Status.ERROR){   this.exception?.let { result(it) } }
            return this
        }

        fun <T> GenesisResult<T>.onLoading(result:() -> Unit):GenesisResult<T> {
            if(status == Status.LOADING){ result() }
            return this
        }
    }
}

fun GenesisResult<*>.get(onSuccess: (Any) -> Unit, onFailure: (Exception) -> Unit){
    this.data?.let {
        onSuccess(it)
    }
    this.exception?.let {
        onFailure(it)
    }
}

suspend fun <T> getResult(call: suspend () -> GenesisResult<T?>): GenesisResult<T> {
    try {
        val response = call()
        return if(response.data != null){
            GenesisResult.success(response.data)
        }else{
            GenesisResult.error(java.lang.Exception("getResult null object"))
        }
    } catch (e: Exception) {
        return error(e)
    }
}