package by.devsgroup.domain.mapper

interface MapperIndexed<F, T> {

    fun map(from: F, index: Int): T

}