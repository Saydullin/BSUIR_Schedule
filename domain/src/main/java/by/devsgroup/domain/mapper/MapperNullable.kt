package by.devsgroup.domain.mapper

interface MapperNullable<F, T> {

    fun map(from: F): T?

}