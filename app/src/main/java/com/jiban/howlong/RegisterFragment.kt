package com.jiban.howlong

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jiban.howlong.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding !!

    private var myPhoneNumber: String? = null
    private lateinit var auth: FirebaseAuth


    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser

        //updateUI(currentUser)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        binding.authBtn.setOnClickListener {

// Initialize Firebase Auth
            myPhoneNumber = binding.phoneEt.text.toString()
            auth = Firebase.auth

            /*
            customToken?.let {
                auth.signInWithCustomToken(it)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("JJS", "signInWithCustomToken:success")
                            val user = auth.currentUser
                            updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("JJS", "signInWithCustomToken:failure", task.exception)
                            Toast.makeText(context, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                            updateUI(null)
                        }
                    }
            }

             */


        }

        binding.addBtn.setOnClickListener {

            val myName: String = binding.nameEt.text.toString()
            val myPassword: String = binding.passwordEt.text.toString()
            val myBirthYear: String = binding.yearEt.text.toString()
            val myBirthMonth: String = binding.monthEt.text.toString()
            val myBirthDay: String = binding.dayEt.text.toString()
            val myBirthTime: String = binding.timeEt.text.toString()


            val db = Firebase.firestore
            // Create a new user with a first and last name
            val user = hashMapOf(

                "phoneNumber" to myPhoneNumber,
                "name" to myName,
                "password" to myPassword,
                "birthYear" to myBirthYear,
                "birthMonth" to myBirthMonth,
                "birthDay" to myBirthDay,
                "birthTime" to myBirthTime
            )
// Add a new document with a generated ID
            db.collection("user")
                .add(user)
                .addOnSuccessListener { documentReference ->
                    Log.d("JJS Register", "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w("JJS Register", "Error adding document", e)
                }

        }
        val view = binding.root
        return view

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
