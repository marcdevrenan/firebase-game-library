package br.edu.infnet.firebasegamelibrary

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FirebaseUtils {

    companion object {

        fun getDatabase() = FirebaseDatabase.getInstance().reference

        private fun getAuth() = FirebaseAuth.getInstance()

        fun getUserId() = getAuth().currentUser?.uid

        fun getAuthenticationStatus() = getAuth().currentUser != null

    }
}