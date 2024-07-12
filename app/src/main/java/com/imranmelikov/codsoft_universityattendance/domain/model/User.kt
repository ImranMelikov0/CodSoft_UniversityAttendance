package com.imranmelikov.codsoft_universityattendance.domain.model

import java.io.Serializable

data class User(var user_id:String,val email:String,val student:String,val start_date:String,val collage_name:String,val academic_degree:String):Serializable
