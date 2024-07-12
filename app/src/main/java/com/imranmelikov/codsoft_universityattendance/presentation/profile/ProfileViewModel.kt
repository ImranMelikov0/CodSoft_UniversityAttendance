package com.imranmelikov.codsoft_universityattendance.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imranmelikov.codsoft_universityattendance.constants.ErrorMsgConstants
import com.imranmelikov.codsoft_universityattendance.domain.Repository
import com.imranmelikov.codsoft_universityattendance.domain.model.User
import com.imranmelikov.codsoft_universityattendance.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: Repository):ViewModel() {
    private val mutableUserLiveData= MutableLiveData<Resource<User>>()
    val userLiveData: LiveData<Resource<User>>
        get() = mutableUserLiveData

    private val exceptionHandlerUser= CoroutineExceptionHandler{_,throwable->
        println("${ErrorMsgConstants.error} ${throwable.localizedMessage}")
        mutableUserLiveData.value=Resource.error(ErrorMsgConstants.errorViewModel,null)
    }

    fun getUser(){
        mutableUserLiveData.value=Resource.loading(null)
        viewModelScope.launch(Dispatchers.IO + exceptionHandlerUser){
            val response= repository.getUser()
            viewModelScope.launch(Dispatchers.Main + exceptionHandlerUser){
                response.data?.let {
                    mutableUserLiveData.value=Resource.success(it)
                }
            }
        }
    }
}