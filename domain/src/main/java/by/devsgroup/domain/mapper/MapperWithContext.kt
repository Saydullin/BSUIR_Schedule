package by.devsgroup.domain.mapper

interface MapperWithContext<F, T, C> {

    fun map(from: F, context: C): T

}