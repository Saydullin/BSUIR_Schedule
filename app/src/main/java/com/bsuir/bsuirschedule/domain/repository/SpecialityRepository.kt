package com.bsuir.bsuirschedule.domain.repository

import com.bsuir.bsuirschedule.data.db.dao.SpecialityDao
import com.bsuir.bsuirschedule.domain.models.Speciality
import com.bsuir.bsuirschedule.domain.utils.Resource

interface SpecialityRepository {

    val specialityDao: SpecialityDao

    suspend fun getSpecialitiesAPI(): Resource<ArrayList<Speciality>>

    suspend fun getSpecialities(): Resource<ArrayList<Speciality>>

    suspend fun getSpecialityById(specialityId: Int): Resource<Speciality>

    suspend fun saveSpecialities(specialities: ArrayList<Speciality>): Resource<Unit>

}