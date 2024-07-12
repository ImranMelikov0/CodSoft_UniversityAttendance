package com.imranmelikov.codsoft_universityattendance.presentation.courses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imranmelikov.codsoft_universityattendance.constants.ErrorMsgConstants
import com.imranmelikov.codsoft_universityattendance.domain.Repository
import com.imranmelikov.codsoft_universityattendance.domain.model.CRUD
import com.imranmelikov.codsoft_universityattendance.domain.model.Courses
import com.imranmelikov.codsoft_universityattendance.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoursesViewModel @Inject constructor(private val repository: Repository):ViewModel() {

    private val coursesMutableLiveData= MutableLiveData<Resource<List<Courses>>>()
    val coursesLiveData: LiveData<Resource<List<Courses>>>
        get() = coursesMutableLiveData

    private val exceptionHandlerCourses = CoroutineExceptionHandler { _, throwable ->
        println("${ErrorMsgConstants.error} ${throwable.localizedMessage}")
        coursesMutableLiveData.value= Resource.error(ErrorMsgConstants.errorViewModel,null)
    }
    fun getCourses(){
        coursesMutableLiveData.value=Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO + exceptionHandlerCourses) {
            val response= repository.getCourses()
            viewModelScope.launch(Dispatchers.Main + exceptionHandlerCourses){
                response.data?.let {courses->
                    coursesMutableLiveData.value=Resource.success(courses)
                }
            }
        }
    }

}