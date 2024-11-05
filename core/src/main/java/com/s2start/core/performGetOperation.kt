package com.s2start.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

fun <T, A> performGetOperation(databaseQuery: () -> LiveData<T>,
                               networkCall: suspend () -> GenesisResult<A>,
                               saveCallResult: suspend (A) -> Unit): LiveData<GenesisResult<T>> =
    liveData(Dispatchers.IO) {
        emit(GenesisResult.loading())
        val source = databaseQuery.invoke().map { GenesisResult.success(it) }
        emitSource(source)

        val responseStatus = networkCall.invoke()
        if (responseStatus.status == GenesisResult.Status.SUCCESS) {
            saveCallResult(responseStatus.data!!)
        } else if (responseStatus.status == GenesisResult.Status.ERROR) {
            emit(GenesisResult.error(Exception("ao converter ")))
            emitSource(source)
        }
    }
