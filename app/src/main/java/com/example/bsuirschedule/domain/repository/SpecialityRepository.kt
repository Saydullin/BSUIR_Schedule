package com.example.bsuirschedule.domain.repository

import com.example.bsuirschedule.data.db.dao.SpecialityDao
import com.example.bsuirschedule.domain.models.Speciality
import com.example.bsuirschedule.domain.utils.Resource

interface SpecialityRepository {

    val specialityDao: SpecialityDao

    suspend fun getSpecialitiesAPI(): Resource<ArrayList<Speciality>>

    suspend fun getSpecialities(): Resource<ArrayList<Speciality>>

    suspend fun getSpecialityById(specialityId: Int): Resource<Speciality>

    suspend fun saveSpecialities(specialities: ArrayList<Speciality>): Resource<Unit>

}