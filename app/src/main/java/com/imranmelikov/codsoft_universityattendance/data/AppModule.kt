package com.imranmelikov.codsoft_universityattendance.data

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.imranmelikov.codsoft_universityattendance.domain.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun injectFireBaseAuth():FirebaseAuth{
        return FirebaseAuth.getInstance()
    }
    @Provides
    @Singleton
    fun injectFireStore():FirebaseFirestore=FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun injectRepo(auth: FirebaseAuth,firestore: FirebaseFirestore,@ApplicationContext context: Context)=RepositoryImpl(auth,firestore,context) as Repository
}