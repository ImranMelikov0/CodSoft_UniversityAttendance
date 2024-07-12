package com.imranmelikov.codsoft_universityattendance.domain

import com.imranmelikov.codsoft_universityattendance.domain.model.CRUD
import com.imranmelikov.codsoft_universityattendance.domain.model.Courses
import com.imranmelikov.codsoft_universityattendance.domain.model.User
import com.imranmelikov.codsoft_universityattendance.util.Resource

interface Repository {
    suspend fun signInUser(email:String,password:String): Resource<CRUD>

    suspend fun getCourses():Resource<List<Courses>>

    suspend fun getCourse(documentId:String):Resource<Courses>

    suspend fun getUser():Resource<User>

    suspend fun updateCourse(courses: Courses):Resource<CRUD>
}