package by.devsgroup.domain.model.error

sealed class ErrorType {

    object DatabaseError : ErrorType()

    object UnknownError : ErrorType()

    object NetworkError : ErrorType()

    object ServerError : ErrorType()

    object CodeError : ErrorType()

}