package com.example.bsuirschedule.domain.repository

import com.example.bsuirschedule.data.db.dao.FacultyDao
import com.example.bsuirschedule.domain.models.Faculty
import com.example.bsuirschedule.domain.utils.Resource

interface FacultyRepository {

    val facultyDao: FacultyDao

    suspend fun getFacultiesAPI(): Resource<ArrayList<Faculty>>

    suspend fun getFaculties(): Resource<ArrayList<Faculty>>

    suspend fun getFacultyById(facultyId: Int): Resource<Faculty>

    suspend fun saveFaculties(faculties: ArrayList<Faculty>): Resource<Unit>

}