package com.example.aston_courseproject_rickmorty.model.retrofit


data class ApiState<out T>(val status: Status, val data: T?, val message: String?) {

    companion object {
        fun <T> success(data: T?): ApiState<T> {
            return ApiState(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String): ApiState<T> {
            return ApiState(Status.ERROR, null, msg)
        }

        fun <T> loading(): ApiState<T> {
            return ApiState(Status.LOADING, null, null)
        }
    }
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}