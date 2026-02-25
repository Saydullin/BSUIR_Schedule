package com.bsuir.domain.mapper

interface Mapper<F, T> {

    fun map(from: F): T

}