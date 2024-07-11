package com.imranmelikov.codsoft_universityattendance.data

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.imranmelikov.codsoft_universityattendance.constants.ErrorMsgConstants
import com.imranmelikov.codsoft_universityattendance.domain.Repository
import com.imranmelikov.codsoft_universityattendance.domain.model.CRUD
import com.imranmelikov.codsoft_universityattendance.util.Resource
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RepositoryImpl(private val auth: FirebaseAuth,private val firestore: FirebaseFirestore,private val context: Context):Repository {
    override suspend fun signInUser(email: String, password: String): Resource<CRUD> {
        return try {
            suspendCoroutine { continuation ->
                auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
                    it.user?.let {user->
                        user.email?.let {email->
                            continuation.resume(Resource.success(CRUD(email,4)))
                        }
                    }
                }.addOnFailureListener {e ->
                    Toast.makeText(context,e.localizedMessage, Toast.LENGTH_SHORT).show()
                    continuation.resume(Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}", null))
                }
            }
        }catch (e:Exception){
            Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}", null)
        }
    }
}