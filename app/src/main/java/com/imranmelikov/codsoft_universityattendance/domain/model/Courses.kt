package com.imranmelikov.codsoft_universityattendance.domain.model

import java.io.Serializable

data class Courses(var course_id:String,val course_name:String,val duration:String,val student_list:MutableList<String>):Serializable