package com.emarket.core

sealed class Response<out T>(val status: ResponseStatus?, val data: T?, val message: String?) {
    data class Success<out T>(val _data: T?) : Response<T>(
        status = ResponseStatus.SUCCESS,
        data = _data,
        message = null
    )

    data class Error(val _code: String?, val _message: String?) : Response<Nothing>(
        status = ResponseStatus.ERROR,
        data = null,
        message = _message
    )

    object Loading : Response<Nothing>(
        status = ResponseStatus.LOADING,
        data = null,
        message = null
    )
}

enum class ResponseStatus {
    SUCCESS,
    ERROR,
    LOADING
}