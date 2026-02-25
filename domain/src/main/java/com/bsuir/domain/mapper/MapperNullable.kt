package com.bsuir.domain.mapper

interface MapperNullable<F, T> {

    fun map(from: F): T?

}