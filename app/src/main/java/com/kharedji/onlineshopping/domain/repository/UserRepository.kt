package com.kharedji.onlineshopping.domain.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.kharedji.onlineshopping.data.utils.State
import com.kharedji.onlineshopping.domain.models.UsersData
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    val auth: FirebaseAuth

    suspend fun signUpUser(email: UsersData, password: String): Flow<State<AuthResult>>
    suspend fun signInUser(email: String, password: String): Flow<State<AuthResult>>
  
}