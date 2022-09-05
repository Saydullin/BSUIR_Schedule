package com.bsuir.bsuirschedule.domain.repository

import com.bsuir.bsuirschedule.data.db.dao.FacultyDao
import com.bsuir.bsuirschedule.domain.models.Faculty
import com.bsuir.bsuirschedule.domain.utils.Resource

interface FacultyRepository {

    val facultyDao: FacultyDao

    suspend fun getFacultiesAPI(): Resource<ArrayList<Faculty>>

    suspend fun getFaculties(): Resource<ArrayList<Faculty>>

    suspend fun getFacultyById(facultyId: Int): Resource<Faculty>

    suspend fun saveFaculties(faculties: ArrayList<Faculty>): Resource<Unit>

}