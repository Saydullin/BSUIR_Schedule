package by.devsgroup.resource

import by.devsgroup.resource.exception.ResourceErrorException

sealed class Resource<T>(
    val data: T? = null,
    val status: String = StatusType.Companion.UNKNOWN_ERROR,
    val e: Throwable? = null,
    val message: String = "null",
) {

    companion object {
        fun <T> tryWith(scope: () -> T): Resource<T> {
            return try {
                Success(scope())
            } catch (e: Exception) {
                e.printStackTrace()
                Error(e)
            }
        }
        fun <T> tryWith(statusType: String, scope: () -> T): Resource<T> {
            return try {
                Success(scope())
            } catch (e: Exception) {
                e.printStackTrace()
                Error(e, statusType)
            }
        }
        suspend fun <T> tryWithSuspend(scope: suspend Resource<T>.() -> T): Resource<T> {
            return try {
                val resource = Success<T>(null)
                val result = resource.scope()
                Success(result)
            } catch (e: ResourceErrorException) {
                e.printStackTrace()
                Error(e.e, e.status, e.description)
            } catch (e: Exception) {
                Error(e)
            }
        }
        suspend fun <T> tryWithSuspend(statusType: String, scope: suspend Resource<T>.() -> T): Resource<T> {
            return try {
                val resource = Success<T>(null)
                val result = resource.scope()
                Success(result)
            } catch (e: ResourceErrorException) {
                e.printStackTrace()
                Error(e.e, e.status, e.description)
            } catch (e: Exception) {
                Error(e, statusType)
            }
        }
    }

    @Deprecated("")
    @SuppressWarnings
    fun interruptWith(
        e: Throwable? = null,
        status: String = StatusType.Companion.UNKNOWN_ERROR,
        description: String = "null",
    ) {
        throw ResourceErrorException(e, status, description)
    }

    suspend fun onSuspendError(scope: suspend (Error<Unit>) -> Unit): T? {
        return if (isErrorData() || data == null) {
            scope(getError())
            null
        } else {
            data
        }
    }

    fun getStatusAndMessage(): String {
        return "$status, $message"
    }

    fun getOrNull(): T? {
        return if (isErrorData() || data == null) null else data
    }

    fun getOrThrow(scope: (Error<Unit>) -> Throwable): T {
        if (isErrorData() || data == null) {
            val throwable = try {
                scope(getError())
            } catch (e: Exception) {
                e
            }

            throw throwable
        }

        return data
    }

    fun getOrThrow(): T {
        return if (isErrorData() || data == null) {
            throw ResourceErrorException(e, status, message)
        } else {
            data
        }
    }

    fun onError(scope: (Error<Unit>) -> Unit): T? {
        return if (isErrorData() || data == null) {
            scope(getError())

            null
        } else {
            data
        }
    }

    fun isSuccessData(): Boolean {
        return this is Success<T> && this.data != null
    }

    fun isErrorData(): Boolean {
        return this is Error
    }

    fun <T> getError(): Error<T> {
        return Error(
            e = this.e,
            status = this.status,
            message = this.message,
        )
    }

    class Success<T>(
        data: T?
    ): Resource<T>(data)

    class Error<T>(
        e: Throwable? = null,
        status: String = StatusType.Companion.UNKNOWN_ERROR,
        message: String = "null",
        data: T? = null
    ): Resource<T>(data, status, e, message) {

        companion object {
            fun <T> successType() = Error<T>(
                status = StatusType.Companion.SUCCESS
            )
        }

    }

}