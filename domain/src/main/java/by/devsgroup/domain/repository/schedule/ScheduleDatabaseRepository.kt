package by.devsgroup.domain.repository.schedule

import by.devsgroup.domain.model.schedule.full.FullSchedule
import by.devsgroup.domain.model.schedule.preview.PreviewSchedule
import by.devsgroup.resource.Resource

interface ScheduleDatabaseRepository {

    suspend fun getScheduleById(id: Long): Resource<PreviewSchedule?>

    suspend fun getFullScheduleById(id: Long): Resource<FullSchedule?>

    suspend fun saveSchedule(schedule: FullSchedule): Resource<Unit>

    suspend fun clear(): Resource<Unit>

}