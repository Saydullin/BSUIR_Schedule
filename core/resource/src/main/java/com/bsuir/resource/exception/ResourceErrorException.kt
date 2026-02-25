package com.bsuir.resource.exception

import com.bsuir.resource.StatusType

class ResourceErrorException(
    val e: Throwable? = null,
    val status: String = StatusType.UNKNOWN_ERROR,
    val description: String = "",
): Throwable()