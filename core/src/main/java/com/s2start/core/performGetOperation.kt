package com.s2start.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

fun <T, A> performGetOperation(databaseQuery: () -> LiveData<T>,
                               networkCall: suspend () -> ModelResult<A>,
                               saveCallResult: suspend (A) -> Unit): LiveData<ModelResult<T>> =
    liveData(Dispatchers.IO) {
        emit(ModelResult.loading())
        val source = databaseQuery.invoke().map { ModelResult.success(it) }
        emitSource(source)

        val responseStatus = networkCall.invoke()
        if (responseStatus.status == ModelResult.Status.SUCCESS) {
            saveCallResult(responseStatus.data!!)
        } else if (responseStatus.status == ModelResult.Status.ERROR) {
            emit(ModelResult.error(Exception("Error Parse To Model")))
            emitSource(source)
        }
    }
