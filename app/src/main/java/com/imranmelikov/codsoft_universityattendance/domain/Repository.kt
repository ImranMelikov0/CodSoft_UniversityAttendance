package com.imranmelikov.codsoft_universityattendance.domain

import com.imranmelikov.codsoft_universityattendance.domain.model.CRUD
import com.imranmelikov.codsoft_universityattendance.util.Resource

interface Repository {
    suspend fun signInUser(email:String,password:String): Resource<CRUD>
}