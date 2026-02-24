package by.devsgroup.schedule.ui.model

sealed class ScheduleDateFilter {

    object FromNow: ScheduleDateFilter()

    class From(val dateMillis: Long): ScheduleDateFilter()

    class Until(val dateMillis: Long): ScheduleDateFilter()

}