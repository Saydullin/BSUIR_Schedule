package by.devsgroup.resource.exception

import by.devsgroup.resource.StatusType

class ResourceErrorException(
    val e: Throwable? = null,
    val status: String = StatusType.UNKNOWN_ERROR,
    val description: String = "",
): Throwable()