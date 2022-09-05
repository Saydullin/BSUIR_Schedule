package com.bsuir.bsuirschedule.domain.usecase

import com.bsuir.bsuirschedule.domain.models.Speciality
import com.bsuir.bsuirschedule.domain.repository.SpecialityRepository
import com.bsuir.bsuirschedule.domain.utils.Resource

class GetSpecialityUseCase(private val specialityRepository: SpecialityRepository) {

    suspend fun getSpecialitiesAPI(): Resource<ArrayList<Speciality>> {
        return specialityRepository.getSpecialitiesAPI()
    }

    suspend fun getSpecialities(): Resource<ArrayList<Speciality>> {
        return specialityRepository.getSpecialities()
    }

    suspend fun getSpecialityById(specialityId: Int): Resource<Speciality> {
        return specialityRepository.getSpecialityById(specialityId)
    }

    suspend fun saveSpecialities(specialities: ArrayList<Speciality>): Resource<Unit> {
        return specialityRepository.saveSpecialities(specialities)
    }

}