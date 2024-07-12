package com.imranmelikov.codsoft_universityattendance.data

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.imranmelikov.codsoft_universityattendance.constants.ErrorMsgConstants
import com.imranmelikov.codsoft_universityattendance.constants.FireStoreCollectionConstants
import com.imranmelikov.codsoft_universityattendance.constants.FireStoreConstants
import com.imranmelikov.codsoft_universityattendance.domain.Repository
import com.imranmelikov.codsoft_universityattendance.domain.model.CRUD
import com.imranmelikov.codsoft_universityattendance.domain.model.Courses
import com.imranmelikov.codsoft_universityattendance.domain.model.User
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

    override suspend fun getCourses(): Resource<List<Courses>> {
        return try {
            suspendCoroutine { continuation ->
                val courseList= mutableListOf<Courses>()
                val studentList= mutableListOf<String>()
                firestore.collection(FireStoreCollectionConstants.courses).get().addOnSuccessListener { querySnapshot->
                    if (querySnapshot.isEmpty){
                        continuation.resume(Resource.success(emptyList()))
                    }else {
                        for (document in querySnapshot.documents) {
                            val arrayList = document.get(FireStoreConstants.student_list) as? ArrayList<*>
                            arrayList?.let {
                                for (student in it) {
                                  studentList.add(student as String)
                                }
                            }
                            val course = Courses(
                                course_id = document.getString(FireStoreConstants.course_id) as String,
                                course_name = document.getString(FireStoreConstants.course_name) as String,
                                duration = document.getString(FireStoreConstants.duration) as String,
                                student_list = studentList
                            )
                            courseList.add(course)
                        }
                        continuation.resume(Resource.success(courseList))
                    }
                }.addOnFailureListener {e->
                    Toast.makeText(context,e.localizedMessage,Toast.LENGTH_SHORT).show()
                    continuation.resume(Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}",null))
                }
            }
        }catch (e:Exception){
            Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}",null)
        }
    }

    override suspend fun getCourse(documentId:String): Resource<Courses> {
        return try {
            suspendCoroutine { continuation ->
                val studentList= mutableListOf<String>()
                firestore.collection(FireStoreCollectionConstants.courses).document(documentId).get().addOnSuccessListener { document->
                            val arrayList = document.get(FireStoreConstants.student_list) as? ArrayList<*>
                            arrayList?.let {
                                for (student in it) {
                                    studentList.add(student as String)
                                }
                            }
                            val course = Courses(
                                course_id = document.getString(FireStoreConstants.course_id) as String,
                                course_name = document.getString(FireStoreConstants.course_name) as String,
                                duration = document.getString(FireStoreConstants.duration) as String,
                                student_list = studentList
                            )
                        continuation.resume(Resource.success(course))
                }.addOnFailureListener {e->
                    Toast.makeText(context,e.localizedMessage,Toast.LENGTH_SHORT).show()
                    continuation.resume(Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}",null))
                }
            }
        }catch (e:Exception){
            Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}",null)
        }
    }

    override suspend fun getUser(): Resource<User> {
        return try {
            suspendCoroutine { continuation ->
                auth.currentUser?.let { userFireBase->
                    userFireBase.email?.let {email->
                        firestore.collection(FireStoreCollectionConstants.users).document(email)
                            .collection(FireStoreCollectionConstants.user)
                            .document(userFireBase.uid).get().addOnSuccessListener {document->
                                        val user=User(
                                            user_id = document.getString(FireStoreConstants.user_id) as String,
                                            student = document.getString(FireStoreConstants.student) as String,
                                            start_date = document.getString(FireStoreConstants.start_date) as String,
                                            email = document.getString(FireStoreConstants.email) as String,
                                            collage_name = document.getString(FireStoreConstants.collage_name) as String,
                                            academic_degree = document.getString(FireStoreConstants.academic_degree) as String,
                                        )
                                    continuation.resume(Resource.success(user))
                            }.addOnFailureListener {e ->
                                Toast.makeText(context,e.localizedMessage,Toast.LENGTH_SHORT).show()
                                continuation.resume(Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}", null))
                            }
                    }
                }
            }
        }catch (e:Exception){
            Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}", null)
        }
    }
    override suspend fun updateCourse(courses: Courses): Resource<CRUD> {
        return try {
            suspendCoroutine { continuation ->
                        firestore.collection(FireStoreCollectionConstants.courses).document(courses.course_id).set(courses).addOnSuccessListener {
                                continuation.resume(Resource.success(CRUD(courses.course_id,3)))
                            }.addOnFailureListener {e ->
                                Toast.makeText(context,e.localizedMessage,Toast.LENGTH_SHORT).show()
                                continuation.resume(Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}", null))
                            }
            }
        }catch (e:Exception){
            Resource.error("${ErrorMsgConstants.errorFromFirebase} ${e.localizedMessage}", null)
        }
    }

}