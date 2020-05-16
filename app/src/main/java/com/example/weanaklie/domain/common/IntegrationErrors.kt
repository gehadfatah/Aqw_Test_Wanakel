package com.android.friendycar.domain.common
class UnAuthorizedException(message: String) : Exception(message)
class InternalServerErrorException : Exception()
class RequestErrorException(  message: String) : Exception( message)