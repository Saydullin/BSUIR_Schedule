package by.devsgroup.domain.mapper

interface Mapper<F, T> {

    fun map(from: F): T

}